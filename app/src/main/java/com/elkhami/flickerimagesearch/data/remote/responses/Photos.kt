package com.elkhami.flickerimagesearch.data.remote.responses

data class Photos(
    val page: Int,
    val pages: String,
    val perpage: Int,
    val photo: List<Photo>,
    val total: String
)