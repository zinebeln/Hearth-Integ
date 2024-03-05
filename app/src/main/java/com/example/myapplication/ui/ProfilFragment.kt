package com.example.myapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.TAG
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.User
import com.example.myapplication.model.ViewModel.AuthViewModel
import com.example.myapplication.model.ViewModel.ProfilViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.C
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfilFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var textUsername: TextView
    private val viewModel: ProfilViewModel by viewModels()
    private lateinit var authViewModel: AuthManager
    private lateinit var userObserver: Observer<User>
    private lateinit var userRepository: UserRepository
    private lateinit var imageProfile: ImageView

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mGoogleMap: GoogleMap
    private val FINE_PERMISSION_CODE = 1;
    private lateinit var lastLocation : Location;
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient;
    private val permissionCode = 101;

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

     //  requestLocationPermission()
//        getCurrentLocationn()

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
                // Indiquez au ViewModel que la navigation a été effectuée
                viewModel.onUserFragmentNavigated()
            }
        })

        view.findViewById<Button>(R.id.btnAddPhoto).setOnClickListener {
            openImagePicker()
        }

        view.findViewById<Button>(R.id.btnAddCamPhoto).setOnClickListener {
            // openCamera()
        }
       viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                Log.d("Saved", "Path: ${uri}")
                val newPath = saveImageFromUri(Uri.parse(uri))
                if (newPath != null) {
                    loadImageFromPath(newPath)
                } else {
                    imageProfile.setImageResource(R.drawable.ic_profil)
                }
            }
        }

        val bottomNavigationView =
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationProfil)
        val navController = findNavController()

        bottomNavigationView.setupWithNavController(navController)

        return view
    }

//
    private fun showUserLocationOnMap(latitude: Double, longitude: Double) {
    Timber.tag("coord show").d("coord " + longitude + ")")
        val userLatLng = LatLng(latitude, longitude)
    mGoogleMap?.addMarker(MarkerOptions().position(userLatLng).title("Votre position"))
    mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)) // Zoom sur la position de l'utilisateur
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1001
            )
        } else {
            getCurrentLocationn()
        }
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
                    // Obtenez la localisation actuelle ici
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        Timber.tag("coord").d("coord " + longitude + ")")
                        // Utilisez les coordonnées pour afficher la localisation de l'utilisateur sur la carte

                        showUserLocationOnMap(latitude, longitude)
                    }
                }
                .addOnFailureListener { e ->
                    // Gérer les erreurs lors de l'obtention de la localisation
                    Timber.tag(TAG).e("Erreur lors de l'obtention de la localisation: " + e.message)
                }
        }
    }


    override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        1001 -> {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // L'utilisateur a accordé l'autorisation de localisation, obtenez la localisation
                getCurrentLocationn()
            } else {
                // L'utilisateur a refusé l'autorisation de localisation, gérez cela en conséquence
                Toast.makeText(requireContext(), "L'autorisation de localisation a été refusée", Toast.LENGTH_SHORT).show()
                // Vous pouvez afficher un message à l'utilisateur ou prendre d'autres mesures en conséquence
            }
        }
    }
}



    @SuppressLint("RestrictedApi")
    override fun onMapReady(googleMap: GoogleMap) {

        // Assurez-vous que l'objet GoogleMap est initialisé correctement
        mGoogleMap = googleMap
        mGoogleMap.uiSettings.isZoomControlsEnabled = true
        mGoogleMap.setOnMarkerClickListener(this)
        setUpMap()
       // getCurrentLocationn()

        // Configurez la carte comme vous le souhaitez
//        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
//
//        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.uiSettings.isRotateGesturesEnabled = true
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
        markerOptions.title("$currentLatLng")
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
        startActivityForResult(galleryIntent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                Log.d("Saved on act ", "Path: ${uri.toString()}")
               // viewModel.setImageUri(uri.toString())
                //viewModel.setImage(uri.toString())
                saveProfileImageToDatabase(uri.toString())
            }
        }
    }
    private fun saveProfileImageToDatabase(imagePath: String) {
        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "") ?: ""
        val password = sharedPref.getString("password", "") ?: ""

      lifecycleScope.launch {
            val userId = userRepository.getUserId(username)
            Log.d("Saved pathhh", "Path: ${imagePath}")
            if (userId != null) {
                // L'utilisateur existe déjà, mettre à jour l'image de profil
                userRepository.updateUserProfileImage(userId, imagePath)
                val p = userRepository.getProfileImagePath(username)
                if (p != null) {
                    viewModel.setImageUri(p)
                }
            }
        }
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