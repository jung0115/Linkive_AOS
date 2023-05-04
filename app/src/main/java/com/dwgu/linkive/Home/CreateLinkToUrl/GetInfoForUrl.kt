package com.dwgu.linkive.Home.CreateLinkToUrl

import android.util.Log
import com.dwgu.linkive.Home.HomeLinkListRecycler.LinkListData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

// URL을 이용해서 웹페이지 정보 가져오기
suspend fun GetInfoForUrl(url: String, folder: String?): LinkListData? {
    var ogMap: HashMap<String, String> = hashMapOf()

    // 페이지에서 가져온 데이터 정리
    var linkData: LinkListData? = null

    //CoroutineScope(Dispatchers.IO).launch {
    GlobalScope.launch(Dispatchers.IO) {

        val deferedLinkData: LinkListData = async {
            val document = Jsoup.connect(url).get()
            val elements = document.select("meta[property^=og:]")

            elements?.let {
                it.forEach { el ->
                    when (el.attr("property")) {
                        "og:url" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("url", content)
                            }
                        }

                        "og:site_name" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("siteName", content)
                            }
                        }

                        "og:title" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("title", content)
                            }
                        }

                        "og:description" -> {
                            el.attr("content")?.let { content ->
                                ogMap.put("description", content)
                            }
                        }

                        "og:image" -> {
                            ogMap.put("image", el.attr("content"))
                        }
                    }
                }

                try {
                    // url - ogMap.get("url").toString()
                    var linkUrl = ogMap.get("url").toString()

                    // 제목 - ogMap.get("title").toString()
                    var title: String = ogMap.get("title").toString()
                    // 제목 글자수가 10자를 넘어가는 경우
                    if (ogMap.get("title").toString().length > 10) {
                        title = title.substring(0 until 10) + "..."
                    }
                    // 제목이 없는 경우 - 내용 첫 10글자를 제목으로
                    else if (ogMap.get("title").toString().length == 0 || title == null) {
                        title = ogMap.get("description").toString().substring(0 until 10) + "..."
                    }

                    // 폴더명 - folderName
                    var folderName = folder
                    if (folderName == null) folderName = "폴더 없음"

                    // 이미지 url - ogMap.get("image").toString()
                    var thumbnailImage = ogMap.get("image").toString()

                    // 내용 - ogMap.get("description").toString()

                    // 출처 플랫폼
                    var linkItemSource: String? = null
                    // 일단 url에 해당 문자를 포함하면 해당 플랫폼 페이지인 것으로 판단 -> 그러나 아닌 경우도 있음
                    if (linkUrl.contains("instagram")) {
                        linkItemSource = "instagram"
                    } else if (linkUrl.contains("twitter")) {
                        linkItemSource = "twitter"
                    } else if (linkUrl.contains("naver") && linkUrl.contains("blog")) {
                        linkItemSource = "naver-blog"
                    }

                    // 인스타그램은 OpenGraph를 지원하지 않음
                    // 트위터, 네이버 블로그는 OpenGraph 지원

                    linkData =
                        LinkListData(linkUrl, title, folderName, thumbnailImage, linkItemSource)

                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    //Toast.makeText(this, "OpenGraph를 지원하지 않거나 잘못된 URL입니다.", Toast.LENGTH_SHORT).show()
                    // OpenGraph를 지원하지 않는 URL
                    // 내용을 가져올 수 없음

                    // url - ogMap.get("url").toString()
                    var linkUrl = ogMap.get("url").toString()

                    // 제목 - 내용을 가져올 수 없으므로 url 첫 10글자를 제목으로
                    var title = linkUrl.substring(0 until 10) + "..."

                    // 폴더명 - folderName
                    var folderName = folder
                    if (folderName == null) folderName = "폴더 없음"

                    // 이미지 url - 내용을 가져올 수 없으므로 비워두기
                    var thumbnailImage = null

                    // 출처 플랫폼
                    var linkItemSource: String? = null
                    // 일단 url에 해당 문자를 포함하면 해당 플랫폼 페이지인 것으로 판단 -> 그러나 아닌 경우도 있음
                    // 인스타그램은 OpenGraph를 지원하지 않음
                    if (linkUrl.contains("instagram")) {
                        linkItemSource = "instagram"
                    } else if (linkUrl.contains("twitter")) {
                        linkItemSource = "twitter"
                    } else if (linkUrl.contains("naver") && linkUrl.contains("blog")) {
                        linkItemSource = "naver-blog"
                    }

                    linkData =
                        LinkListData(linkUrl, title, folderName, thumbnailImage, linkItemSource)

                    //return@let
                }
            }
            linkData!!
        }.await()

        return@launch
    }

    // 만약 아직 데이터를 가져오지 못했다면 1초간 기다리기
    if(linkData == null) {
        delay(1000)
    }

    Log.d("checkUrl", linkData.toString())

    return linkData
}