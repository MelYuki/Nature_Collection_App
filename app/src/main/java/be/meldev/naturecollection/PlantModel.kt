package be.meldev.naturecollection

// Permet de donner et de lister toutes les caractéristique d'une plante
class PlantModel (
    val id: String = "plant0",
    val name: String = "Tulipe",
    val description: String = "Petite description",
    val imageUrl: String = "http://graven.yt/plante.jpg",
    val grow: String = "Lente",
    val water: String = "Moyenne",
    var liked: Boolean = false
)