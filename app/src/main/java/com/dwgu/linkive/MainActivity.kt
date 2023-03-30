package com.dwgu.linkive

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.dwgu.linkive.Folder.FolderFragment
import com.dwgu.linkive.Home.HomeFragment
import com.dwgu.linkive.Login.LoginFragment
import com.dwgu.linkive.Login.SignUpFragment
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

    private var doubleBackToExit = false
    // 이전 버튼 - 폰에 있는 이전 버튼
    override fun onBackPressed() {
        //super.onBackPressed()

        if (doubleBackToExit) {
            finishAffinity()
        } else {
            // 현재 액티비티
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val info = manager.getRunningTasks(1)
            val componentName = info[0].topActivity
            val ActivityName = componentName!!.shortClassName.substring(1)

            // 메인 액티비티인 경우
            if(ActivityName == "MainActivity") {
                var currentFragment: Fragment? = null
                var cntFragment: Int = 0

                // 현재 프래그먼트 찾기
                for (fragment: Fragment in supportFragmentManager.fragments) {
                    if (fragment.isVisible) {
                        currentFragment = fragment
                        cntFragment++
                    }
                }

                // 현재 프래그먼트가 하단 메뉴를 눌렀을 때 나오는 첫 페이지가 아닌 경우
                if (cntFragment > 1) {
                    // 이전 페이지로 이동
                    supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
                    supportFragmentManager.popBackStack()
                }
                // 현재 프래그먼트가 하단 메뉴를 눌렀을 때 나오는 첫 페이지 중 하나인 경우
                else {
                    Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
                    doubleBackToExit = true
                    runDelayed(1500L) {
                        doubleBackToExit = false
                    }
                }
            }
            // 링크 편집 페이지인 경우
            /*else if(ActivityName == "EditLinkActivity") {
                // 변경 내용 저장 안 된다는 안내 BottomSheet
            }*/
        }
    }
    fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }

}