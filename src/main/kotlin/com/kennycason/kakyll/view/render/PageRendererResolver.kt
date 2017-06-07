package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.view.render.HtmlPageRenderer
import org.apache.commons.io.FilenameUtils
import java.nio.file.Path

/**
 * Created by kenny on 5/18/17.
 */
class PageRendererResolver {
    private val renderers = mapOf(
            Pair("html", HtmlPageRenderer()),
            Pair("hbs", HtmlPageRenderer()),
            Pair("handlebars", HtmlPageRenderer()),
            Pair("md", FlexMarkPageRenderer())
    )

    fun resolve(path: Path): PageRenderer {
        val extension = FilenameUtils.getExtension(path.toString()).toLowerCase()
        renderers[extension]?.let { renderer ->
            return renderer
        }
        throw RuntimeException("Could not find Html Renderer for file extensions [${extension}]")
    }

}