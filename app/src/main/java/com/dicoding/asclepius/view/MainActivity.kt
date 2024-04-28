package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.yalantis.ucrop.UCrop
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cancerInfo.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: run {
                showToast(getString(R.string.result))
            }
        }
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        croppedImageUri = null
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            performCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun performCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File(cacheDir, "UCropImage"))
        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .start(this@MainActivity) // Menggunakan this@MainActivity sebagai context
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(data!!)
            if (resultUri != null) {
                croppedImageUri = resultUri
                showCroppedImage()
            } else {
                showToast("Failed to crop image")
            }
        }
        // Periksa apakah pengguna kembali dari aktivitas ResultActivity
        if (requestCode == RESULT_ANALYZE && resultCode == RESULT_OK) {
            // Perbarui gambar preview jika sudah ada gambar yang dipilih
            currentImageUri?.let {
                showCroppedImage()
            }
        }
    }

    private fun showCroppedImage() {
        currentImageUri?.let {
            Log.d("Cropped Image URI", "showCroppedImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage(uri: Uri) {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        val intent = Intent(this, ResultActivity::class.java)
        croppedImageUri?.let {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, it.toString())
        } ?: run {
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        }
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val RESULT_ANALYZE = 101
    }
}
