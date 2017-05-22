package com.kennycason.kakyll.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern

/**
 * Created by kenny on 5/21/17.
 */
class PostDateGenerator {
    private val datePattern = Pattern.compile("[0-9]{4}-[0-9]{2}-[0-9]{2}")
    private val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun now() = LocalDate.now().format(dateFormat)

    fun parse(fileName: String): LocalDate {
        val dateMatcher = datePattern.matcher(fileName)
        if (!dateMatcher.matches()) {
            throw RuntimeException("Failed to extract date from file name. Format should be [yyyy-MM-dd-file-name.md]")
        }
        return LocalDate.parse(dateMatcher.group())
    }
}