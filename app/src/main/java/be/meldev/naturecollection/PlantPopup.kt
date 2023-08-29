package be.meldev.naturecollection

import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import be.meldev.naturecollection.adapter.PlantAdapter
import com.bumptech.glide.Glide

class PlantPopup(
    private val adapter: PlantAdapter,
    private val currentPlant: PlantModel
    ) : Dialog(adapter.context) {

    // Injection du layout de la popup
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.popup_plant_details)
        // Initialiser les composants avec les bonnes valeurs
        setupComponents()
        // Initialiser le bouton de fermeture
        setupCloseButton()
        // Initialiser le bouton de suppression
        setupDeleteButton()
        // Initialiser le bouton de favoris
        setupStarButton()
    }

    // Gestion de l'affichage de l'état actuel de l'étoile
    private fun updateStar(button: ImageView) {
        if (currentPlant.liked) {
            button.setImageResource(R.drawable.ic_star)
        }
        else {
            button.setImageResource(R.drawable.ic_unstar)
        }
    }

    private fun setupStarButton() {
        // Récupérer l'image
        val starButton = findViewById<ImageView>(R.id.star_button)

        // Appel de la fonction de gestion d'état
        updateStar(starButton)

        // Interaction
        starButton.setOnClickListener {
            // Donner la valeure inversée
            currentPlant.liked =!currentPlant.liked
            // Mise à jour des infos dans la bdd
            val repo = PlantRepository()
            repo.updatePlant(currentPlant)
            // Appel de la fonction de gestion d'état
            updateStar(starButton)
        }
    }

    private fun setupDeleteButton() {
        // Intégrer l'image et son action
        findViewById<ImageView>(R.id.delete_button).setOnClickListener {
            // Supprimer de la bdd
            val repo = PlantRepository()
            repo.deletePlant(currentPlant)
            // Fermeture de la popup après suppression
            dismiss()
        }
    }

    private fun setupCloseButton() {
        // Intégrer l'image et son action
        findViewById<ImageView>(R.id.close_button).setOnClickListener {
            // Fermer la fenêtre
            dismiss()
        }
    }

    private fun setupComponents() {
        // Actualiser l'image de la plante
        val plantImage = findViewById<ImageView>(R.id.image_item)
        Glide.with(adapter.context).load(Uri.parse(currentPlant.imageUrl)).into(plantImage)

        // Actualiser le nom de plante
        findViewById<TextView>(R.id.popup_plant_name).text = currentPlant.name

        // Acutaliser la description de la plante
        findViewById<TextView>(R.id.popup_plant_description_subtitle).text = currentPlant.description

        // Actualiser la croissance
        findViewById<TextView>(R.id.popup_plant_grow_subtitle).text = currentPlant.grow

        // Actualiser la consommation
        findViewById<TextView>(R.id.popup_plant_water_subtitle).text = currentPlant.water
    }
}