package be.meldev.naturecollection.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import be.meldev.naturecollection.*
import com.bumptech.glide.Glide

class PlantAdapter (
    val context: MainActivity,
    private val plantList: List<PlantModel>,
    private val layoutId: Int
    // Définition de la classe
    ) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    // Boite pour ranger tous les composants à controller
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        // Image de la plante
        val plantImage: ImageView = view.findViewById(R.id.image_item)
        // Nom de la plante
        // val plantName= view.findViewById<TextView>(R.id.name_item)
        val plantName: TextView? = view.findViewById(R.id.name_item)
        // Description de la plante
        // val plantDescription= view.findViewById<TextView>(R.id.description_item)
        val plantDescription:TextView? = view.findViewById(R.id.description_item)
        // Icone de la plante
        val starIcon: ImageView? = view.findViewById(R.id.star_icon)
    }

    // Permet d'injecter le composant
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)

    }

    // Permet de renvoyer un nombre d'items dynamiquement
    // override fun getItemCount(): Int = 5
    override fun getItemCount(): Int = plantList.size


    // Permet de mettre à jour chaque modeles
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Récupérer les infos de la plante
        val currentPlant = plantList[position]

        // Récupérer le repository
        val repo = PlantRepository()

        // Utiliser glide pour récupérer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // Mettre à jour le nom de la plante
        // holder.plantName.text = currentPlant.name
        holder.plantName?.text = currentPlant.name

        // Mettre à jour la description de la plante
        // holder.plantDescription.text = currentPlant.description
        holder.plantDescription?.text = currentPlant.description

        // Vérifier si la plante a été likée ou non
        if(currentPlant.liked) {
            holder.starIcon?.setImageResource(R.drawable.ic_star)
        }
        else {
            holder.starIcon?.setImageResource(R.drawable.ic_unstar)
        }

        // Rajouter une interaction sur l'étoile
        holder.starIcon?.setOnClickListener {
            // Inverse si le bouton est like ou pas
            currentPlant.liked = !currentPlant.liked
            // Mettre à jour l'objet plante
            repo.updatePlant(currentPlant)
        }

        // Interaction lors du click sur une plante
        holder.itemView.setOnClickListener {
            // Afficher la popup
            PlantPopup(this,currentPlant).show()
        }
    }
}