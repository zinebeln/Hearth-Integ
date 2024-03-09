package com.example.myapplication.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.domain.repository.UserRepository
import com.example.myapplication.model.ViewModel.DecksViewModel
import com.example.myapplication.model.ViewModel.SharedViewModel
import kotlinx.coroutines.launch

import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class CardDetailsFragment : Fragment()  {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val decksViewModel: DecksViewModel by activityViewModels()

    private lateinit var textViewCardName: TextView
    private lateinit var textViewCardType: TextView
    private lateinit var imageViewCard: ImageView
    private lateinit var btnFav : Button
    private lateinit var auth: AuthManager
    private lateinit var userRepo : UserRepository

    private val imageNamess =
        listOf("imagun", "imagedeux", "imagetrois", "imagecinq", "imagesix")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("CardDetail", "onCreateView called")
        val view = inflater.inflate(R.layout.fragment_card_details, container, false)

        textViewCardName = view.findViewById(R.id.textViewCardNameDetails)
        textViewCardType = view.findViewById(R.id.textViewCardTypeDetails)
        imageViewCard = view.findViewById(R.id.imageViewCardDetails)
        btnFav = view.findViewById(R.id.btnAddToFavorites)
        userRepo = UserRepository()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        auth = AuthManager(requireContext())



        sharedViewModel.selectedCard?.observe(viewLifecycleOwner, Observer { selectedCard ->
            Log.d("CardDetailsFragment", "Selected Card: $selectedCard")

                selectedCard?.let {
                    textViewCardName.text = it.name
                    textViewCardType.text = it.type

                    if (it.img != null) {
                        Glide.with(this)
                            .load(it.img)
                            .into(imageViewCard)
                    } else {
                        val randomImageName = imageNamess.random()
                        val ressourceId = this.resources.getIdentifier(
                            randomImageName,
                            "drawable",
                           this.context?.packageName
                        )
                        Glide.with(this)
                            .load(ressourceId)
                            .apply(RequestOptions().centerCrop())
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(imageViewCard)
                    }

                    val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)
                    val username = sharedPref.getString("username", "")?: ""
                    lifecycleScope.launch {
                        val userId = userRepo.getUserId(username)
                        btnFav.setOnClickListener {
                            lifecycleScope.launch {
                                Log.d("on click favorite", "click $selectedCard")
                                Toast.makeText(requireContext(), "Carte ajoutée aux favoris ", Toast.LENGTH_SHORT).show()
                                decksViewModel.toggleFavoriteStatus3(selectedCard, userId)
                            }
                        }
                    }



                    val textToEncode = it.dbfId.toString()
                    val qrCodeBitmap = generateQRCode(textToEncode)

                    val qrCodeImageView = view.findViewById<ImageView>(R.id.qrCodeImageView)
                    qrCodeImageView.setImageBitmap(qrCodeBitmap)

                }





        })
    }

    fun generateQRCode(text: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

}