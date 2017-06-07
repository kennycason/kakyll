package com.kennycason.kakyll.view.posts

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
                val tags = post.parameters.get("tags")
                if (tags is List<*>) {
                    tags.forEach { tag ->
                        if (tag is String) {
                            if (!tagCloud.contains(tag)) {
                                tagCloud.put(tag, 0)
                            }
                            tagCloud.put(tag, tagCloud.get(tag)!! + 1)
                        }
                    }
                }
            }
        }
        return tagCloud
                .entries
                .map { entry -> Tag(entry.key, entry.value) }
                .sortedByDescending(Tag::count)
                .toList()
    }

}