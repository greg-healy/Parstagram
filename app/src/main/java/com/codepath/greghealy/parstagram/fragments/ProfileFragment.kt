package com.codepath.greghealy.parstagram.fragments

import android.util.Log
import com.codepath.greghealy.parstagram.Post
import com.parse.ParseQuery
import com.parse.ParseUser

class ProfileFragment: PostsFragment() {
    private val TAG = this.javaClass.simpleName

    override fun queryPosts() {
        val query = ParseQuery.getQuery(Post::class.java)
        query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())

        query.include(Post.KEY_USER)
        query.limit = 20
        query.addDescendingOrder(Post.KEY_CREATED_AT)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e)
                return@findInBackground
            }
            postsAdapter.clear()
            postsAdapter.addAll(posts)
            swipeRefreshLayout.isRefreshing = false
        }
    }
}