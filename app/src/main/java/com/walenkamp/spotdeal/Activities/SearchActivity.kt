package com.walenkamp.spotdeal.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.walenkamp.spotdeal.Adapters.CustomerAdapter
import com.walenkamp.spotdeal.BLL.CustomerLogic
import com.walenkamp.spotdeal.BLL.SupplierLogic
import com.walenkamp.spotdeal.Entities.Customer
import com.walenkamp.spotdeal.Entities.Supplier
import com.walenkamp.spotdeal.Interface.ICallBackSupplier
import com.walenkamp.spotdeal.Interface.ICallbackCustomers
import com.walenkamp.spotdeal.Fragments.LogoutDialogFragment
import com.walenkamp.spotdeal.R
import kotlinx.android.synthetic.main.activity_search.*



class SearchActivity : AppCompatActivity() {

    // Drawer menu instance
    private lateinit var mDrawerLayout: DrawerLayout

    // Supplier instance
    private lateinit var supplier: Supplier

    // SupplierLogic instance
    private val supplierLogic: SupplierLogic = SupplierLogic()

    // Customer list
    private lateinit var allCustomers: List<Customer>

    // CustomerLogic instance
    private val customerLogic: CustomerLogic = CustomerLogic()

    // CustomerAdapter instance
    private val adapter = CustomerAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setNavigationAndToolbar()

        // Gets the supplier from SupplierLogic and calls the getCustomers function
        supplierLogic.getSupplier(object : ICallBackSupplier{
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
                getCustomers(supplier.id)
            }
        })
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filterCustomer(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                filterCustomer(query)
                return true
            }
        })
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

    // Sets the navigation drawer and toolbar up
    private fun setNavigationAndToolbar(){
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

    // Creates a new list of customers matching the result and sets that list to the adapter
    private fun filterCustomer(text: String?) {
        if(text == "") {
            adapter.setCustomers(allCustomers)
            return
        }
        val newCustomerList = mutableListOf<Customer>()
        for (cus in allCustomers) {
            if (cus.name.contains("$text", true) ||
                cus.address.contains("$text", true) ||
                cus.email.contains("$text", true) ||
                cus.phone.toString().contains("$text", true) &&
                    !newCustomerList.contains(cus))
            {
                newCustomerList.add(cus)
            }

        }
        adapter.setCustomers(newCustomerList)
    }

    // Gets all customers and sets them to the adapter
    private fun getCustomers(supplierId: String) {
        customerLogic.getCustomers(object : ICallbackCustomers {
            override fun onFinishCustomers(customers: List<Customer>?) {
                allCustomers = customers!!
                rec_customer.adapter = adapter
                rec_customer.layoutManager = LinearLayoutManager(baseContext)
                adapter.setCustomers(customers)
                customer_progress.visibility = View.INVISIBLE
            }
        }, supplierId)
    }
}
