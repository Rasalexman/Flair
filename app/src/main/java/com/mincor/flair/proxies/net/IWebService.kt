package com.mincor.flair.proxies.net

import com.mincor.flair.proxies.vo.Tag
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface IWebService {
    @GET("tags")
    @Headers("Content-type: application/json")
    fun getTags(@Query("last_update") lastUpdate:String = "0"):Call<TagResponse>
}

data class TagResponse(val response: TagDataResponse? = null, val ver:String?, val error:ErrorResponse? = null)
data class TagDataResponse(val last_update:String = "0", val tags: TagListsResponse? = null)
data class TagListsResponse(val added:List<Tag>? = null, val edited:List<Tag>? = null, val removed:List<Tag>? = null)
data class ErrorResponse(val code:Int? = 0, val message:String? = null)