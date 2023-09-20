package com.teamtripdraw.android.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityMainBinding
import com.teamtripdraw.android.ui.allPosts.AllPostsFragment
import com.teamtripdraw.android.ui.allTrips.AllTripsFragment
import com.teamtripdraw.android.ui.home.HomeFragment
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.HOME
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.MY_PAGE
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.POSTS
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.TRIPS
import com.teamtripdraw.android.ui.myPage.MyPageFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initDefaultFragment()
        initBottomNavigation()
    }

    private fun initDefaultFragment() {
        supportFragmentManager.beginTransaction().replace<HomeFragment>(R.id.fcv_main).commit()
    }

    private fun initBottomNavigation() {
        binding.bnMain.setOnItemSelectedListener {
            when (BottomNavigationMenuType.resourceIdToMenuType(it.itemId)) {
                HOME -> {
                    supportFragmentManager.beginTransaction()
                        .replace<HomeFragment>(R.id.fcv_main).commit()
                    return@setOnItemSelectedListener true
                }
                TRIPS -> {
                    supportFragmentManager.beginTransaction()
                        .replace<AllTripsFragment>(R.id.fcv_main).commit()
                    return@setOnItemSelectedListener true
                }
                POSTS -> {
                    supportFragmentManager.beginTransaction()
                        .replace<AllPostsFragment>(R.id.fcv_main).commit()
                    return@setOnItemSelectedListener true
                }
                MY_PAGE -> {
                    supportFragmentManager.beginTransaction()
                        .replace<MyPageFragment>(R.id.fcv_main).commit()
                    return@setOnItemSelectedListener true
                }
            }
        }
    }

    private enum class BottomNavigationMenuType(@IdRes private val resourceId: Int) {
        HOME(R.id.menu_bn_home),
        TRIPS(R.id.menu_bn_trips),
        POSTS(R.id.menu_bn_posts),
        MY_PAGE(R.id.menu_bn_my_page),
        ;

        companion object {
            fun resourceIdToMenuType(@IdRes resourceId: Int): BottomNavigationMenuType =
                values().find { it.resourceId == resourceId }
                    ?: throw IllegalStateException(WRONG_BOTTOM_NAVIGATION_RESOURCE_ID_ERROR)

            private const val WRONG_BOTTOM_NAVIGATION_RESOURCE_ID_ERROR =
                "잘못된 바텀 네비게이션 resourceId 입니다."
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
