package com.codepath.greghealy.parstagram.fragments

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.codepath.greghealy.parstagram.Post
import com.codepath.greghealy.parstagram.R
import com.codepath.greghealy.parstagram.ui.login.LoginActivity
import com.parse.ParseFile
import com.parse.ParseQuery
import com.parse.ParseUser
import java.io.File

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [ComposeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ComposeFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42
    private lateinit var etDescription: EditText
    private lateinit var btnCaptureImage: Button
    private lateinit var ivPostImage: ImageView
    private lateinit var btnSubmit: Button
    private lateinit var photoFile: File
    var photoFileName = "photo.jpg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etDescription = view.findViewById(R.id.etDescription)
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage)
        ivPostImage = view.findViewById(R.id.ivPostImage)
        btnSubmit = view.findViewById(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val description = etDescription.text.toString()
            if (description.isEmpty()) {
                Toast.makeText(this.requireContext(), "Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (photoFile == null || ivPostImage.drawable == null) {
                Toast.makeText(this.requireContext(), "There is no image!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val currentUser = ParseUser.getCurrentUser()
            savePost(description, currentUser, photoFile)
        }

        btnCaptureImage.setOnClickListener {
            launchCamera()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ComposeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ComposeFragment().apply {
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this.requireContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun savePost(description: String, currentUser: ParseUser, photoFile: File) {
        val post = Post()
        post.description = description
        post.image = ParseFile(photoFile)
        post.user = currentUser
        post.saveInBackground {
            if (it != null) {
                Log.e(TAG, "Error while saving", it)
                Toast.makeText(this.requireContext(), "Error while saving", Toast.LENGTH_SHORT).show()
            }
            Log.i(TAG, "Post save was successful")
            etDescription.setText("")
            ivPostImage.setImageResource(0)
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
                this.requireContext(),
                "com.codepath.fileprovider",
                photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.requireContext().packageManager) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
        }
    }

    private fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir = File(this.requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
}