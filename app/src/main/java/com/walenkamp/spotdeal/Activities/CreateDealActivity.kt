package com.walenkamp.spotdeal.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackFinished
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_create_deal.*
import java.lang.Exception

// Pick file request code
val PICKFILE_REQUEST_CODE = 1234

class CreateDealActivity : AppCompatActivity() {

    private val TAG = "CREATE_DEAL_ACTIVITY"

    // Pick file request code
    private val PICKFILE_REQUEST_CODE = 1234

    // Supplier id
    private lateinit var supplierId: String

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_deal)

        supplierId = intent.getStringExtra(SUPPLIER_ID)
        create_deal_image.setOnClickListener{
            openCameraRoll()
        }
        create_deal_btn.setOnClickListener {
            createDeal()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            create_deal_image.setImageURI(data?.data)
        }
    }

    // Opens device camera roll
    private fun openCameraRoll() {
        intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICKFILE_REQUEST_CODE)
    }

    // Creates a deal
    private fun createDeal(){
        if(!create_deal_name.text.isEmpty() &&
            !create_deal_description.text.isEmpty() &&
            !create_deal_info.text.isEmpty() &&
            !create_deal_price.text.isEmpty()) {
            try {
                val deal = Deal("",
                    supplierId,
                    create_deal_name.text.toString(),
                    create_deal_price.text.toString().toFloat(),
                    create_deal_description.text.toString(),
                    create_deal_info.text.toString())

                val bitmap: Bitmap = (create_deal_image.drawable as BitmapDrawable).bitmap
                updateLayout()
                dealLogic.createDeal(deal, bitmap, object : ICallbackFinished {
                    override fun onFinishFinished(finished: Boolean) {
                        finish()
                    }
                })
            } catch (e: Exception) {
                Snackbar.make(create_deal_constraint, "Please select an image", Snackbar.LENGTH_LONG).show()
                Log.d(TAG, e.message)
            }
        } else {
            Snackbar.make(create_deal_constraint, "Please fill in all fields", Snackbar.LENGTH_LONG).show()
        }
    }

    // Updates the layout to display loading
    private fun updateLayout(){
        create_deal_name.visibility = View.INVISIBLE
        create_deal_description.visibility = View.INVISIBLE
        create_deal_grid.visibility = View.INVISIBLE
        create_deal_btn.hide()
        create_deal_image.visibility = View.INVISIBLE

        progress_create_deal.visibility = View.VISIBLE
    }
}
