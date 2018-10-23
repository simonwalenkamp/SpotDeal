package com.walenkamp.spotdeal

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout


class SearchFragment : Fragment() {

    // Drawer menu instance
     private lateinit var mDrawerLayout: DrawerLayout


    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        mDrawerLayout = view.findViewById(R.id.drawer_layout)

        (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
        val actionbar: ActionBar? = (activity as AppCompatActivity).supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        mDrawerLayout.addDrawerListener(
            object : DrawerLayout.DrawerListener {
                override fun onDrawerStateChanged(newState: Int) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerClosed(drawerView: View) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDrawerOpened(drawerView: View) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
            }
        )
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.drawer_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                mDrawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
