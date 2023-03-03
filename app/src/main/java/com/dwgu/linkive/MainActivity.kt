package com.dwgu.linkive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dwgu.linkive.Folder.FolderFragment
import com.dwgu.linkive.Home.HomeFragment
import com.dwgu.linkive.MyPage.MyPageFragment
import com.dwgu.linkive.Search.SearchFragment
import com.dwgu.linkive.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    // ViewBinding Setting
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BottomNavigation Setting
        // 첫 화면에 보일 fragment 설정
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).commitAllowingStateLoss()
        // BottomNavigation 함수 호출
        BottomNavigation()
    }

    private fun BottomNavigation() {
        binding.navBottom.run {
            setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.menu_home -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, HomeFragment())
                            .commitAllowingStateLoss()
                        Log.d("msg", "menu_home work")
                    }
                    R.id.menu_search -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, SearchFragment())
                            .commitAllowingStateLoss()
                        Log.d("msg", "menu_search work")
                    }
                    R.id.menu_folder -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, FolderFragment())
                            .commitAllowingStateLoss()
                        Log.d("msg", "menu_folder work")
                    }
                    R.id.menu_mypage -> {
                        supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, MyPageFragment())
                            .commitAllowingStateLoss()
                        Log.d("msg", "menu_mypage work")
                    }
                }
                true
            }
            selectedItemId = R.id.nav_bottom
        }
    }

}