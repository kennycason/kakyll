package com.kennycason.kakyll.view.render

import com.kennycason.kakyll.view.render.PageRenderer
import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataHolder
import com.vladsch.flexmark.util.options.MutableDataSet
import java.nio.file.Path

/**
 * HtmlRender doesn't need to do anything except return the content as-is
 */
class HtmlPageRenderer : PageRenderer {

    override fun render(content: String) = Page(content)

}