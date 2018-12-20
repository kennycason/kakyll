package com.kennycason.kakyll.view.render

import com.vladsch.flexmark.ext.emoji.EmojiExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.gfm.tables.TablesExtension
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataHolder
import com.vladsch.flexmark.util.options.MutableDataSet


/**
 * Created by kenny on 5/18/17.
 */
class FlexMarkPageRenderer : PageRenderer {

    private val options = MutableDataSet()
            .set(Parser.EXTENSIONS, listOf(
                    YamlFrontMatterExtension.create(),
                    TablesExtension.create(),
                    StrikethroughExtension.create(),
                    EmojiExtension.create()
            ))

    override fun render(content: String): Page {
        // first parse templates
        val parser = Parser.builder(options).build()
        val document = parser.parse(content)
        // render to content
        val renderer = HtmlRenderer.builder(options).build()
        val html = renderer.render(document)

        val metadataParser = AbstractYamlFrontMatterVisitor()
        metadataParser.visit(document)

        return Page(html, transform(metadataParser.data))
    }


    private fun transform(data: Map<String, MutableList<String>>): MutableMap<String, Any> {
        val transformed = mutableMapOf<String, Any>()
        data.entries.forEach { entry ->
            if (entry.value.size == 1) {
                transformed[entry.key] = maybeTransformToList(entry.key, entry.value.first())
            } else {
                transformed[entry.key] = maybeTransformToList(entry.key, entry.value)
            }
        }
        return transformed
    }

    private fun maybeTransformToList(key: String, parameter: Any): Any {
        if (parameter !is String) { return parameter }

        if (key == "tags") {
            return parameter
                    .split(",")
                    .map(String::trim)
                    .toList()
        }
        return parameter
    }

}