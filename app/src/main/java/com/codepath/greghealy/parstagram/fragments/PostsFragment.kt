package com.codepath.greghealy.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.greghealy.parstagram.Post
import com.codepath.greghealy.parstagram.PostsAdapter
import com.codepath.greghealy.parstagram.R
import com.parse.ParseQuery

/**
 * A simple [Fragment] subclass.
 * Use the [PostsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class PostsFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    protected var allPosts: ArrayList<Post> = ArrayList()
    protected val postsAdapter by lazy { PostsAdapter(requireContext(), allPosts) }
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPosts = view.findViewById<RecyclerView>(R.id.rvPosts)
        rvPosts.adapter = postsAdapter
        rvPosts.layoutManager = LinearLayoutManager(requireContext())
        queryPosts()

        swipeRefreshLayout = view.findViewById(R.id.swipeContainer)
        swipeRefreshLayout.setOnRefreshListener {
            queryPosts()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PostsFragment.
         */
        @JvmStatic
        fun newInstance() = PostsFragment().apply { }
    }

    protected open fun queryPosts() {
        val query = ParseQuery.getQuery(Post::class.java)

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