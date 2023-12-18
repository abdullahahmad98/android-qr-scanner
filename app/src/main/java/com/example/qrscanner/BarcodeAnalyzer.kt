package com.example.qrscanner

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.Executor

class BarcodeAnalyzer(
    private val barcodeScanner: BarcodeScanner,
    private val executor: Executor,
    private val onBarcodesDetected: (List<Barcode>) -> Unit
) : ImageAnalysis.Analyzer {

    @OptIn(ExperimentalGetImage::class) override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val task = barcodeScanner.process(image)
            task.addOnCompleteListener(executor) { result ->
                processResult(result, imageProxy)
            }
            task.addOnFailureListener(executor) { e ->
                e.printStackTrace()
            }
        }
    }

    private fun processResult(result: Task<List<Barcode>>, imageProxy: ImageProxy) {
        if (result.isSuccessful) {
            val barcodes = result.result
            if (!barcodes.isNullOrEmpty()) {
                onBarcodesDetected(barcodes)
            }
        }
        imageProxy.close()
    }
}
