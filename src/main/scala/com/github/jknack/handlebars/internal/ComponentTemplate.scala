package com.github.jknack.handlebars.internal

import com.github.jknack.handlebars.{Context, Template, Handlebars => H}
import scala.collection.JavaConversions._
import com.twitter.util.Future
import java.io.Writer
import com.github.savaki.handlebars.{Handlebars, ComponentContext, ComponentResponse, ComponentRequest}
import java.lang.reflect.Field

/**
 * @author matt.ho@gmail.com
 */
class ComponentTemplate(handlebars: Handlebars, template: Template) {
  /**
   * the list of blocks that reference components
   */
  private val blocks: List[Block] = findBlocks(template)

  private def findBlocksWithinPartial(partial: Partial): List[Block] = {
    val field: Field = partial.getClass.getDeclaredField("template")
    field.setAccessible(true)
    val partialTemplate: Template = field.get(partial).asInstanceOf[Template]
    findBlocks(partialTemplate)
  }

  private def findBlocks(template: Template): List[Block] = {
    template match {
      case block: Block if handlebars.helperName.equalsIgnoreCase(block.name()) => List(block)
      case partial: Partial => findBlocksWithinPartial(partial)
      case list: TemplateList => list.toList.flatMap(t => findBlocks(t))
      case _ => List[Block]()
    }
  }

  private val blocksWithParams = blocks.map {
    block => block.body() -> new ParamExtractor(block, handlebars.underlying).paramsToString().split(" ")
  }

  def render(context: ComponentContext): Future[String] = {
    val futures: Future[Map[Template, ComponentResponse]] = Future.collect {
      blocksWithParams.map {
        entry => {
          val template: Template = entry._1
          val params: Array[String] = entry._2
          val name = params.head
          val args = params.tail
          val request = ComponentRequest(name, args: _*)
          context.service(request).map(response => template -> response)
        }
      }
    }.map(_.toMap)

    futures.map {
      responses => template(Map(handlebars.propertyName -> responses) ++ context.context)
    }
  }

  class ParamExtractor(resolver: HelperResolver, handlebars: H) extends HelperResolver(handlebars) {

    def text() = ""

    def remove(child: Template) = false

    def merge(context: Context, writer: Writer) {}

    override def paramsToString() = resolver.paramsToString()
  }

}

