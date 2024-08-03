package com.example.pinterest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LikesAdapter : RecyclerView.Adapter<LikesAdapter.LikeViewHolder>() {

    private var likesCount: Int = 0

    class LikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val likeText: TextView = itemView.findViewById(R.id.likeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_like, parent, false)
        return LikeViewHolder(view)
    }

    override fun onBindViewHolder(holder: LikeViewHolder, position: Int) {
        // Muestra el contador de likes
        holder.likeText.text = likesCount.toString()
    }

    override fun getItemCount(): Int {
        return 1 // Solo necesitamos un elemento para el contador de likes
    }

    fun addLike() {
        likesCount++
        notifyItemChanged(0) // Notifica el cambio al primer (y único) elemento
    }
}
