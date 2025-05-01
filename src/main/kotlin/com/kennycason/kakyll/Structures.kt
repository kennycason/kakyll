package com.kennycason.kakyll

/**
 * Created by kenny on 5/17/17.
 */
object Structures {
    const val TEMPLATE_RESOURCE_PATH = "/com/kennycason/kakyll/default/"

    object Directories {
        const val SITE = "_site"

        const val TEMPLATES = "templates"
        const val POSTS = "posts" // only used for New command

        const val ASSETS = "assets"
        const val CSS = "css"
        const val JS = "js"
        const val IMAGES = "images"
    }

    object Files {
        const val CONFIG = "config.yml"
        const val INDEX = "index.hbs"
        const val ABOUT = "about.hbs"
        const val TAGS = "tags.hbs"
        const val SAMPLE_POST = "welcome-to-kakyll.md"

        const val STYLE_CSS = "style.css"
        const val KAKYLL_MAIN_IMAGE = "post_main_image.png"
        const val KAKYLL_THUMBNAIL = "post_thumbnail.png"

        object Templates {
            const val DEFAULT = "default.hbs"
            const val POST = "post.hbs"
        }
    }
}
