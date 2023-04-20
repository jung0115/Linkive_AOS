package com.dwgu.linkive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dwgu.linkive.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ViewBinding Setting
    lateinit var binding: ActivityMainBinding

    // NavController 선언
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // NavController 설정
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        // Navigation Graph를 사용해서 Bottom Navigation 설정
        binding.navBottom.setupWithNavController(navController)

        // 아이콘에 색상 입히지 않고 아이콘 이미지 그대로 보여주기
        binding.navBottom.itemIconTintList = null

        // 최상위 화면을 제외하고는 BottomNavigation Bar 없애기
        setBottomNavigation()
    }

    private fun setBottomNavigation() {
        navController.addOnDestinationChangedListener{_, destination, _ ->
            if(destination.id == R.id.menu_home ||
                destination.id == R.id.menu_search ||
                destination.id == R.id.menu_folder ||
                destination.id == R.id.menu_mypage) {
                binding.navBottom.visibility = View.VISIBLE
            } else {
                binding.navBottom.visibility = View.GONE
            }
        }
    }

//    private fun transitionNavigationBottomView(bottomView: BottomNavigationView, fragmentManager: FragmentManager) {
//        bottomView.setOnItemSelectedListener {
//            it.isChecked = true
//            when(it.itemId) {
//                R.id.menu_home -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, HomeFragment())
//                        .commit()
//                    Log.d("msg", "menu_home work")
//                }
//                R.id.menu_search -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, SearchFragment())
//                        .commit()
//                    Log.d("msg", "menu_search work")
//                }
//                R.id.menu_folder -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, FolderFragment())
//                        .commit()
//                    Log.d("msg", "menu_folder work")
//                }
//                R.id.menu_mypage -> {
//                    supportFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, MyPageFragment())
//                        .commit()
//                    Log.d("msg", "menu_mypage work")
//                }
//                else -> Log.d("bottom navigation", "error") == 0
//            }
//            Log.d("bottom navigation", "final") == 0
//        }
//    }
//
//    private var doubleBackToExit = false
//    // 이전 버튼 - 폰에 있는 이전 버튼
//    override fun onBackPressed() {
//        //super.onBackPressed()
//
//        if (doubleBackToExit) {
//            finishAffinity()
//        } else {
//            // 현재 액티비티
//            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//            val info = manager.getRunningTasks(1)
//            val componentName = info[0].topActivity
//            val ActivityName = componentName!!.shortClassName.substring(1)
//
//            // 메인 액티비티인 경우
//            if(ActivityName == "MainActivity") {
//                var currentFragment: Fragment? = null
//                var cntFragment: Int = 0
//
//                // 현재 프래그먼트 찾기
//                for (fragment: Fragment in supportFragmentManager.fragments) {
//                    if (fragment.isVisible) {
//                        currentFragment = fragment
//                        cntFragment++
//                    }
//                }
//
//                // 현재 프래그먼트가 하단 메뉴를 눌렀을 때 나오는 첫 페이지가 아닌 경우
//                if (cntFragment > 1) {
//                    // 이전 페이지로 이동
//                    supportFragmentManager.beginTransaction().remove(currentFragment!!).commit()
//                    supportFragmentManager.popBackStack()
//                }
//                // 현재 프래그먼트가 하단 메뉴를 눌렀을 때 나오는 첫 페이지 중 하나인 경우
//                else {
//                    Toast.makeText(this, "종료하시려면 뒤로가기를 한번 더 눌러주세요.", Toast.LENGTH_SHORT).show()
//                    doubleBackToExit = true
//                    runDelayed(1500L) {
//                        doubleBackToExit = false
//                    }
//                }
//            }
//        }
//    }
//    fun runDelayed(millis: Long, function: () -> Unit) {
//        Handler(Looper.getMainLooper()).postDelayed(function, millis)
//    }



}