package com.codepath.greghealy.parstagram

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.codepath.greghealy.parstagram.fragments.ComposeFragment
import com.codepath.greghealy.parstagram.fragments.PostsFragment
import com.codepath.greghealy.parstagram.fragments.ProfileFragment
import com.codepath.greghealy.parstagram.ui.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.parse.ParseUser


class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    private val bottomNav: BottomNavigationView by lazy {findViewById(R.id.bottomNavigationView)}
    private val fragmentManager: FragmentManager = supportFragmentManager
    private val composeFragment by lazy { ComposeFragment.newInstance() }
    private val postsFragment by lazy { PostsFragment.newInstance() }
    private val profileFragment by lazy { ProfileFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav.setOnNavigationItemSelectedListener {
            var fragment: Fragment = composeFragment
            when (it.itemId) {
                R.id.action_home ->  {
//                    Snackbar.make(findViewById(R.id.linearLayout), "Home pressed", Snackbar.LENGTH_LONG).show()
                    fragment = postsFragment
                }
                R.id.action_compose -> {
//                    Snackbar.make(findViewById(R.id.linearLayout), "Compose pressed", Snackbar.LENGTH_LONG).show()
                    fragment = composeFragment
                }
                R.id.action_profile -> {
//                    Snackbar.make(findViewById(R.id.linearLayout), "Profile pressed", Snackbar.LENGTH_LONG).show()
                    fragment = profileFragment
                }
            }
            fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit()
            true
        }

        bottomNav.selectedItemId = R.id.action_home
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "onOptionsSelected")
        when(item.itemId) {
            R.id.btnLogout -> {
                ParseUser.logOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return true
    }
}