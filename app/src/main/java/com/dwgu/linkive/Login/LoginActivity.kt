package com.dwgu.linkive.Login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.dwgu.linkive.Login.loginRepository.Companion.api
import com.dwgu.linkive.Login.loginRepository.Companion.id
import com.dwgu.linkive.Login.loginRepository.Companion.password
import com.dwgu.linkive.Login.loginService.login
import com.dwgu.linkive.Login.loginService.loginTokens
import com.dwgu.linkive.MainActivity
import com.dwgu.linkive.R
import com.dwgu.linkive.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    // ViewBinding
    private var mbinding: ActivityLoginBinding ?= null
    private val binding get() = mbinding!!

    // Kakao Login
    lateinit var kakaoCallback: (OAuthToken?, Throwable?) -> Unit

    // Google Login
    var auth : FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null
    var GOOGLE_LOGIN_CODE = 9001

    // Naver Login
    // 현재 에러나기 때문에 꼭 주석처리하고 실행할 것
//    val NAVER_CLIENT_ID = getString(R.string.NAVER_CLIENT_ID)
//    val NAVER_CLIENT_SECRET = getString(R.string.NAVER_CLIENT_SECRET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // viewBinding
        mbinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 이미 로그인된 경우
        if(GloabalApplication.prefs.getString("accessToken", "") != "" &&
            GloabalApplication.prefs.getString("refreshToken", "") != "") {
            Log.d("login - token", GloabalApplication.prefs.getString("accessToken", ""))
            Log.d("login-token", GloabalApplication.prefs.getString("refreshToken", ""))
            // 메인 화면으로 이동
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Google Login
//        auth = FirebaseAuth.getInstance()

//        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(R.string.default_web_client_id)
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 네이버 SDK 초기화
//        NaverIdLoginSDK.initialize(this, NAVER_CLIENT_ID, NAVER_CLIENT_SECRET,"LoginTest")
    }

    override fun onResume() {
        super.onResume()

        binding.btnErrorId.visibility = View.GONE
        binding.btnErrorPassword.visibility = View.GONE

        setOnClickListener()
    }

    private fun setOnClickListener() {

        // 비밀번호 보여주기, 숨기기 버튼
        binding.btnViewHide.setOnCheckedChangeListener {_, isChecked ->
            if(isChecked) {
                // 비밀번호를 보이게
                binding.inputPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.btnViewHide.setBackgroundResource(R.drawable.view)
            } else {
                // 비밀번호가 안보이게
                binding.inputPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.btnViewHide.setBackgroundResource(R.drawable.hidden)
            }
            // 커서가 항상 뒤에 위치하도록
            binding.inputPassword.setSelection(binding.inputPassword.length())
        }

        // 로그인 버튼 클릭 시
        binding.btnLogin.setOnClickListener {

            // 아이디, 비밀번호 값 가져오기
            id = binding.inputId.text.toString()
            password = binding.inputPassword.text.toString()

            // login api 호출
            postLogin(id, password)
        }

        // 아이디/비밀번호 찾기 버튼 클릭 시, 해당 화면으로 이동
        binding.btnFindId.setOnClickListener {
            Log.d("login", "move to find id")
            val intent = Intent(this@LoginActivity, FindIdActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnFindPassword.setOnClickListener {
            Log.d("login", "move to find password")
            val intent = Intent(this@LoginActivity, FindPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 회원 가입 버튼 클릭 시, 회원 가입 화면으로 이동
        binding.btnSignUp.setOnClickListener {
            Log.d("login", "move to sign up")
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 소셜 로그인
        // 카카오 로그인
        binding.btnKakaoLogin.setOnClickListener {
            Log.d("login", "kakao login")

            // hash 값 받아오기, kakao login 정보 확인
            checkKaKaoLogin()
            // 로그인 성공/실패 시, 콜백
            setKakaoCallback()

            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)){
                UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = kakaoCallback)
            }else{
                UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = kakaoCallback)
            }
        }

        // 네이버 로그인
        binding.btnNaverLogin.setOnClickListener {
            Log.d("login", "naver login")
            naverLogin()
        }

        // 구글 로그인
        binding.btnGoogleLogin.setOnClickListener {
            Log.d("login", "google login")
//            var signInIntent = googleSignInClient?.signInIntent
//            if (signInIntent != null) {
//                startActivityForResult(signInIntent, GOOGLE_LOGIN_CODE)
//                Log.d("msg", "sign in intent null?")
//            }
        }
    }

    fun checkKaKaoLogin() {
        // Kakao Login Hash 값 받아오기
        var keyHash = Utility.getKeyHash(this)
        Log.d("login - kakao keyHash", keyHash)

        // Kakao Login 정보 확인
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else if (tokenInfo != null) {
                Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    fun setKakaoCallback() {
        kakaoCallback = { token, error ->
            // 에러가 발생하는 경우
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            // 로그인에 성공한 경우
            else if (token != null) {
                Toast.makeText(this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()

                // 메인 페이지로 이동
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                finish()
            }
        }
    }

    // NaverLogin
    private fun naverLogin() {
        // oauthLoginCallback: OAuth 인증 요청이 종료됐음을 알려주는 콜백 인터페이스
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@LoginActivity, "errorCode: $errorCode, errorDesc: $errorDescription", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    // Google Login
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_LOGIN_CODE) {
            var result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            // result가 성공햇을 때, 이 값을 firebase에 넘겨주기
            if (result != null) {
                if(result.isSuccess) {
                    var account = result.signInAccount
                    // Second Step
                    if (account != null) {
                        Log.d("msg", "account not null")
                        firebaseAuthWithGoogle(account)
                    }
                    Log.d("msg", "account null")
                }
            }
            Log.d("msg", "result null")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        var credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener {
                task ->
                if(task.isSuccessful) {
                    // 로그인 성공 Toast
                    Log.d("msg", "google login work")
                    Toast.makeText(this@LoginActivity, "로그인에 성공했습니다.", Toast.LENGTH_SHORT).show()

                    // Login, 아이디와 패스워드가 맞았을 때
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // 아이디, 패스워드가 틀렸을 때
                    Toast.makeText(this, "아이디나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    // 로그인 api
    private fun postLogin(id: String, password: String) {
        var data = login(id, password)
        api.postLogin(data).enqueue(object:Callback<loginTokens>{
            override fun onFailure(call: Call<loginTokens>, t: Throwable) {}

            override fun onResponse(call: Call<loginTokens>, response: Response<loginTokens>) {

                Log.d("login - success", response.code().toString())
                var code  = response.code()

                if(code == 401) {
                    // 로그인에 실패하는 경우
                    Toast.makeText(this@LoginActivity, "아이디 및 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                    binding.btnErrorId.visibility = View.VISIBLE
                    binding.btnErrorPassword.visibility = View.VISIBLE
                } else {
                    // 로그인에 성공하는 경우
                    Log.d("login ", "success")
                    // 발급된 토큰 기기 내부에 저장
                    var accessToken = response.body()?.accessToken.toString()
                    var refreshToken = response.body()?.accessToken.toString()

                    GloabalApplication.prefs.setString("accessToken", accessToken)
                    GloabalApplication.prefs.setString("refreshToken", refreshToken)

                    Log.d("login - accessToken", accessToken)
                    Log.d("login-refreshToken", refreshToken)

                    // 에러 아이콘 숨기기
                    binding.btnErrorId.visibility = View.GONE
                    binding.btnErrorPassword.visibility = View.GONE

                    // 메인 페이지으로 이동
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

    override fun onDestroy() {
        mbinding = null
        super.onDestroy()
    }
}
