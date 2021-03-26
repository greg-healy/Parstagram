package com.codepath.greghealy.parstagram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PostsAdapter(val context: Context, val posts: ArrayList<Post>): RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
        private val ivImage = itemView.findViewById<ImageView>(R.id.ivImage)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)

        fun bind(post: Post) {
            tvUsername.text = post.user.username
            tvDescription.text = post.description
            Glide.with(context).load(post.image?.url).into(ivImage)
        }

    }

    fun clear() {
        posts.clear()
        notifyDataSetChanged()
    }

    fun addAll(postList: List<Post>) {
        posts.addAll(postList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post)
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}