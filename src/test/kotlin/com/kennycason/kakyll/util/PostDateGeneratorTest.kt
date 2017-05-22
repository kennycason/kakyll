package com.kennycason.kakyll.util

import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Created by kenny on 5/21/17.
 */
class PostDateGeneratorTest {

    @Test
    fun basicTest() {
        val postDateGenerator = PostDateGenerator()
        val now = LocalDate.now()
        val nowString = postDateGenerator.now()
        val nowParsed = postDateGenerator.parse(nowString + "-foo-bar.md")
        assertEquals(now.year, nowParsed.year)
        assertEquals(now.month, nowParsed.month)
        assertEquals(now.dayOfMonth, nowParsed.dayOfMonth)
    }

}