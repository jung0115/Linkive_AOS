package com.dwgu.linkive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentManager
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
        supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, HomeFragment()).commit()
        // 아이콘에 색상 입히지 않고 아이콘 이미지 그대로 보여주기
        binding.navBottom.itemIconTintList = null
        // BottomNavigation 함수 호출
        transitionNavigationBottomView(binding.navBottom, supportFragmentManager)
    }

    private fun transitionNavigationBottomView(bottomView: BottomNavigationView, fragmentManager: FragmentManager) {
        bottomView.setOnItemSelectedListener {
            it.isChecked = true
            when(it.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, HomeFragment())
                        .commit()
                    Log.d("msg", "menu_home work")
                }
                R.id.menu_search -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, SearchFragment())
                        .commit()
                    Log.d("msg", "menu_search work")
                }
                R.id.menu_folder -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, FolderFragment())
                        .commit()
                    Log.d("msg", "menu_folder work")
                }
                R.id.menu_mypage -> {
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, MyPageFragment())
                        .commit()
                    Log.d("msg", "menu_mypage work")
                }
                else -> Log.d("bottom navigation", "error") == 0
            }
            Log.d("bottom navigation", "final") == 0
        }
    }

}