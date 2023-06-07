package com.dwgu.linkive.ImageUploadApi

import android.content.ContentValues.TAG
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Login.GloabalApplication
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

// server에 이미지 업로드 api

private const val IMAGE_BASE_URL = "http://linkive.site/"

// api 통신을 위한 retrofit
private val retrofit: Retrofit = ApiClient.getInstance()

// 현재 로그인 된 사용자 token
private var authorization: String? = null
private var refreshToken: String? = null

// token 정보 세팅
fun setTokenForImage() {
    authorization = "JWT " + GloabalApplication.prefs.getString("accessToken", "")
    refreshToken = GloabalApplication.prefs.getString("refreshToken", "")

    //Log.d("Test token", authorization + ", " + refreshToken)
}

// 이미지 업로드 api
fun apiImageUpload(
    position: Int,
    img : MultipartBody.Part,
    responseImageUrl: (image: SetImage) -> Unit // 이미지 url 반환
    ) {
    // 토큰 정보 세팅
    setTokenForImage()

    retrofit.create(ImageUploadService::class.java)
        .uploadImage(authorization = authorization!!, refreshToken = refreshToken!!, img = img)
        .enqueue(object : Callback<ImageUploadResponse> {
            override fun onResponse(call: Call<ImageUploadResponse>, response: Response<ImageUploadResponse>) {
                Log.d(TAG, "이미지 업로드 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                // 이미지
                var body: ImageUploadResponse = response.body()!!
                var imageUrl = IMAGE_BASE_URL + body.file_info.path
                responseImageUrl(SetImage(position, imageUrl))
            }

            override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                Log.d(TAG, "이미지 업로드 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 이미지를 form-data 형식으로 바꿔줌
fun uploadImage(path: Uri?, context : Context) : MultipartBody.Part? {
    Log.d("Test Uri", "-----------------------------------------")
    Log.d("Test Uri", path.toString())
    // 이미지의 절대경로를 가져옴
    // 그 절대 경로를 통해 File() 함수로 이미지 파일을 얻고, file 변수에 저장
    val file = File(absolutelyPath(path, context))

    //Log.d("Test Image", "file -----------------------------------------")
    //Log.d("Test Image", file.toString())

    // Request 형식으로 바꿈
    //val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)

    //Log.d("Test Image", "requestFile -----------------------------------------")
    //Log.d("Test Image", requestFile.toString())

    // form-data 형식으로 바꿈
    //val body = MultipartBody.Part.createFormData("img", file.name, requestFile)

    //Log.d("Test Image", "-----------------------------------------")
    //Log.d("Test Image", body.toString())

    return null
}

// 갤러리에서 가져온 이미지의 uri를 넣고, 이미지의 절대경로를 가져옴
fun absolutelyPath(path: Uri?, context : Context): String {
    var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
    Log.d("Test Image", "proj -----------------------------------------")
    Log.d("Test Image", proj.toString())
    var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
    Log.d("Test Image", "Cursor -----------------------------------------")
    Log.d("Test Image", c.toString())
    var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
    c?.moveToFirst()
    Log.d("Test Image", "index -----------------------------------------")
    Log.d("Test Image", index.toString())
    var result = c?.getString(index!!)
    Log.d("Test Image", "result -----------------------------------------")
    Log.d("Test Image", result.toString())

    return "test"
}
