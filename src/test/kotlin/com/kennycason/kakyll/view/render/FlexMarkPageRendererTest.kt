package com.kennycason.kakyll.view.render

import org.apache.commons.io.IOUtils
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

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

    @Test
    fun imageExtraction() {
        val markdown = """
            ---
            title: Test Post
            author: Test Author
            tags: test, preview
            main_image: /assets/images/test-main.jpg
            thumbnail: /assets/images/test-thumbnail.jpg
            description: A test post with images
            ---

            Test content
        """.trimIndent()

        val markdownRenderer = FlexMarkPageRenderer()
        val page = markdownRenderer.render(markdown)

        // Verify that the main image is extracted from the front matter
        assertTrue(page.parameters.containsKey("main_image"))
        assertEquals("/assets/images/test-main.jpg", page.parameters["main_image"])

        // Verify that the thumbnail is extracted from the front matter
        assertTrue(page.parameters.containsKey("thumbnail"))
        assertEquals("/assets/images/test-thumbnail.jpg", page.parameters["thumbnail"])

        // Verify other metadata is also extracted correctly
        assertEquals("Test Post", page.parameters["title"])
        assertEquals("Test Author", page.parameters["author"])
        assertEquals("A test post with images", page.parameters["description"])

        // Verify tags are parsed as a list
        assertTrue(page.parameters["tags"] is List<*>)
        val tags = page.parameters["tags"] as List<*>
        assertEquals(2, tags.size)
        assertEquals("test", tags[0])
        assertEquals("preview", tags[1])
    }

}
