package com.example.pinterest

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.imageview.ShapeableImageView
import androidx.recyclerview.widget.RecyclerView

class ImagesAdapter(private val imagesList: List<Int>) : RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder>() {

    class ImagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ShapeableImageView = itemView as ShapeableImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImagesViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ImagesViewHolder, position: Int) {
        val currentItem = imagesList[position]
        holder.imageView.setImageResource(currentItem)

        holder.imageView.setOnClickListener {
            if (position == 0) { // Verifica si es el primer ítem
                val context = holder.itemView.context
                val intent = Intent(context, foto2::class.java)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = imagesList.size
}
