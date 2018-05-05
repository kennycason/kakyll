package com.kennycason.kakyll.util

import com.kennycason.kakyll.exception.KakyllException
import com.kennycason.kakyll.view.GlobalContext
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.regex.Pattern

/**
 * Created by kenny on 6/7/17.
 *
 * parse a year-month-day date from the beginning of the file
 *
 * Eventually this will become more customizable
 */
class DateParser {
    private val blogNameDateFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    private val dateRegex = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2}).*")

    fun now(): String = DateTime.now().toString(blogNameDateFormat)

    fun parse(url: String): DateTime {
        val matcher = dateRegex.matcher(url)
        if (!matcher.matches()) {
            throw KakyllException(
                    "Failed to parse date from url. " +
                    "Expected blogNameDateFormat is yyyy-MM-dd. Url [$url]")
        }
        val match = matcher.group(1)
        return blogNameDateFormat.parseDateTime(match)
    }

    fun prettyParse(url: String): String = parse(url).toString(DateTimeFormat.forPattern(GlobalContext.config.dateFormat))

}