package com.kennycason.kakyll.view.posts

import com.kennycason.kakyll.view.render.Page

/**
 * Builds collections of images from posts
 */
class ImagesCollectionBuilder {

    fun build(posts: List<Page>): Map<String, List<Image>> {
        val allImages = mutableListOf<Image>()
        val mainImages = mutableListOf<Image>()
        val thumbnails = mutableListOf<Image>()

        posts.forEach { post ->
            val postUrl = post.parameters["url"] as? String ?: ""
            val postTitle = post.parameters["title"] as? String ?: ""

            // Process main_image
            if (post.parameters.containsKey("main_image")) {
                val mainImagePath = post.parameters["main_image"] as? String
                if (mainImagePath != null) {
                    val mainImage = Image(
                        path = mainImagePath,
                        title = postTitle,
                        type = "main",
                        postUrl = postUrl,
                        postTitle = postTitle
                    )
                    mainImages.add(mainImage)
                }
            }

            // Process thumbnail
            if (post.parameters.containsKey("thumbnail")) {
                val thumbnailPath = post.parameters["thumbnail"] as? String
                if (thumbnailPath != null) {
                    val thumbnail = Image(
                        path = thumbnailPath,
                        title = postTitle,
                        type = "thumbnail",
                        postUrl = postUrl,
                        postTitle = postTitle
                    )
                    thumbnails.add(thumbnail)
                }
            }

            // Process images list
            if (post.parameters.containsKey("images")) {
                val images = post.parameters["images"]
                if (images is List<*>) {
                    images.forEach { imagePath ->
                        if (imagePath is String) {
                            val image = Image(
                                path = imagePath,
                                title = postTitle,
                                type = "regular",
                                postUrl = postUrl,
                                postTitle = postTitle
                            )
                            allImages.add(image)
                        }
                    }
                }
            }
        }

        return mapOf(
            "all_images" to allImages,
            "all_main_images" to mainImages,
            "all_thumbnails" to thumbnails
        )
    }
}
