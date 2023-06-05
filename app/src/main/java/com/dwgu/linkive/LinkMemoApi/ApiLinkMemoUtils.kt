package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import android.content.ContentValues.TAG
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.Home.CreateLinkToUrl.CreateLinkToUrlDialog
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.DetailLinkMemoService
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.LinkMemoBaseInfo
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoService
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewCheckboxItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewCodeItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewImageItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewLinkItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewPlaceItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewTextItem
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

// 폴더 전체 조회 - 링크 추가 시
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
                            memoNum = linkItem.memo_num,
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

// 사용된 링크 아이템 종류 아이콘

// 메모 번호로 링크 내용 조회
fun apiDetailLinkMemo(
    memoNum: Int,
    setLinkViewInfo: (baseInfo: LinkMemoBaseInfo) -> Unit,
    addLinkViewItem: (detailItem: LinkViewItem) -> Unit,
    ) {

    retrofit.create(DetailLinkMemoService::class.java)
        .detailLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, memosNum = memoNum)
        .enqueue(object : Callback<ViewLinkMemo> {
            override fun onResponse(call: Call<ViewLinkMemo>, response: Response<ViewLinkMemo>) {
                Log.d(TAG, "메모 번호로 링크 내용 조회  -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                var linkMemo: ViewLinkMemo = response.body()!!
                var arr = linkMemo.content!!.arr

                // 출처 플랫폼
                var linkItemSource: String? = getSourceForLink(linkMemo.link)
                // pageSheet 선택 버튼 보여줄지 유무
                var unselectPageSheet = true
                if(arr != null && arr.size > 1) unselectPageSheet = false
                // 기본 정보 세팅: 링크 URL, 제목, 플랫폼, 폴더명
                setLinkViewInfo(
                    LinkMemoBaseInfo(
                        linkMemo.link,
                        linkMemo.title,
                        linkItemSource,
                        linkMemo.folder_name,
                        unselectPageSheet // pageSheet 선택 버튼 보여줄지 유무
                    )
                )

                // 메모 content 내용 추가
                if(arr != null) {
                    for(item in arr) {
                        // 아이템 내용을 json 형태로 변환
                        var jsonContent: JSONObject? = null
                        jsonContent = JSONObject(item)
                        // 글 아이템일 경우
                        if(jsonContent.getString("type") == "text") {
                            addLinkViewItem(
                                LinkViewTextItem(jsonContent.getString("value")) // 글 내용
                            )
                        }
                        // 이미지 아이템일 경우
                        else if(jsonContent.getString("type") == "image") {
                            addLinkViewItem(
                                LinkViewImageItem(jsonContent.getString("value")) // 이미지 url
                            )
                        }
                        // 주소 아이템일 경우
                        else if(jsonContent.getString("type") == "place") {
                            addLinkViewItem(
                                LinkViewPlaceItem(
                                    jsonContent.getString("road_address"), // 도로명 주소
                                    jsonContent.getString("land_address")  // 지번 주소
                                )
                            )
                        }
                        // 링크 아이템일 경우
                        else if(jsonContent.getString("type") == "link") {
                            addLinkViewItem(
                                LinkViewLinkItem(
                                    jsonContent.getString("title"), // 링크 제목
                                    jsonContent.getString("url")    // 링크 url
                                )
                            )
                        }
                        // 코드 아이템일 경우
                        else if(jsonContent.getString("type") == "code") {
                            addLinkViewItem(
                                LinkViewCodeItem(
                                    jsonContent.getString("value") // 코드 내용
                                )
                            )
                        }
                        // 체크리스트 아이템일 경우
                        else if(jsonContent.getString("type") == "checkbox") {
                            addLinkViewItem(
                                LinkViewCheckboxItem(
                                    jsonContent.getString("value"),                 // 할 일 내용
                                    jsonContent.getString("is_checked").toBoolean() // 체크 유무
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ViewLinkMemo>, t: Throwable) {
                Log.d(TAG, "메모 번호로 링크 내용 조회 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}