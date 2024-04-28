package com.dicoding.asclepius.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Menampilkan hasil gambar, prediksi, dan confidence score.
        val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI))
        imageUri?.let { uri ->
            Log.d("Image URI", "showImage: $uri")
            binding.resultImage.setImageURI(uri)

            val imageClassifierHelper = ImageClassifierHelper(
                context = this,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        Toast.makeText(this@ResultActivity, error, Toast.LENGTH_SHORT).show()
                    }

                    override fun onResults(results: List<Classifications>?) {
                        results?.let {
                            if (it.isNotEmpty()) {
                                val topResult = it[0]
                                val label = topResult.categories[0].label
                                val confidence = topResult.categories[0].score

                                val formattedConfidence =
                                    NumberFormat.getPercentInstance().format(confidence.toDouble())

                                val resultText =
                                    "Prediction: $label\nConfidence: $formattedConfidence"
                                binding.resultText.text = resultText
                            } else {
                                Toast.makeText(
                                    this@ResultActivity,
                                    "No results",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            )
            imageClassifierHelper.classifyStaticImage(uri)
        }
    }

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }
}
