package com.kennycason.kakyll.util

import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.util.DateParser
import org.joda.time.DateTime
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

/**
 * Created by kenny on 6/7/17.
 */
class DateParserTest {
    private val dateParser = DateParser()

    @Test
    fun standard() {
        val date = dateParser.parse("2017-03-15-welcome-to-kakyll.md")
        assertEquals(2017, date.year)
        assertEquals(3, date.monthOfYear)
        assertEquals(15, date.dayOfMonth)
    }

    @Test
    fun singleDigitDayAndMonth() {
        val date = dateParser.parse("2000-1-1-welcome-to-kakyll.md")
        assertEquals(2000, date.year)
        assertEquals(1, date.monthOfYear)
        assertEquals(1, date.dayOfMonth)
    }

    @Test(expected = KakyllException::class)
    fun noDate() {
        dateParser.parse("welcome-to-kakyll.md")
    }

    @Test(expected = KakyllException::class)
    fun badDate() {
        dateParser.parse("10-01-welcome-to-kakyll.md")
    }

    @Test
    fun now() {
        val now = DateTime.now()
        val nowString = dateParser.now()
        val nowParsed = dateParser.parse(nowString + "-foo-bar.md")
        assertEquals(now.year, nowParsed.year)
        assertEquals(now.monthOfYear, nowParsed.monthOfYear)
        assertEquals(now.dayOfMonth, nowParsed.dayOfMonth)
    }

}