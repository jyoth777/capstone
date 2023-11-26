package com.xperiencelabs.armenu.activity


import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.xperiencelabs.armenu.R
import com.xperiencelabs.armenu.fragment.Dashboard
import com.xperiencelabs.armenu.fragment.Favourite


class Recommendation : AppCompatActivity() {
        lateinit var drawerLayout: androidx.drawerlayout.widget.DrawerLayout
        lateinit var coordinatorLayout: androidx.coordinatorlayout.widget.CoordinatorLayout
        lateinit var toolbar: androidx.appcompat.widget.Toolbar
        lateinit var frameLayout: FrameLayout
        lateinit var navigationView: com.google.android.material.navigation.NavigationView
        var previousMenuItem: MenuItem? = null
        lateinit var dash: MenuItem


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            setContentView(R.layout.activity_main)


            drawerLayout = findViewById(R.id.drawerLayout)
            coordinatorLayout = findViewById(R.id.coordinatorLayout)
            toolbar = findViewById(R.id.toolbar)
            frameLayout = findViewById(R.id.frame)
            navigationView = findViewById(R.id.navigationView)

            setUpToolbar()
            openDashboard()

            val actionBarDrawerToggle = ActionBarDrawerToggle(
                this@Recommendation,
                drawerLayout,
                R.string.open_drawer,
                R.string.close_drawer
            )
            //action strings tell the two states the toggle will give to the navigation drawer
            drawerLayout.addDrawerListener(actionBarDrawerToggle)
            //state of toggle is sync with state of navigation bar


            actionBarDrawerToggle.syncState()
            navigationView.setNavigationItemSelectedListener {

                if (previousMenuItem != null) {
                    previousMenuItem?.isChecked = false

                }
                it.isCheckable = true
                it.isChecked = true

                previousMenuItem = it
                when (it.itemId) { //it gives the currently selected item

                    R.id.favourites -> {
                        supportActionBar?.title = "Favourites"
                        supportFragmentManager.beginTransaction().replace(R.id.frame, Favourite())
                            .commit()
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    R.id.about -> {
                        supportActionBar?.title = "About"
                        Toast.makeText(this@Recommendation, "about", Toast.LENGTH_SHORT).show()
                    }
                    R.id.dashboard -> {
                        supportActionBar?.title = "Dashboard"
                        supportActionBar
                        supportFragmentManager.beginTransaction().replace(
                            R.id.frame,
                            Dashboard()
                        )//dashboard fragment is replacing the blank frame

                            .commit()//commit applies the transaction
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                return@setNavigationItemSelectedListener true
            }

        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            val id = item.itemId
            if (id == android.R.id.home) {//id.home because actiontoggle is placed at home
                drawerLayout.openDrawer(GravityCompat.START)
            }

            return super.onOptionsItemSelected(item)
        }

        fun setUpToolbar() {
            //to make the toolbar as the action bar
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Toolbar Title"
            //supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)


        }

//        override fun onBackPressed() {
//            val frag = supportFragmentManager.findFragmentById(R.id.frame)
//            when (frag) {
//                !is Dashboard -> {
//                    openDashboard()
//                }
//                else -> {
//                    super.onBackPressed()
//                }
//            }
//
//
//        }

        fun openDashboard() {
            supportFragmentManager.beginTransaction().replace(R.id.frame, Dashboard())
                .addToBackStack("Dashboard").commit()
            supportActionBar?.title = "Dashboard"
            navigationView.setCheckedItem(R.id.dashboard)

//        dash.isCheckable=true
//        dash.isChecked=true
//        previousMenuItem=dash


        }


    }

