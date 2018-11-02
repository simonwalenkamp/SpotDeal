package com.walenkamp.spotdeal.DAL

import com.google.firebase.storage.FirebaseStorage
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import android.graphics.BitmapFactory
import android.util.Log


class StorageHelper {

    // FirebaseStorage reference
    var storageRef = FirebaseStorage.getInstance("gs://spotdeal-9c383.appspot.com/").reference

    // Gets the image for a specific deal
    fun getDealImage(callback: ICallbackDealImage, dealImageId: String) {
        val ONE_MEGABYTE: Long = 1024 * 1024
        storageRef.child(dealImageId).getBytes(ONE_MEGABYTE).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val img = task.result as ByteArray
                val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
                callback.onFinishDealImage(bitmap)
            }
        }

    }
}