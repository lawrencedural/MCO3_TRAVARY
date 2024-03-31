// HomePageAdapter.kt
package com.mobdeve.s11.patawaran.yuan.dummy2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomePageAdapter(private var posts: List<PostDB>) : RecyclerView.Adapter<HomePageAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val captionTextView: TextView = itemView.findViewById(R.id.captionTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val screenshotImageView: ImageView = itemView.findViewById(R.id.screenshotImageView)
        val photo1ImageView: ImageView = itemView.findViewById(R.id.photo1ImageView)
        val photo2ImageView: ImageView = itemView.findViewById(R.id.photo2ImageView)
        val photo3ImageView: ImageView = itemView.findViewById(R.id.photo3ImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.captionTextView.text = post.caption
        holder.locationTextView.text = post.location
        // Load screenshot
        holder.screenshotImageView.setImageBitmap(post.screenshotBitmap)
        // Load photos if available
        if (post.photoBitmaps.isNotEmpty()) {
            holder.photo1ImageView.setImageBitmap(post.photoBitmaps.getOrNull(0))
            holder.photo2ImageView.setImageBitmap(post.photoBitmaps.getOrNull(1))
            holder.photo3ImageView.setImageBitmap(post.photoBitmaps.getOrNull(2))
        } else {
            // Clear image views if no photos are available
            holder.photo1ImageView.setImageBitmap(null)
            holder.photo2ImageView.setImageBitmap(null)
            holder.photo3ImageView.setImageBitmap(null)
        }
    }

    // Function to update posts in the adapter
    fun updatePosts(newPosts: List<PostDB>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}
