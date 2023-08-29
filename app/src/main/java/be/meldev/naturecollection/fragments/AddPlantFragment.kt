package be.meldev.naturecollection.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import be.meldev.naturecollection.MainActivity
import be.meldev.naturecollection.PlantModel
import be.meldev.naturecollection.PlantRepository
import be.meldev.naturecollection.PlantRepository.Singleton.downloadUri
import be.meldev.naturecollection.R
import java.util.*

class AddPlantFragment(
    private val context: MainActivity
) : Fragment() {

    // Attributs accessibles par toutes les fonctions de la classe
    private var file: Uri? = null
    private var uploadedImage: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Intégration de l'élément
        val view = inflater?.inflate(R.layout.fragment_add_plant, container, false)

        // Récupérer uploadedImage pour lui associer son composant
        uploadedImage = view?.findViewById(R.id.preview_image)

        // Récupérer le bouton pour charger une image
        val pickupImageButton = view?.findViewById<Button>(R.id.upload_button)

        // Lorqu'on click dessus, ouvre les images du téléphone
        pickupImageButton?.setOnClickListener { pickupImage() }

        // Récupérer le bouton confirmer
        val confirmButton = view?.findViewById<Button>(R.id.confirm_button)
        confirmButton?.setOnClickListener { sendForm(view) }

        // Renvoie de la view pour qu'elle puisse être assignée
        return view
    }

    // Méthode d'envoi des formulaires
    private fun sendForm(view: View?) {
        // Heberger dans le bucket
        val repo = PlantRepository()
        repo.uploadImage(file!!){

            // Récupération des informations
            val plantName = view?.findViewById<EditText>(R.id.name_input)?.text.toString()
            val plantDescription = view?.findViewById<EditText>(R.id.description_input)?.text.toString()
            val grow = view?.findViewById<Spinner>(R.id.grow_spinner)?.selectedItem.toString()
            val water = view?.findViewById<Spinner>(R.id.water_spinner)?.selectedItem.toString()
            // Récupérer le lien de l'image
            val downloadImageUrl = downloadUri

            // Créer un nouvel objet PlantModel
            val plant = PlantModel(
                UUID.randomUUID().toString(),
                plantName,
                plantDescription,
                downloadImageUrl.toString(),
                grow,
                water
            )

            // Envoyer en bdd
            repo.insertPlant(plant)
        }

    }

    private fun pickupImage() {
        // Intent() permet d'ouvrir des choses dans le téléphone
        val intent = Intent()
        intent.type = "image/"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 47)
    }

    // Fonction qui suit l'évènement "startActivity..."
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Vérifier les données reçues
        if(requestCode == 47 && resultCode == Activity.RESULT_OK) {

            // Vérifier si les données sont nulles
            if(data == null || data.data == null) return

            // Récupérer l'image
            // val selectedImage = data.data
            file = data.data

            // Mettre à jour l'aperçu de l'image
            // uploadedImage?.setImageURI(selectedImage)
            uploadedImage?.setImageURI(file)

            // Heberger dans le bucket
            // val repo = PlantRepository()
            // repo.uploadImage(selectedImage!!)
        }
    }
}