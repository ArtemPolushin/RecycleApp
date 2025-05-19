package com.hse.recycleapp.ui.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.core.graphics.scale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hse.recycleapp.ml.TrashnetModel
import com.hse.recycleapp.data.WasteType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import javax.inject.Inject

@HiltViewModel
class WasteClassifierViewModel @Inject constructor(
) : ViewModel() {

    private var model: TrashnetModel? = null

    private val _classificationResult = MutableStateFlow("")
    val classificationResult: StateFlow<String> = _classificationResult

    fun classifyImage(bitmap: Bitmap, context: Context) {
        viewModelScope.launch(Dispatchers.Default) {
            if (model == null) {
                model = TrashnetModel.newInstance(context)
            }

            val imageSize = 224
            val scaledBitmap = bitmap.scale(imageSize, imageSize, false)
            val inputBuffer = preprocessImage(scaledBitmap)

            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
            inputFeature0.loadBuffer(inputBuffer)

            val outputs = model!!.process(inputFeature0)
            val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
            val confidences = outputFeature0.floatArray

            val outputClasses = WasteType.entries.map { it.displayName }
            var maxConfidence = 0f
            var maxPos = 0
            val sb = StringBuilder()
            for (i in confidences.indices) {
                sb.append("${outputClasses[i]}: ${"%.2f".format(confidences[i])}\n")
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i]
                    maxPos = i
                }
            }

            _classificationResult.value = "Определено как: ${outputClasses[maxPos]}\n\nПодробности:\n$sb"
        }
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val imageSize = 224
        val resizedBitmap = bitmap.scale(imageSize, imageSize, false)
        val inputBuffer = ByteBuffer.allocateDirect(imageSize * imageSize * 3 * 4).apply {
            order(ByteOrder.nativeOrder())
            rewind()
        }

        val pixels = IntArray(imageSize * imageSize)
        resizedBitmap.getPixels(pixels, 0, imageSize, 0, 0, imageSize, imageSize)

        for (pixel in pixels) {
            val r = (Color.red(pixel) / 255.0f)
            val g = (Color.green(pixel) / 255.0f)
            val b = (Color.blue(pixel) / 255.0f)

            inputBuffer.putFloat(r)
            inputBuffer.putFloat(g)
            inputBuffer.putFloat(b)
        }

        return inputBuffer
    }

    override fun onCleared() {
        super.onCleared()
        model?.close()
    }
}