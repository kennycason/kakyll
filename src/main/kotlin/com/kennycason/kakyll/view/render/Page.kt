package com.kennycason.kakyll.view.render

/**
 * Created by kenny on 5/18/17.
 */
data class Page(val content: String,
                val parameters: MutableMap<String, Any> = mutableMapOf())

