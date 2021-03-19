package com.codepath.greghealy.parstagram

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.codepath.greghealy.parstagram.ui.login.LoginActivity
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File


class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName

    private val etDescription: EditText by lazy { findViewById(R.id.etDescription) }
    private val btnCaptureImage: Button by lazy { findViewById(R.id.btnCaptureImage) }
    private val ivPostImage: ImageView by lazy { findViewById(R.id.ivPostImage) }
    private val btnSubmit: Button by lazy { findViewById(R.id.btnSubmit) }
    private val btnLogout: Button by lazy { findViewById(R.id.btnLogout)}
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42
    private lateinit var photoFile: File
    var photoFileName = "photo.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        queryPosts()
        btnSubmit.setOnClickListener {
            val description = etDescription.text.toString()
            if (description.isEmpty()) {
                Toast.makeText(this, "Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (photoFile == null || ivPostImage.drawable == null) {
                Toast.makeText(this, "There is no image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentUser = ParseUser.getCurrentUser()
            savePost(description, currentUser, photoFile)
        }

        btnCaptureImage.setOnClickListener {
            launchCamera()
        }

       btnLogout.setOnClickListener {
           ParseUser.logOut()
           val intent = Intent(this, LoginActivity::class.java)
           startActivity(intent)
           finish()
       }
    }

    private fun launchCamera() {
        // create Intent to take a picture and return control to the calling application
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        val fileProvider: Uri = FileProvider.getUriForFile(
            this,
            "com.codepath.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    private fun savePost(description: String, currentUser: ParseUser, photoFile: File) {
        val post = Post()
        post.description = description
        post.image = ParseFile(photoFile)
        post.user = currentUser
        post.saveInBackground {
            if (it != null) {
                Log.e(TAG, "Error while saving", it)
                Toast.makeText(this, "Error while saving", Toast.LENGTH_SHORT).show()
            }
            Log.i(TAG, "Post save was successful")
            etDescription.setText("")
            ivPostImage.setImageResource(0)
        }
    }

    private fun queryPosts() {
        val query = ParseQuery.getQuery<Post>(Post::class.java)

        query.include(Post.KEY_USER)
        query.findInBackground { posts, e ->
            if (e != null) {
                Log.e(TAG, "Issue with getting posts", e)
                return@findInBackground
            }
            for (post in posts) {
                Log.i(TAG, "Post: ${post.description}, username: ${post.user.username}")
            }
        }

    }
}