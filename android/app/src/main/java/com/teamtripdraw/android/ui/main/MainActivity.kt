package com.teamtripdraw.android.ui.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.replace
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityMainBinding
import com.teamtripdraw.android.ui.history.HistoryFragment
import com.teamtripdraw.android.ui.home.HomeFragment
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.HISTORY
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.HOME
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.MY_PAGE
import com.teamtripdraw.android.ui.myPage.MyPageFragment

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
                HISTORY -> {
                    supportFragmentManager.beginTransaction()
                        .replace<HistoryFragment>(R.id.fcv_main).commit()
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
        HISTORY(R.id.menu_bn_history),
        MY_PAGE(R.id.menu_bn_my_page);

        companion object {
            fun resourceIdToMenuType(@IdRes resourceId: Int): BottomNavigationMenuType =
                values().find { it.resourceId == resourceId }
                    ?: throw IllegalStateException(WRONG_BOTTOM_NAVIGATION_RESOURCE_ID_ERROR)

            private const val WRONG_BOTTOM_NAVIGATION_RESOURCE_ID_ERROR =
                "잘못된 바텀 네비게이션 resourceId 입니다."
        }
    }
}
