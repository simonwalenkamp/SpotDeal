package com.walenkamp.spotdeal.DAL

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.storage.UploadTask
import com.walenkamp.spotdeal.Interface.ICallbackDone
import com.walenkamp.spotdeal.Interface.ICallbackFinished
import java.io.ByteArrayOutputStream


class StorageHelper {

    // FirebaseStorage reference
    var storageRef = FirebaseStorage.getInstance("gs://spotdeal-9c383.appspot.com/").reference

    // Gets the image for a specific deal
    fun getDealImage(callback: ICallbackDealImage, id: String) {
        storageRef.child(id).getBytes(Long.MAX_VALUE).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val img = task.result as ByteArray
                val bitmap = BitmapFactory.decodeByteArray(img, 0, img.size)
                callback.onFinishDealImage(bitmap)
            }
        }
    }

    // Saves image in storage
    fun saveImage(img: Bitmap, id: String, callback: ICallbackFinished) {
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val uploadTask: UploadTask = storageRef.child(id).putBytes(data)
        uploadTask.addOnCompleteListener {task ->
            if(task.isSuccessful) {
                callback.onFinishFinished(true)
            } else {
                callback.onFinishFinished(false)
            }
        }
    }

    // Deletes an image in storage by its id
    fun deleteImage(id: String) {
        storageRef.child(id).delete()
    }

    // Replaces an image in storage
    fun editImage(img: Bitmap, id: String, callback: ICallbackFinished) {
        storageRef.child(id).delete().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                saveImage(img, id, object : ICallbackFinished{
                    override fun onFinishFinished(finished: Boolean) {
                        callback.onFinishFinished(true)
                    }
                })
            }
        }
    }
}