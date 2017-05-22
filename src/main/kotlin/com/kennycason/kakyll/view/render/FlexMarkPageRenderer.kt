package com.kennycason.kakyll.view.render

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.kennycason.kakyll.Structures
import com.kennycason.kakyll.view.render.PageRenderer
import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.parser.Parser
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.nio.file.Path
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterVisitor
import com.vladsch.flexmark.html.HtmlRenderer
import java.util.Arrays
import com.vladsch.flexmark.util.options.MutableDataSet
import com.vladsch.flexmark.util.options.MutableDataHolder
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils
import java.nio.file.Files


/**
 * Created by kenny on 5/18/17.
 */
class FlexMarkPageRenderer : PageRenderer {
    private val OPTIONS: MutableDataHolder = MutableDataSet()
            .set(Parser.EXTENSIONS, listOf(
                    YamlFrontMatterExtension.create(),
                    TablesExtension.create(),
                    StrikethroughExtension.create(),
                    EmojiExtension.create()))

    override fun render(content: String): Page {
        // first parse template
        val parser = Parser.builder(OPTIONS).build()
        val document = parser.parse(content)

        // render to html
        val renderer = HtmlRenderer.builder().build()
        val html = renderer.render(document)

        val metadataParser = AbstractYamlFrontMatterVisitor()
        metadataParser.visit(document)

        return Page(html, transform(metadataParser.data))
    }


    private fun transform(data: Map<String, MutableList<String>>): MutableMap<String, Any> {
        val transformed = mutableMapOf<String, Any>()
        data.entries.forEach { entry ->
            if (entry.value.size == 1) {
                transformed.put(entry.key, entry.value.first())
            } else {
                transformed.put(entry.key, entry.value)
            }
        }
        return transformed
    }

}