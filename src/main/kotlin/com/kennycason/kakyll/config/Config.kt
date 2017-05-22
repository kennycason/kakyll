package com.kennycason.kakyll.config

import com.kennycason.kakyll.view.render.Page

/**
 * Created by kenny on 5/18/17.
 */
data class Config(var title: String = "",
                  var email: String = "",
                  var description: String = "",
                  var baseUrl: String = "",
                  var templateEngine: String = "",
                  var encoding: String = "UTF-8",
                  var pages: MutableList<String> = mutableListOf(),
                  var directories: MutableList<String> = mutableListOf())