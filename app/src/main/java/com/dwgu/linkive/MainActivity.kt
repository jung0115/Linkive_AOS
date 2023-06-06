package com.dwgu.linkive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dwgu.linkive.LinkView.LinkViewMenuListener.LinkViewMenuListener
import com.dwgu.linkive.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), LinkViewMenuListener {

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

    // 링크 메모 삭제 후 세부 페이지 닫기
    override fun backStackListener() {
        navController.popBackStack()
    }

    // 메인 화면(=하단바로 바로 들어가지는 페이지)들에서 이전 버튼 2번 누르면 앱 종료
    var waitTime = 0L
    override fun onBackPressed() {
        if(navController.currentDestination?.id == R.id.menu_home ||
            navController.currentDestination?.id == R.id.menu_search ||
            navController.currentDestination?.id == R.id.menu_folder ||
            navController.currentDestination?.id == R.id.menu_mypage) {
            if (System.currentTimeMillis() - waitTime >= 1500) {
                waitTime = System.currentTimeMillis()
                Toast.makeText(this, getString(R.string.toast_back_main_page), Toast.LENGTH_SHORT).show()
            } else {
                finish() // 액티비티 종료
            }
        }
        else{
            super.onBackPressed()
        }
    }
}