package com.kennycason.kakyll.view.posts

import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.view.render.Page

/**
 * Created by kenny on 6/7/17.
 */
class PostsTagCloudBuilder {

    fun build(posts: List<Page>): List<Tag> {
        val tagCloud = mutableMapOf<String, Int>()
        // make this cleaner, right now it look puke
        posts.forEach { post ->
            if (post.parameters.containsKey("tags")) {
                val tags = post.parameters["tags"]
                if (tags is List<*>) {
                    tags.forEach { tag ->
                        if (tag is String) {
                            if (!tagCloud.contains(tag)) {
                                tagCloud[tag] = 0
                            }
                            tagCloud[tag] = tagCloud[tag]!! + 1
                        }
                    }
                }
                else {
                    throw KakyllException("tags parameter should be a list. Found [${tags!!::class}]")
                }
            }
        }
        return tagCloud
                .entries
                .map { entry -> Tag(
                        tag = entry.key,
                        count = entry.value,
                        url = "/tags/" + entry.key + ".html" // TODO standardize
                        ) }
                .sortedByDescending(Tag::count)
                .toList()
    }

}