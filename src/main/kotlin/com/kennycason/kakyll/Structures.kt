package com.kennycason.kakyll

/**
 * Created by kenny on 5/17/17.
 */
object Structures {
     val TEMPLATE_RESOURCE_PATH = "/com/kennycason/kakyll/default/"

    object Directories {
        val SITE = "_site"

        val TEMPLATES = "templates"
        val POSTS = "posts" // only used for New command

        val ASSETS = "assets"
        val CSS = "css"
        val JS = "js"
        val IMAGES = "images"
    }

    object Files {
        val CONFIG = "config.yml"
        val INDEX = "index.hbs"
        val ABOUT = "about.hbs"
        val SAMPLE_POST = "welcome-to-kakyll.md"

        object Templates {
//            val HEADER = "header.hbs"
//            val FOOTER = "footer.hbs"
            val DEFAULT = "default.hbs"
            val POST = "post.hbs"
        }
    }
}