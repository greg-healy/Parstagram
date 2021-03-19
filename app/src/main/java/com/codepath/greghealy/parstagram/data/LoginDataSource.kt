package com.codepath.greghealy.parstagram.data

import android.util.Log
import com.codepath.greghealy.parstagram.data.model.LoggedInUser
import com.parse.LogInCallback
import com.parse.ParseUser
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    val TAG = this.javaClass.simpleName

    fun login(username: String, password: String): Result {
        // TODO: handle loggedInUser authentication
        ParseUser.logInInBackground(username, password, LogInCallback { user, e ->
            if (e != null) {
                Log.e(TAG, "Issue with login", e)
//                return Result.Error(e)
            }
//            return Result.Success("You logged in!")
        })
        return Result.Success("Yay")
    }

    fun logout() {
        // TODO: revoke authentication
    }
}