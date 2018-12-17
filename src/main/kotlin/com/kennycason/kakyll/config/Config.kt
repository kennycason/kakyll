package com.kennycason.kakyll.config

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
                  var dateFormat: String = "yyyy-dd-MM",
                  var posts: Posts = Posts(),
                  var tags: Tags = Tags(),
                  var pages: MutableList<String> = mutableListOf(),
                  var directories: MutableList<Directory> = mutableListOf())

data class Posts(var directory: String = "post",
                 var template: String = "")

data class Tags(var template: String = "index.hbs")

data class Directory(val name: String = "", val render: Boolean = false)