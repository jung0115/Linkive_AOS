package com.dwgu.linkive

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.loginService.LoginInterface
import com.dwgu.linkive.Login.loginService.PreferenceUtil
import com.dwgu.linkive.databinding.ActivityMainBinding
import retrofit2.Retrofit

// ApiClient의 instance 불러오기
private val retrofit: Retrofit = ApiClient.getInstance()
// Retrofit의 interface 구현
private val api: LoginInterface = retrofit.create(LoginInterface::class.java)
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

    // 하단 바의 페이지를 제외하고, 하단 바 숨기기
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



}