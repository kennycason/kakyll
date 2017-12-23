package com.kennycason.kakyll.view

import com.kennycason.kakyll.config.ConfigLoader
import com.kennycason.kakyll.view.posts.PostsLoader
import com.kennycason.kakyll.view.posts.PostsTagCloudBuilder

/**
 * Created by kenny on 6/7/17.
 *
 * Load the context lazily, refresh when changes happen
 */
object GlobalContext {
    var config = ConfigLoader().load()
    var posts = PostsLoader().load()
    var tags = PostsTagCloudBuilder().build(posts)

    fun load() {
        config = ConfigLoader().load()
        posts = PostsLoader().load()
        tags = PostsTagCloudBuilder().build(posts)
    }

}