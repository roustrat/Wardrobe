package com.str.wardrobe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.str.wardrobe.simpleMVVM.views.base.BaseFragment
import com.str.wardrobe.simpleMVVM.views.categorydresses.DressesCategoryFragment


class MainActivity : AppCompatActivity() {

    // Initialise the DrawerLayout, NavigationView and ToggleBar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

    private val activityViewModel by viewModels<MainViewModel> {
        ViewModelProvider.AndroidViewModelFactory(
            application
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            // define the initial screen that should be launched when app starts.
            activityViewModel.launchFragment(
                activity = this,
                screen = DressesCategoryFragment.Screen(),
                addToBackStack = false
            )
        }

        // Call findViewById on the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_container)
        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)
        // Call findViewById on the Toolbar
        toolbar = findViewById(R.id.toolbar)

        // Set a Toolbar
        setSupportActionBar(toolbar)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Pass the ActionBarToggle action into the drawerListener
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_open)
        drawerLayout.addDrawerListener(toggle)


        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
//        actionBarToggle.isDrawerIndicatorEnabled = true
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

//        // This will display an Up icon (<-), we will replace it with hamburger later
////        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeButtonEnabled(true)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_new_category -> {
                    Toast.makeText(this, "New category", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_list_of_categories -> {
                    Toast.makeText(this, "List of categories", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_settings -> {
                    Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_about -> {
                    Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    false
                }
            }
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)

    }

//    @SuppressLint("ResourceType")
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.id.home, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.home -> {
//                drawerLayout.openDrawer(GravityCompat.START)
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(navView)
        onBackPressed()
        return true
    }

    // override the onBackPressed() function to close the Drawer when the back button is clicked
    override fun onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        // execute navigation actions only when activity is active
        activityViewModel.whenActivityActive.resource = this
    }

    override fun onPause() {
        super.onPause()
        // postpone navigation actions if activity is not active
        activityViewModel.whenActivityActive.resource = null
    }

    fun notifyScreenUpdates() {
        val f = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (supportFragmentManager.backStackEntryCount > 0) {
            // more than 1 screen -> show back button in the toolbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

//        if (f is HasScreenTitle && f.getScreenTitle() != null) {
//            // fragment has custom screen title -> display it
//            supportActionBar?.title = f.getScreenTitle()
//        } else {
//            supportActionBar?.title = getString(R.string.app_name)
//        }

        val result = activityViewModel.result.value?.getValue() ?: return
        if (f is BaseFragment) {
            // has result that can be delivered to the screen's view-model
            f.viewModel.onResult(result)
        }
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            notifyScreenUpdates()
        }
    }

}