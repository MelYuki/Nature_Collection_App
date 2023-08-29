package be.meldev.naturecollection.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class PlantItemDecoration : RecyclerView.ItemDecoration() {

    // Permet de rajouter un offset pour opérer une séparation autour de l'élement
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = 20
    }
}