package com.kennycason.kakyll.view.render

import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
interface PageRenderer {
    fun render(content: String): Page
}