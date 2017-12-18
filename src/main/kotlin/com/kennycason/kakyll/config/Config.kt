package com.kennycason.kakyll.config

import com.kennycason.kakyll.view.render.Page

/**
 * Created by kenny on 5/18/17.
 *
 * var instead of val and default values are soe that jackson mapping can work
 */
data class Config(var title: String = "",
                  var email: String = "",
                  var description: String = "",
                  var baseUrl: String = "",
                  var templateEngine: String = "",
                  var encoding: String = "UTF-8",
                  var deploy: String = "",
                  var posts: Posts = Posts("post", ""),
                  var pages: MutableList<String> = mutableListOf(),
                  var directories: MutableList<String> = mutableListOf())

data class Posts(var directory: String = "",
                 var template: String = "")