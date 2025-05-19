package com.hse.recycleapp.ui.ml

import android.graphics.Bitmap
import android.graphics.Color
import com.hse.recycleapp.ml.TrashnetModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder
import androidx.core.graphics.scale
import com.hse.recycleapp.data.WasteType

object TensorFLowHelper {

    private const val IMAGE_SIZE = 224
    fun classifyImage(bitmap: Bitmap, context: android.content.Context): String {
        val model = TrashnetModel.newInstance(context)
        val byteBuffer = preprocessImage(bitmap)
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, IMAGE_SIZE, IMAGE_SIZE, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0: TensorBuffer = outputs.getOutputFeature0AsTensorBuffer()
        val confidences = outputFeature0.floatArray

        val outputClasses = WasteType.entries.map { it -> it.displayName }.toList()
        var maxPos = 0
        var maxConfidence = 0f
//        var st: String = ""
        for (i in confidences.indices) {
//            st += outputClasses[i] + "  " + confidences[i] + "\n"
            if (confidences[i] > maxConfidence) {
                maxConfidence = confidences[i]
                maxPos = i
            }
        }

        model.close()
//        return st
        return outputClasses[maxPos]
    }


    fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val resizedBitmap = bitmap.scale(IMAGE_SIZE, IMAGE_SIZE, false)
        val inputBuffer = ByteBuffer.allocateDirect(IMAGE_SIZE * IMAGE_SIZE * 3 * 4).apply {
            order(ByteOrder.nativeOrder())
            rewind()
        }

        val pixels = IntArray(IMAGE_SIZE * IMAGE_SIZE)
        resizedBitmap.getPixels(pixels, 0, IMAGE_SIZE, 0, 0, IMAGE_SIZE, IMAGE_SIZE)

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
}