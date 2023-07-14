package com.teamtripdraw.android.ui.main

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.teamtripdraw.android.R
import com.teamtripdraw.android.databinding.ActivityMainBinding
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.HISTORY
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.HOME
import com.teamtripdraw.android.ui.main.MainActivity.BottomNavigationMenuType.MY_PAGE

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initBottomNavigation() {
        binding.bnMain.setOnItemSelectedListener {
            when (BottomNavigationMenuType.resourceIdToMenuType(it.itemId)) {
                HOME -> {
                    return@setOnItemSelectedListener true
                }
                HISTORY -> {
                    return@setOnItemSelectedListener true
                }
                MY_PAGE -> {
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
