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
import java.util.stream.Collectors


/**
 * Created by kenny on 12/20/17.
 */
class NoOpPageRenderer : PageRenderer {

    override fun render(content: String): Page {
        return Page(content, mutableMapOf())
    }

}