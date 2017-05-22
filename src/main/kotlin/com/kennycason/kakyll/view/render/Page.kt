package com.kennycason.kakyll.view.render

/**
 * Created by kenny on 5/18/17.
 */
data class Page(val html: String,
                val parameters: MutableMap<String, Any> = mutableMapOf())