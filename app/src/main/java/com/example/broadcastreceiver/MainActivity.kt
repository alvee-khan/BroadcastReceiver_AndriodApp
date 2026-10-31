package com.example.broadcastreceiver

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<MaterialToolbar>(
            R.id.mainToolbar
        )

        setSupportActionBar(toolbar)
        supportActionBar?.title = "App"

        InsetsHelper.applySystemBars(
            findViewById(R.id.mainContent)
        )

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { item ->

            openSection(item.itemId)

            navigationView.setCheckedItem(item.itemId)

            true
        }

        if (savedInstanceState == null) {

            openSection(R.id.navBroadcast)

            navigationView.setCheckedItem(R.id.navBroadcast)
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {

                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                        drawerLayout.closeDrawer(GravityCompat.START)

                    } else {

                        isEnabled = false
                        onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

    private fun openSection(itemId: Int) {

        val fragment: Fragment = when (itemId) {

            R.id.navBroadcast -> BroadcastFragment()

            R.id.navImageScale -> ImageScaleFragment()

            R.id.navVideo -> VideoFragment()

            R.id.navAudio -> AudioFragment()

            else -> BroadcastFragment()
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()

        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
