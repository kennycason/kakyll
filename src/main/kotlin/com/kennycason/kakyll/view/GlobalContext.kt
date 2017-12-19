package com.kennycason.kakyll.view

import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.posts.PostsLoader
import com.kennycason.kakyll.view.posts.PostsTagCloudBuilder

/**
 * Created by kenny on 6/7/17.
 *
 * Load the context once and only once
 */
object GlobalContext {
    fun posts() = PostsLoader().load()
    fun tags() = PostsTagCloudBuilder().build(posts())
    fun config() = ConfigLoader().load()
}