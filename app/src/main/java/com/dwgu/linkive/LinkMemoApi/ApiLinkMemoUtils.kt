package com.dwgu.linkive.LinkMemoApi.CreateLinkMemo

import android.content.ContentValues.TAG
import android.util.Log
import com.dwgu.linkive.Api.ApiClient
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkCheckboxItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkCodeItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkImageItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkLinkItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkPlaceItem
import com.dwgu.linkive.EditLink.EditLinkRecyclerview.EditLinkTextItem
import com.dwgu.linkive.Home.CreateLinkToUrl.CreateLinkToUrlDialog
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListItem
import com.dwgu.linkive.LinkMemoApi.DeleteLinkMemo.DeleteLinkMemoRequest
import com.dwgu.linkive.LinkMemoApi.DeleteLinkMemo.DeleteLinkMemoService
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.DetailLinkMemoService
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.LinkMemoBaseInfo
import com.dwgu.linkive.LinkMemoApi.DetailLinkMemo.LinkMemoEditBaseInfo
import com.dwgu.linkive.LinkMemoApi.EditLinkMemo.EditLinkMemoData
import com.dwgu.linkive.LinkMemoApi.EditLinkMemo.EditLinkMemoService
import com.dwgu.linkive.LinkMemoApi.GetAllPageSheet.GetAllPageSheetService
import com.dwgu.linkive.LinkMemoApi.GetAllPageSheet.GetPageSheetData
import com.dwgu.linkive.LinkMemoApi.GetAllPageSheet.GetPageSheetListData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemo
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoData
import com.dwgu.linkive.LinkMemoApi.ViewLinkMemo.ViewLinkMemoService
import com.dwgu.linkive.LinkView.LinkViewMenuListener.LinkViewMenuListener
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewCheckboxItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewCodeItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewImageItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewLinkItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewPlaceItem
import com.dwgu.linkive.LinkView.LinkViewRecycler.LinkViewTextItem
import com.dwgu.linkive.Login.GloabalApplication
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

// token 정보 세팅
fun setTokenForMemo() {
    authorization = "JWT " + GloabalApplication.prefs.getString("accessToken", "")
    refreshToken = GloabalApplication.prefs.getString("refreshToken", "")

    //Log.d("Test token", authorization + ", " + refreshToken)
}

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
                val linkLists: MutableList<ViewLinkMemo> = response.body()!!.memoList
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
    } else if (linkUrl.contains("youtube") || linkUrl.contains("youtu.be")) {
        linkItemSource = "youtube"
    } else if (linkUrl.contains("github")) {
        linkItemSource = "github"
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
                        linkMemo.folder_num,
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
                            var text: String? = jsonContent.getString("value")
                            if(text == "null" || text!!.length == 0) text = null
                            addLinkViewItem(
                                LinkViewTextItem(text) // 글 내용
                            )
                        }
                        // 이미지 아이템일 경우
                        else if(jsonContent.getString("type") == "image") {
                            var imageUrl: String? = jsonContent.getString("value")
                            if(imageUrl == "null" || imageUrl!!.length == 0) imageUrl = null
                            addLinkViewItem(
                                LinkViewImageItem(imageUrl) // 이미지 url
                            )
                        }
                        // 주소 아이템일 경우
                        else if(jsonContent.getString("type") == "place") {
                            var roadAddress: String? = jsonContent.getString("road_address") // 도로명 주소
                            var landAddress: String? = jsonContent.getString("land_address") // 지번 주소
                            if(roadAddress == "null" || roadAddress!!.length == 0) roadAddress = null
                            if(landAddress == "null" || landAddress!!.length == 0) landAddress = null
                            addLinkViewItem(
                                LinkViewPlaceItem(
                                    roadAddress, // 도로명 주소
                                    landAddress  // 지번 주소
                                )
                            )
                        }
                        // 링크 아이템일 경우
                        else if(jsonContent.getString("type") == "link") {
                            var title: String? = jsonContent.getString("title") // 링크 제목
                            var url: String? = jsonContent.getString("url") // 링크 url
                            if(title == "null" || title!!.length == 0) title = null
                            if(url == "null" || url!!.length == 0) url = null
                            addLinkViewItem(
                                LinkViewLinkItem(
                                    title, // 링크 제목
                                    url    // 링크 url
                                )
                            )
                        }
                        // 코드 아이템일 경우
                        else if(jsonContent.getString("type") == "code") {
                            var code: String? = jsonContent.getString("value")
                            if(code == "null" || code!!.length == 0) code = null
                            addLinkViewItem(
                                LinkViewCodeItem(
                                    code // 코드 내용
                                )
                            )
                        }
                        // 체크리스트 아이템일 경우
                        else if(jsonContent.getString("type") == "checkbox") {
                            var todo: String? = jsonContent.getString("value")
                            if(todo == "null" || todo!!.length == 0) todo = null

                            var isChecked: Boolean = false
                            if(jsonContent.getString("is_checked") == "Y") isChecked = true

                            addLinkViewItem(
                                LinkViewCheckboxItem(
                                    todo,     // 할 일 내용
                                    isChecked // 체크 유무
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

// 메모 번호로 링크 메모 삭제
fun apiDeleteLinkMemo(memoNum: Int) {
    val deleteMemo = DeleteLinkMemoRequest(memoNum)
    retrofit.create(DeleteLinkMemoService::class.java)
        .deleteLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, params = deleteMemo)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "메모 번호로 링크 삭제 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "메모 번호로 링크 삭제 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 폴더 이동
fun apiMoveFolder(
    memoNum: Int,
    folderNum: Int,
    reopenLinkViewListener: LinkViewMenuListener?) {
    // 링크 메모 내용 조회
    retrofit.create(DetailLinkMemoService::class.java)
        .detailLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, memosNum = memoNum)
        .enqueue(object : Callback<ViewLinkMemo> {
            override fun onResponse(call: Call<ViewLinkMemo>, response: Response<ViewLinkMemo>) {
                Log.d(TAG, "폴더 이동 전 링크 내용 조회  -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                val oriLinkMemo = response.body()
                if(oriLinkMemo != null) {
                    // 수정할 내용
                    val editLinkMemo = EditLinkMemoData(
                        memoNum,
                        oriLinkMemo.link,
                        oriLinkMemo.title,
                        oriLinkMemo.content,
                        folderNum
                    )

                    // 링크 메모 수정
                    apiEditLinkMemo(editLinkMemo, reopenLinkViewListener, finish = null)
                }
            }

            override fun onFailure(call: Call<ViewLinkMemo>, t: Throwable) {
                Log.d(TAG, "폴더 이동 전 링크 내용 조회 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 링크 메모 편집 내용
fun apiEditLinkMemoContent(
    memoNum: Int,
    title: String,
    content: LinkMemoContent,
    finish: () -> Unit) {
    // 링크 메모 내용 조회
    retrofit.create(DetailLinkMemoService::class.java)
        .detailLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, memosNum = memoNum)
        .enqueue(object : Callback<ViewLinkMemo> {
            override fun onResponse(call: Call<ViewLinkMemo>, response: Response<ViewLinkMemo>) {
                Log.d(TAG, "링크 메모 편집 전 링크 내용 조회  -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                val oriLinkMemo = response.body()!!
                apiEditLinkMemo(
                    editLinkMemo = EditLinkMemoData(
                        memo_num = memoNum,                 // 메모 번호
                        link = oriLinkMemo.link,            // 링크 URL
                        title = title,                      // 제목
                        content = content,                  // 내용
                        folder_num = oriLinkMemo.folder_num // 폴더 번호
                    ),
                    reopenLinkViewListener = null,
                    finish = {
                        finish()
                    }
                )
            }

            override fun onFailure(call: Call<ViewLinkMemo>, t: Throwable) {
                Log.d(TAG, "링크 메모 편집 전 링크 내용 조회 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 링크 메모 편집
fun apiEditLinkMemo(
    editLinkMemo: EditLinkMemoData,
    reopenLinkViewListener: LinkViewMenuListener?,
    finish: (() -> Unit)?) {
    retrofit.create(EditLinkMemoService::class.java)
        .editLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, editLinkMemo)
        .enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                Log.d(TAG, "링크 메모 편집 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                // 링크 세부 페이지 Reopen
                if(reopenLinkViewListener != null) {
                    reopenLinkViewListener.reopenLinkViewListener(editLinkMemo.memo_num)
                }
                // 링크 편집 페이지 닫기
                if(finish != null) {
                    finish()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.d(TAG, "링크 메모 편집 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// PageSheet 전체 조회
fun apiGetAllPageSheet(setAllpageSheet: (pagesheets: MutableList<GetPageSheetData>) -> Unit) {
    retrofit.create(GetAllPageSheetService::class.java)
        .getAllPageSheet(authorization = authorization!!, refreshToken = refreshToken!!)
        .enqueue(object : Callback<GetPageSheetListData> {
            override fun onResponse(call: Call<GetPageSheetListData>, response: Response<GetPageSheetListData>) {
                Log.d(TAG, "PageSheet 전체 조회 결과 -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                setAllpageSheet(response.body()!!.pagesheets)
            }

            override fun onFailure(call: Call<GetPageSheetListData>, t: Throwable) {
                Log.d(TAG, "PageSheet 전체 조회 결과 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}

// 메모 번호로 링크 내용 조회
fun apiGetEditLinkMemo(
    memoNum: Int,
    setLinkEditInfo: (baseInfo: LinkMemoEditBaseInfo) -> Unit,
    addLinkEditItem: (detailItem: EditLinkItem) -> Unit,
) {

    retrofit.create(DetailLinkMemoService::class.java)
        .detailLinkMemo(authorization = authorization!!, refreshToken = refreshToken!!, memosNum = memoNum)
        .enqueue(object : Callback<ViewLinkMemo> {
            override fun onResponse(call: Call<ViewLinkMemo>, response: Response<ViewLinkMemo>) {
                Log.d(TAG, "편집: 메모 번호로 링크 내용 조회  -------------------------------------------")
                Log.d(TAG, "onResponse: ${response.body().toString()}")

                var linkMemo: ViewLinkMemo = response.body()!!
                var arr = linkMemo.content!!.arr

                // 출처 플랫폼
                var linkItemSource: String? = getSourceForLink(linkMemo.link)
                // 기본 정보 세팅: 링크 URL, 제목, 플랫폼, 페이지시트 번호
                setLinkEditInfo(
                    LinkMemoEditBaseInfo(
                        linkMemo.title,
                        linkItemSource,
                        linkMemo.content?.pagesheet_num
                    )
                )

                // 메모 content 내용 추가
                if(arr != null) {
                    for(i in 0 until arr.size) {
                        // 아이템 내용을 json 형태로 변환
                        var jsonContent: JSONObject? = null
                        jsonContent = JSONObject(arr[i])
                        // 글 아이템일 경우
                        if(jsonContent.getString("type") == "text") {
                            var text: String? = jsonContent.getString("value")
                            if(text == "null" || text!!.length == 0) text = null
                            addLinkEditItem(
                                EditLinkTextItem(text, i) // 글 내용
                            )
                        }
                        // 이미지 아이템일 경우
                        else if(jsonContent.getString("type") == "image") {
                            var imageUrl: String? = jsonContent.getString("value")
                            if(imageUrl == "null" || imageUrl!!.length == 0) imageUrl = null
                            addLinkEditItem(
                                EditLinkImageItem(imageUrl, null, i) // 이미지 url
                            )
                        }
                        // 주소 아이템일 경우
                        else if(jsonContent.getString("type") == "place") {
                            var roadAddress: String? = jsonContent.getString("road_address") // 도로명 주소
                            var landAddress: String? = jsonContent.getString("land_address") // 지번 주소
                            if(roadAddress == "null" || roadAddress!!.length == 0) roadAddress = null
                            if(landAddress == "null" || landAddress!!.length == 0) landAddress = null
                            addLinkEditItem(
                                EditLinkPlaceItem(
                                    roadAddress, // 도로명 주소
                                    landAddress, // 지번 주소
                                    i
                                )
                            )
                        }
                        // 링크 아이템일 경우
                        else if(jsonContent.getString("type") == "link") {
                            var title: String? = jsonContent.getString("title") // 링크 제목
                            var url: String? = jsonContent.getString("url") // 링크 url
                            if(title == "null" || title!!.length == 0) title = null
                            if(url == "null" || url!!.length == 0) url = null
                            addLinkEditItem(
                                EditLinkLinkItem(
                                    title, // 링크 제목
                                    url,   // 링크 url
                                    i
                                )
                            )
                        }
                        // 코드 아이템일 경우
                        else if(jsonContent.getString("type") == "code") {
                            var code: String? = jsonContent.getString("value")
                            if(code == "null" || code!!.length == 0) code = null
                            addLinkEditItem(
                                EditLinkCodeItem(
                                    code, // 코드 내용
                                    i
                                )
                            )
                        }
                        // 체크리스트 아이템일 경우
                        else if(jsonContent.getString("type") == "checkbox") {
                            var todo: String? = jsonContent.getString("value")
                            if(todo == "null" || todo!!.length == 0) todo = null

                            var isChecked: Boolean = false
                            if(jsonContent.getString("is_checked") == "Y") isChecked = true

                            addLinkEditItem(
                                EditLinkCheckboxItem(
                                    todo,      // 할 일 내용
                                    isChecked, // 체크 유무
                                    i
                                )
                            )
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ViewLinkMemo>, t: Throwable) {
                Log.d(TAG, "편집: 메모 번호로 링크 내용 조회 fail -------------------------------------------")
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
}