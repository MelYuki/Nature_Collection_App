package be.meldev.naturecollection.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import be.meldev.naturecollection.MainActivity
import be.meldev.naturecollection.PlantRepository.Singleton.plantList
import be.meldev.naturecollection.R
import be.meldev.naturecollection.adapter.PlantAdapter
import be.meldev.naturecollection.adapter.PlantItemDecoration

class CollectionFragment(
    private val context: MainActivity
)   : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Intégration de l'élément
        val view = inflater?.inflate(R.layout.fragment_collection, container, false)

        // Récupérer la recycler view
        val collectionRecyclerView = view?.findViewById<RecyclerView>(R.id.collection_recycler_list)
        collectionRecyclerView?.adapter = PlantAdapter(context, plantList.filter { it.liked }, R.layout.item_vertical_plant)
        collectionRecyclerView?.layoutManager = LinearLayoutManager(context)
        // Remettre les espacements entres les éléments
        collectionRecyclerView?.addItemDecoration(PlantItemDecoration())

        // Renvoie de la view pour qu'elle puisse être assignée
        return view
    }
}