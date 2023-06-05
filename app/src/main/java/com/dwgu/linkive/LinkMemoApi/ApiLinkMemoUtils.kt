package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import android.content.ContentValues.TAG
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Home.CreateLinkToUrl.CreateLinkToUrlDialog
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkMemoApi.TestLogin
import com.dwgu.linkive.LinkMemoApi.TestLoginData
import com.dwgu.linkive.LinkMemoApi.TestSignUp
import com.dwgu.linkive.LinkMemoApi.TestUserData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoService
import com.dwgu.linkive.LinkMemoApi.token
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


// api 통신을 위한 retrofit
private val retrofit: Retrofit = ApiClient.getInstance()

// 현재 로그인 된 사용자 token
private var authorization: String? = null
private var refreshToken: String? = null


// 링크 url 내용 조회한 걸로 링크 메모 생성
fun apiCreateLinkMemo(linkMemo: CreateLinkMemoData, refreshHomeListener: CreateLinkToUrlDialog.RefreshHomeListener) {


    retrofit.create(CreateLinkMemoService::class.java)
        .addLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, linkMemo)
        .enqueue(object : Callback<CreateLinkMemoResponse> {
            override fun onResponse(call: Call<CreateLinkMemoResponse>, response: Response<CreateLinkMemoResponse>) {
                Log.d(TAG, "링크 메모 추가 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                // 메인 페이지 링크 리스트 Refresh
                refreshHomeListener.refreshHomeListener()
            }

            override fun onFailure(call: Call<CreateLinkMemoResponse>, t: Throwable) {
                Log.d(TAG, "링크 메모 추가 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 폴더 전체 조회
fun apiGetAllFolders(setFolders: (folders: MutableList<FolderList>?) -> Unit) {


    retrofit.create(GetAllFolderService::class.java)
        .getAllFolders(authorization = authorization!!, refreshToken = refreshToken!!)
        .enqueue(object : Callback<GetAllFolderData> {
            override fun onResponse(call: Call<GetAllFolderData>, response: Response<GetAllFolderData>) {
                Log.d(TAG, "폴더 전체 조회 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                // 조회한 폴더 데이터 보내기
                setFolders(response.body()!!.folderList)
            }

            override fun onFailure(call: Call<GetAllFolderData>, t: Throwable) {
                Log.d(TAG, "폴더 전체 조회 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 링크 메모 전체 조회 -> 메인 페이지 링크 리스트에 추가
fun apiViewLinkMemo(addLinkList: (linkListItem: LinkListItem) -> Unit) {


    retrofit.create(ViewLinkMemoService::class.java)
        .viewLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!)
        .enqueue(object : Callback<ViewLinkMemoData> {
            override fun onResponse(call: Call<ViewLinkMemoData>, response: Response<ViewLinkMemoData>) {
                Log.d(TAG, "링크 메모 조회 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                // 조회한 링크 리스트를 화면에 보여주기 위해 데이터 추가
                val linkLists: MutableList<ViewLinkMemo> = response.body()!!.arr
                for(linkItem in linkLists) {
                    var linkItemSource: String? = getSourceForLink(linkItem.link)

                    // 썸네일 이미지
                    var thumbnailUrl: String? = null
                    if(linkItem.content != null)
                        thumbnailUrl = getThumbnailUrl(linkItem.content.arr)

                    addLinkList(
                        LinkListItem(
                            linkTitle = linkItem.title,
                            folderName = linkItem.folder_name,
                            thumbnailImage = thumbnailUrl,
                            linkItemSource = linkItemSource,
                            linkItemForms = null,
                            created_date = linkItem.date_created))
                }
            }

            override fun onFailure(call: Call<ViewLinkMemoData>, t: Throwable) {
                Log.d(TAG, "링크 메모 조회 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 썸네일 이미지 url 가져오기
fun getThumbnailUrl(arr: MutableList<String>?): String? {
    if(arr != null) {
        for(item in arr) {
            // 아이템 내용을 json 형태로 변환
            var json: JSONObject? = null
            json = JSONObject(item)
            // 이미지 아이템일 경우
            if(json.getString("type") == "image") {
                return json.getString("value")
            }
        }
    }

    return null
}

// 링크 플랫폼 정보
fun getSourceForLink(linkUrl: String): String? {
    // 출처 플랫폼
    var linkItemSource: String? = null
    // 일단 url에 해당 문자를 포함하면 해당 플랫폼 페이지인 것으로 판단 -> 그러나 아닌 경우도 있음
    // 인스타그램은 OpenGraph를 지원하지 않음
    if (linkUrl.contains("instagram")) {
        linkItemSource = "instagram"
    } else if (linkUrl.contains("twitter")) {
        linkItemSource = "twitter"
    } else if (linkUrl.contains("naver") && linkUrl.contains("blog")) {
        linkItemSource = "naver_blog"
    } else if (linkUrl.contains("google") && linkUrl.contains("play")) {
        linkItemSource = "play_store"
    }

    return linkItemSource
}

// 링크 아이템 종류

fun testSignUp() {
    val user = TestUserData("jung0987", "test1234", "test1234@naver.com", "testjm")
    retrofit.create(TestSignUp::class.java)
        .testSignup(user)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "테스트용 계정 가입 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "테스트용 계정 가입 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

fun testLogin() {
    val user = TestLoginData("jung0987", "test1234")
    retrofit.create(TestLogin::class.java)
        .testLogin(user)
        .enqueue(object : Callback<token> {
            override fun onResponse(call: Call<token>, response: Response<token>) {
                Log.d(TAG, "테스트용 계정 로그인 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                authorization = "JWT " + response.body()!!.accessToken
                refreshToken = response.body()!!.refreshToken
            }

            override fun onFailure(call: Call<token>, t: Throwable) {
                Log.d(TAG, "테스트용 계정 로그인 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}