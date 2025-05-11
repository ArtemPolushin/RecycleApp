package com.hse.recycleapp.ui.ml

import android.graphics.Bitmap
import com.hse.recycleapp.ml.RecycleDetectionModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

object TensorFLowHelper {

    @Composable
    fun ClassifyImage(bitmap: Bitmap, callback : (@Composable (waste : String) -> Unit)) {
        val model = RecycleDetectionModel.newInstance(LocalContext.current)
        val imageSize = bitmap.width
        assert(imageSize == bitmap.height)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, imageSize, imageSize, 3), DataType.FLOAT32)
        val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3).order(ByteOrder.nativeOrder())
        val intValues = IntArray(imageSize * imageSize)
        bitmap.getPixels(intValues, 0, imageSize, 0, 0, imageSize, imageSize)
        var pixel = 0

        for (i in 0 until imageSize) {
            for (j in 0 until imageSize) {
                val `val` = intValues[pixel++] // RGB
                byteBuffer.putFloat((`val` shr 16 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` shr 8 and 0xFF) * (1f / 1))
                byteBuffer.putFloat((`val` and 0xFF) * (1f / 1))
            }
        }

        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
        val confidences = outputFeature0.floatArray

        var maxPos = 0
        var maxConfidence = 0f
        for (i in confidences.indices) {
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }
        val outputClasses = listOf(
            "battery", "biological", "brown-glass", "cardboard", "clothes", "green-glass",
            "metal", "paper", "plastic", "shoes", "trash", "white-glass"
        )
        val outputClasses2 = listOf(
            "plastic", "glass", "metal", "electronics", "paper", "non-recyclable"
        )

        callback.invoke(outputClasses2[maxPos])


        // Releases model resources if no longer used.
        model.close()

    }

}