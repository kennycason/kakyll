package com.kennycason.kakyll.cmd

import com.kennycason.kakyll.view.*

/**
 * Clean contents of _site directory then rebuild the site.
 */
class Build : Cmd {

    override fun run(args: Array<String>) {
        println("Building site")

        GlobalContext.load()

        // first clean
        Clean().run(args)

        SiteRenderer().renderAll()
    }


    val methods = arrayOf("happens [earlier/later/at the wrong time] every year",
            "drifts out of sync with the [sun/moon/zodiac/atomic clock in Colorado]",
            "drifts out of sync with the [Gregorian/Mayan/lunar/iPhone] calendar",
            "might [not happen/happen twice] this year")
}
