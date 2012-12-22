package com.github.jknack.handlebars.internal

import com.github.jknack.handlebars.{Context, Template, Handlebars => H}
import scala.collection.JavaConversions._
import com.twitter.util.Future
import java.io.Writer
import com.github.savaki.handlebars.{Handlebars, ComponentRequest}
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

  /**
   * recursively go through and find blocks that reference our magic handlebars helper
   *
   * @param template the root template to search from
   * @return the list of blocks that utilize our magic help
   */
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

  /**
   * wrap the specified object in a handlebars context
   *
   * @param model the object being wrapped
   * @return
   */
  private def newContext(model: AnyRef): Context = {
    Context.newBuilder(model).resolver(handlebars.resolvers: _*).build()
  }

  def render(model: Map[String, Any] = Map()): Future[String] = {
    val futures: Future[Map[Template, Context]] = Future.collect {
      blocksWithParams.map {
        entry => {
          val template: Template = entry._1
          val params: Array[String] = entry._2
          val name = params.head
          val args = params.tail
          val request = ComponentRequest(model, name, args: _*)
          handlebars(request).map {
            response => template -> newContext(response.model)
          }
        }
      }
    }.map(_.toMap)

    futures.map {
      responses => {
        val context = newContext(Map(handlebars.propertyName -> responses) ++ model)
        template(context)
      }
    }
  }

  class ParamExtractor(resolver: HelperResolver, handlebars: H) extends HelperResolver(handlebars) {

    def text() = ""

    def remove(child: Template) = false

    def merge(context: Context, writer: Writer) {}

    override def paramsToString() = resolver.paramsToString()
  }

}

