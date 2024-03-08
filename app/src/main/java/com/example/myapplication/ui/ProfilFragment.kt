package com.example.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.ViewModel.ProfilViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class ProfilFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var textUsername: TextView
    private val viewModel: ProfilViewModel by viewModels()
    private lateinit var authViewModel: AuthManager
    private lateinit var userRepository: UserRepository
    private lateinit var imageProfile: ImageView
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var lastLocation : Location;
    private lateinit var imageUri : String;
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient;

    private

    companion object {
        private const val REQUEST_CHECK_SETTINGS = 1001
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)
        authViewModel = AuthManager(requireContext())
        userRepository = UserRepository()
        imageProfile = view.findViewById(R.id.imageProfile)
        textUsername = view.findViewById(R.id.textUsername)

        mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        textUsername.text = username
        Log.e("ProfilFragment", "User " + sharedPref.getString("username", ""))

        val deleteAccountButton = view.findViewById<Button>(R.id.btnDeleteAccount)
        val decoBtn = view.findViewById<Button>(R.id.btnDeconnexion)
        val changepwd = view.findViewById<Button>(R.id.btnChangePassword)

        changepwd.setOnClickListener {
            findNavController().navigate(R.id.action_profilFragment_to_passwordFragment)
        }
        decoBtn.setOnClickListener {
            deconnexionUtilisateur()
        }
        deleteAccountButton.setOnClickListener {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val username = sharedPref.getString("username", "") ?: ""
            lifecycleScope.launch {
                val user = userRepository.getUserByUsername(username)
                if (user != null) {
                    userRepository.deleteUser(user)
                    Toast.makeText(
                        requireContext(),
                        "Votre compte a été supprimé avec succès",
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_profilFragment_to_userFragment)
                } else {
                    Toast.makeText(requireContext(), "Impossible de supp", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
        viewModel.navigateToUserFragment.observe(viewLifecycleOwner, Observer { shouldNavigate ->
            Log.d("ProfilFragment", "Navigate to UserFragment: $shouldNavigate")
            if (shouldNavigate) {
                findNavController().navigate(R.id.action_profilFragment_to_userFragment)
                viewModel.onUserFragmentNavigated()
            }
        })

        imageProfile.setOnClickListener {
            showDeleteConfirmationDialog()
        }

        view.findViewById<Button>(R.id.btnAddPhoto).setOnClickListener {
//            openImagePicker()
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)

            lifecycleScope.launch {
                try {
                    val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                    val username = sharedPref.getString("username", "") ?: ""
                    Log.d("Saved  uriii ", "Path: ${imageUri}")
                    val userId = viewModel.getId(username)
                    if (userId != null && imageUri != null) {
                        viewModel.updateProfileImage(userId!!, imageUri!!)
                    } else {
                        Log.d("erreur", "Path: ")
                    }
                } catch (e: Exception) {
                    Log.d("execption", "Path: ${e}")
                }
            }
        }

        view.findViewById<Button>(R.id.btnAddCamPhoto).setOnClickListener {
            checkCameraPermission()
        }

        viewModel.getUserImageUri(username).observe(viewLifecycleOwner) { uri ->
            uri?.let {
                loadImageFromPath(it)
            }
        }

       viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Log.d("Saved", "Path: ${uri}")
                lifecycleScope.launch {
                    val newPath = saveImageFromUri(Uri.parse(uri))
                    if (newPath != null) {
                        loadImageFromPath(newPath)
                        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                        val username = sharedPref.getString("username", "") ?: ""
                        val userId = viewModel.getId(username)
                        viewModel.updateProfileImage(userId, newPath)
                    } else {
                        imageProfile.setImageResource(R.drawable.ic_profil)
                    }

                }

            }
        }

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationProfil)
        val navController = findNavController()

        bottomNavigationView.setupWithNavController(navController)

        return view
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(context)
            .setTitle("Supprimer la photo")
            .setMessage("Êtes-vous sûr de vouloir supprimer votre photo de profil ?")
            .setPositiveButton("Supprimer") { dialog, which ->
                deleteProfileImage()
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun deleteProfileImage() {
        lifecycleScope.launch {
            val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
            val username = sharedPref.getString("username", "") ?: ""
            val userId = viewModel.getId(username)
            viewModel.updateProfileImages(userId, null)
            imageProfile.setImageResource(R.drawable.ic_profil)
        }
    }


    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 2)
        } else {
            Log.d("check", "appel open")
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 2 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera()
        } else {
            Toast.makeText(context, "Permission Camera refusée", Toast.LENGTH_SHORT).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            imageUri = absolutePath
        }
    }

    private fun openCamera() {

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.d("open", "dans open ${intent}")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            Log.d("open if", "dans open")
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                Log.d("execption", "Path: ${ex}")
                null
            }
            Log.d("open photo", "dans open ${photoFile}")
            photoFile?.also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this.requireContext(),
                    "${this.requireContext().packageName}.provider", it
                )
                Log.d("open uri", "Path: ${photoURI}")
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, 2)
            }
        }
    }


    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(this.requireContext())

        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            Log.d("Saved on act data", "Path: ${data.data}")
            selectedImageUri?.let {

                imageUri = it.toString()
                viewModel.setImageUri(it.toString())
                Log.d("Saved on act", "Path: ${it}")
            }
        } else if (requestCode == 2 && data != null) {
            val photo = data.extras?.get("data") as Bitmap
            val savedUri = saveImageToInternalStorage(photo)
            savedUri?.let {
                imageUri = it.toString()
                viewModel.setImageUri(it.toString())
                Log.d("Saved on act", "Path: ${it}")
            }
        }
    }


    private fun showUserLocationOnMap(latitude: Double, longitude: Double) {
    Timber.tag("coord show").d("coord " + longitude + ")")
        val userLatLng = LatLng(latitude, longitude)
    mGoogleMap?.addMarker(MarkerOptions().position(userLatLng).title("Votre position"))
    mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)) // Zoom sur la position de l'utilisateur
    }

    @SuppressLint("RestrictedApi")
    private fun getCurrentLocationn() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        Timber.tag("coord").d("coord " + longitude + ")")
                        showUserLocationOnMap(latitude, longitude)
                    }
                }
                .addOnFailureListener { e ->
                    Timber.tag(TAG).e("Erreur lors de l'obtention de la localisation: " + e.message)
                }
        }
    }
    @SuppressLint("RestrictedApi")
    override fun onMapReady(googleMap: GoogleMap) {
        mGoogleMap = googleMap
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        mGoogleMap.setOnMarkerClickListener(this)
        setUpMap()
   }

    private fun setUpMap (){
        if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this.requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

            return
        }
        mGoogleMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this.requireActivity()) {
            location ->
            Log.d("coooord", "test"+ location + "test")
            if(location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

            }
        }
    }

    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("My position")
        mGoogleMap.addMarker(markerOptions)
    }

    private fun saveImageFromUri(uri: Uri): String? {
        try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = File(requireContext().externalCacheDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
    private fun loadImageFromPath(imagePath: String?) {
        if (imagePath != null) {
            Glide.with(requireContext())
                .load(imagePath)
                .circleCrop()
                .into(imageProfile)
            imageProfile.visibility = View.VISIBLE
           Log.d("Image Loading", "Chargement de l'image à partir du chemin : $imagePath")
        } else {
            Log.e("Image Loading", "Le chemin de l'image est vide.")
        }
    }

    private fun openImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, 1)
    }

    private fun deconnexionUtilisateur() {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            remove("username")
            apply()
        }
        Toast.makeText(requireContext(), "Vous etes deconnecté", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_profilFragment_to_userFragment)
    }

    override fun onMarkerClick(p0: Marker) = false

}