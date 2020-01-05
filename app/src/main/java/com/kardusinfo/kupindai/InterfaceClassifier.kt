package com.kardusinfo.kupindai

import android.graphics.Bitmap

interface InterfaceClassifier {
    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 3F
    ){
        override fun toString(): String {
            return "${title}\nConfidence ${confidence}\n\n"
        }
    }

    fun recognizeImage(bitmap: Bitmap):List<Recognition>

    fun close()
}