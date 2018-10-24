package com.walenkamp.spotdeal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.walenkamp.spotdeal.BLL.SupplierLogic
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier

class SearchActivity : AppCompatActivity() {

    // Drawer menu instance
    private lateinit var mDrawerLayout: DrawerLayout

    // Supplier instance
    private lateinit var supplier: Supplier

    // SupplierLogic instance
    private var supplierLogic: SupplierLogic = SupplierLogic()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Gets the supplier from SupplierLogic
        supplierLogic.getSupplier(object : ICallBackSupplier{
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
            }
        })


        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val supplierNameTV: TextView = headerView.findViewById(R.id.header_tv)

        // Gets the supplier from SupplierLogic
        supplierLogic.getSupplier(object : ICallBackSupplier{
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
                supplierNameTV.text = supplier.name
            }
        })

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // set item as selected to persist highlight
            menuItem.isChecked = true
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()
            when {
                menuItem.toString() == "Log out" -> logout()
                menuItem.toString() == "Scan code" -> startScan()
            }
           true
        }

        this.setSupportActionBar(findViewById(R.id.toolbar))
        val actionbar: ActionBar? = this.supportActionBar

        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
    }

    // Handles opening the drawer menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Shows the LogoutDialogFragment
    private fun logout() {
        val dialog = LogoutDialogFragment.newInstance()
        dialog.show(supportFragmentManager, "LogoutDialog")
    }

    // Start the ScanActivity
    private fun startScan() {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }
}
