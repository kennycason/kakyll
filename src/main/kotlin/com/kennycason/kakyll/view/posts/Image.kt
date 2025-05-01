package com.kennycason.kakyll.view.posts

/**
 * Represents an image in a post
 */
data class Image(
    val path: String,
    val title: String = "",
    val type: String = "regular", // "main", "thumbnail", or "regular"
    val postUrl: String = "",
    val postTitle: String = ""
)
