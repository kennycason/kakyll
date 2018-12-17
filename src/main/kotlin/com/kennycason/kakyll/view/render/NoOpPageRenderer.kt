package com.kennycason.kakyll.view.render


/**
 * Created by kenny on 12/20/17.
 */
class NoOpPageRenderer : PageRenderer {

    override fun render(content: String): Page {
        return Page(content, mutableMapOf())
    }

}