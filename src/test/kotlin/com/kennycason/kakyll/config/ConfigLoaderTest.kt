package com.kennycason.kakyll.config

import org.junit.Test
import kotlin.test.assertEquals

/**
 * Created by kenny on 6/6/17.
 */
class ConfigLoaderTest {

    @Test
    fun load() {
        val path = "/com/kennycason/kakyll/config/sample.yml"
        val inputStream = javaClass.getResourceAsStream(path)
        val config = ConfigLoader().load(inputStream)

        assertEquals("My Blog Title", config.title)
        assertEquals("you@email.com", config.email)
        assertEquals("My Blog is about random stuff.", config.description)
        assertEquals("http://localhost:8080", config.baseUrl)
        assertEquals("handlebars", config.templateEngine)
        assertEquals("UTF-8", config.encoding)
        assertEquals("posts", config.posts.directory)
        assertEquals("post.hbs", config.posts.template)
        assertEquals("index.hbs", config.pages[0])
        assertEquals("about.hbs", config.pages[1])
        assertEquals("assets", config.directories[0])
    }

}