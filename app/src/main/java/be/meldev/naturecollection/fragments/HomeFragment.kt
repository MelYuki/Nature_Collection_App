package be.meldev.naturecollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import be.meldev.naturecollection.MainActivity
import be.meldev.naturecollection.PlantModel
import be.meldev.naturecollection.PlantRepository.Singleton.plantList
import be.meldev.naturecollection.R
import be.meldev.naturecollection.adapter.PlantAdapter
import be.meldev.naturecollection.adapter.PlantItemDecoration

class HomeFragment(
    private val context: MainActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Intégration de l'élément
        val view = inflater?.inflate(R.layout.fragment_home, container, false)

        // Créer une liste qui va contenir nos plantes
        // val plantList = arrayListOf<PlantModel>()

        // Enregistrer une première plante dans notre liste
        // plantlist.add(PlantModel(
        //      "Pissenlit",
        //      "jaune soleil",
        //      "Url d'une image trouvée sur pixabay.com",
        //      false))

        // Enregistrer une deuxième plante dans notre liste
        // ...

        // Récupérer le recyclerView horizontal
        val horizontalRecyclerView = view?.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView?.adapter = PlantAdapter(context, plantList.filter { !it.liked }, R.layout.item_horizontal_plant)

        // Récupérer le recyclerView vertical
        val verticalRecyclerView = view?.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView?.adapter = PlantAdapter(context, plantList, R.layout.item_vertical_plant)
        verticalRecyclerView?.addItemDecoration(PlantItemDecoration())

        return view
    }
}