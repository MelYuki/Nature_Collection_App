package be.meldev.naturecollection

import android.net.Uri
import be.meldev.naturecollection.PlantRepository.Singleton.databaseRef
import be.meldev.naturecollection.PlantRepository.Singleton.downloadUri
import be.meldev.naturecollection.PlantRepository.Singleton.plantList
import be.meldev.naturecollection.PlantRepository.Singleton.storageReference
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.Continuation
import kotlin.coroutines.coroutineContext

class PlantRepository {

    // Singleton permet d'accéder aux valeurs de la DB sans les rafraichir
    object Singleton {
        // Donner le lien pour accéder au bucket
        private val BUCKET_URL: String = "gs://nature-collection-816d4.appspot.com"

        // Se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)

        // Se connecter à la référence "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // Créer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()

        // Contenir le lien de l'image courante
        var downloadUri: Uri? = null
    }

    // Mettre à jour les données
    fun updateData(callback: () -> Unit) {
        // Absorber les données depuis la databaseRef -> liste de plantes
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // Retirer les anciennes
                plantList.clear()

                // Récolter la liste
                for (ds in snapshot.children) {
                    // Construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // Vérifier que la plante n'est pas null
                    if(plant != null) {
                        // Ajouter la plante à notre liste
                        plantList.add(plant)
                    }
                }

                // Actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // Créer une méthode pour envoyer des fichiers sur le storage
    fun uploadImage(file: Uri, callback: () -> Unit) {
        // Vérifier que ce fichier n'est pas null
        if(file != null) {
            // Créer le nom du fichier
            val fileName = UUID.randomUUID().toString() + ".jpg"
            // Endroit de la bdd ou le ranger
            val ref = storageReference.child(fileName)
            // Associer le contenu à soumettre
            val uploadTask = ref.putFile(file)

            // Démarrer la tâche d'envoi
            uploadTask.continueWithTask(
                com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // S'il y a eu un problème lors de l'envoi du fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl

            // Récupérer le lien de l'image qui est maintenant hébergé
            }).addOnCompleteListener { task ->
                // Vérifier si tout a bien fonctionné
                if(task.isSuccessful) {
                    // Récupérer l'image
                    // val downloadURI = task.result
                    downloadUri = task.result
                    // Récupérer l'image dans sendForm de AddPlantFragment
                    callback()
                }
            }
        }
    }

    // Mettre à jour un objet plante en bdd
    fun updatePlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // Insérer une nouvelle plante en bdd
    fun insertPlant(plant: PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // Supprimer une plante de la bdd
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()
}