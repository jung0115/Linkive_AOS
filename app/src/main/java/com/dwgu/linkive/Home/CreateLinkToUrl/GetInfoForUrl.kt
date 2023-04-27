package com.dwgu.linkive.Home.CreateLinkToUrl

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

// URL을 이용해서 웹페이지 정보 가져오기
public fun GetInfoForUrl(url: String, folder: String?) {
    var ogMap: HashMap<String, String> = hashMapOf()

    CoroutineScope(Dispatchers.IO).launch {
        val document = Jsoup.connect(url).get()
        val elements = document.select("meta[property^=og:]")
        elements?.let {
            it.forEach { el ->
                when(el.attr("property")) {
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
                    "og:image" -> { ogMap.put("image", el.attr("content")) }
                }
            }

            try {
                var title: String = ogMap.get("title").toString()
                // 제목 글자수가 10자를 넘어가는 경우
                if(ogMap.get("title").toString().length > 10) {
                    title = title.substring(0 until 10) + "..."
                }
                // 제목이 없는 경우 - 내용 첫 10글자를 제목으로
                else if(ogMap.get("title").toString().length == 0 || title == null) {
                    title = ogMap.get("description").toString().substring(0 until 10) + "..."
                }
                // url - ogMap.get("url").toString()
                // 제목 - title
                // 폴더명 - folder
                // 이미지 url - ogMap.get("image").toString()
                // 내용 - ogMap.get("description").toString()
                // 출처 플랫폼

                // 인스타그램은 OpenGraph를 지원하지 않음
                // 트위터, 네이버 블로그는 OpenGraph 지원함

            } catch (e: NullPointerException) {
                e.printStackTrace()
                //Toast.makeText(this, "OpenGraph를 지원하지 않거나 잘못된 URL입니다.", Toast.LENGTH_SHORT).show()
                return@launch
            }
        }
    }
}