package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.view.render.HtmlPageRenderer
import org.apache.commons.io.FilenameUtils
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class PageRendererResolver {
    private val NO_OP_PAGE_RENDERER = NoOpPageRenderer()

    private val renderers = mapOf(
            Pair("html", HtmlPageRenderer()),
            Pair("hbs", HtmlPageRenderer()),
            Pair("handlebars", HtmlPageRenderer()),
            Pair("md", FlexMarkPageRenderer()),
            Pair("markdown", FlexMarkPageRenderer())
    )

    fun resolve(path: Path): PageRenderer {
        val extension = FilenameUtils.getExtension(path.toString()).toLowerCase()
        renderers[extension]?.let { renderer ->
            return renderer
        }
        return NO_OP_PAGE_RENDERER
    }

}