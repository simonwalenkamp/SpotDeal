package com.walenkamp.spotdeal.Activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.walenkamp.spotdeal.BLL.DealLogic
import com.walenkamp.spotdeal.Entities.Deal
import com.walenkamp.spotdeal.Interface.ICallbackDealImage
import com.walenkamp.spotdeal.Interface.ICallbackFinished
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_edit_deal.*
import java.lang.Exception

class EditDealActivity : AppCompatActivity() {

    // Tag used for logging
    private val TAG = "EDIT_DEAL_ACTIVITY"

    // Deal instance
    private lateinit var deal: Deal

    // DealLogic instance
    private val dealLogic: DealLogic = DealLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_deal)

        deal = intent.getSerializableExtra(DEAL) as Deal
        setUpDeal()
        edit_deal_image.setOnClickListener{
            openCameraRoll()
        }
        edit_deal_btn.setOnClickListener {
            editDeal()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == PICKFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            edit_deal_image.setImageURI(data?.data)
        }
    }

    // Sets the deal data to layout fields
    private fun setUpDeal() {
        edit_deal_name.setText(deal.name)
        edit_deal_description.setText(deal.description)
        edit_deal_info.setText(deal.info)
        edit_deal_price.setText(deal.price.toString())

        dealLogic.getDealImage(object : ICallbackDealImage{
            override fun onFinishDealImage(dealImage: Bitmap?) {
                edit_deal_image.setImageBitmap(dealImage)
            }
        }, deal.id)
    }

    // Opens device camera roll
    private fun openCameraRoll() {
        intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICKFILE_REQUEST_CODE)
    }

    // Edits the deal
    private fun editDeal() {
        if(!edit_deal_name.text.isEmpty() &&
            !edit_deal_description.text.isEmpty() &&
            !edit_deal_info.text.isEmpty() &&
            !edit_deal_price.text.isEmpty()) {
            try {
                hideLayout()
                val bitmap: Bitmap = (edit_deal_image.drawable as BitmapDrawable).bitmap

                deal.name = edit_deal_name.text.toString()
                deal.description = edit_deal_description.text.toString()
                deal.price = edit_deal_price.text.toString().toFloat()
                deal.info = edit_deal_info.text.toString()

                dealLogic.editDeal(deal, bitmap, object : ICallbackFinished{
                    override fun onFinishFinished(finished: Boolean) {
                        if(finished) {
                            finish()
                        } else {
                            showLayout()
                            Snackbar.make(edit_deal_constraint, R.string.could_not_edit_has_active, Snackbar.LENGTH_LONG).show()
                        }
                    }
                })
            } catch (e: Exception){
                Log.d(TAG, e.message)
                Snackbar.make(edit_deal_constraint, R.string.could_not_edit, Snackbar.LENGTH_LONG).show()
            }
        } else {
            Snackbar.make(edit_deal_constraint, R.string.fill_in_fields, Snackbar.LENGTH_LONG).show()
        }
    }


    // Hides layout to show progress
    private fun hideLayout() {
        edit_deal_name.visibility = View.INVISIBLE
        edit_deal_description.visibility = View.INVISIBLE
        edit_deal_grid.visibility = View.INVISIBLE
        edit_deal_image.visibility = View.INVISIBLE

        progress_edit_deal.visibility = View.VISIBLE
    }

    // Shows the layout to stop progress
    private fun showLayout() {
        edit_deal_name.visibility = View.VISIBLE
        edit_deal_description.visibility = View.VISIBLE
        edit_deal_grid.visibility = View.VISIBLE
        edit_deal_image.visibility = View.VISIBLE

        progress_edit_deal.visibility = View.INVISIBLE
    }
}
