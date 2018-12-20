package com.kennycason.kakyll.view.render

import org.apache.commons.io.IOUtils
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by kenny
 */
class FlexMarkPageRendererTest {

    @Test
    fun githubTables() {
        val content = javaClass.getResource("/com/kennycason/kakyll/view/render/post_with_github_tables.markdown").readText()
        val expectedHtml = javaClass.getResource("/com/kennycason/kakyll/view/render/post_with_github_tables.html").readText()

        val markdownRenderer = FlexMarkPageRenderer()
        val renderedHtml = markdownRenderer.render(content).content

        assertEquals(expectedHtml, renderedHtml)
    }

}