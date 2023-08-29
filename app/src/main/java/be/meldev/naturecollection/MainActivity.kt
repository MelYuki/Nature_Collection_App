package be.meldev.naturecollection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import be.meldev.naturecollection.fragments.AddPlantFragment
import be.meldev.naturecollection.fragments.CollectionFragment
import be.meldev.naturecollection.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Méthode de chargement du fragment principal
        loadFragment(HomeFragment(this), R.string.home_page_title)

        // Importer la bottomNavigationView
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home_page -> {
                    loadFragment(HomeFragment(this), R.string.home_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.collection_page -> {
                    loadFragment(CollectionFragment(this), R.string.collection_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.add_page -> {
                    loadFragment(AddPlantFragment(this), R.string.add_plant_page_title)
                    return@setOnNavigationItemSelectedListener true
                }
                // Cas par défaut
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, string: Int) {
        // Charger notre PlantRepository
        val repo = PlantRepository()

        // Actualiser le titre de la page
        findViewById<TextView>(R.id.page_title).text = resources.getString(string)

        // Mettre à jour la liste de plantes
        repo.updateData{
            // Injecter le fragement dans notre boite (fragment_container)
            val transaction = supportFragmentManager.beginTransaction()
            // transaction.replace(R.id.fragment_container, HomeFragment(this))
            // transaction.replace(R.id.fragment_container, CollectionFragment(this))
            // transaction.replace(R.id.fragment_container, AddPlantFragment(this))
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}