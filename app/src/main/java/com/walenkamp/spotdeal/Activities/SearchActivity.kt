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
import com.walenkamp.spotdeal.Separator
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.drawer_header.*
import android.view.WindowManager
import android.os.Build
import android.view.Menu


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

    // Separator instance
    private lateinit var separator: Separator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setNavigationAndToolbar()

        separator = Separator(this)
        rec_customer.addItemDecoration(separator)


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

    // Start the OrderHistoryActivity
    private fun startHistory() {
        val intent = Intent(this, OrderHistoryActivity::class.java).putExtra(SUPPLIER_ID, supplier.id)
        startActivity(intent)
    }

    // Sets the navigation drawer and toolbar up
    private fun setNavigationAndToolbar(){
        mDrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        // Gets the supplier from SupplierLogic
        supplierLogic.getSupplier(object : ICallBackSupplier{
            override fun onFinishSupplier(sup: Supplier?) {
                supplier = sup!!
                header_name_tv.text = supplier.name
                header_email_tv.text = supplier.email
                header_phone_tv.text = supplier.phone.toString()
                header_address_tv.text = supplier.address
            }
        })

        navigationView.setNavigationItemSelectedListener { menuItem ->
            // close drawer when item is tapped
            mDrawerLayout.closeDrawers()
            when {
                menuItem.toString() == getString(R.string.logout) -> logout()
                menuItem.toString() == getString(R.string.scan) -> startScan()
                menuItem.toString() == getString(R.string.history) -> startHistory()
            }
            true
        }
        this.setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
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
