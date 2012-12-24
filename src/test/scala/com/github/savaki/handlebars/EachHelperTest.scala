package com.github.savaki.handlebars

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.github.jknack.handlebars.io.ClassTemplateLoader
import com.github.jknack.handlebars.internal.ComponentTemplate

/**
 * @author matt.ho@gmail.com
 */
class EachHelperTest extends FlatSpec with ShouldMatchers {
  val service = {
    val s = new Handlebars(new ClassTemplateLoader())
    s.addToPackages(this.getClass.getPackage.getName)
    s
  }

  val hbs =
    """
      |{{#each names}}
      |name: {{name}}
      |{{/each}}
    """.stripMargin

  val template: ComponentTemplate = service.compileString(hbs)
  val names = List("alice", "bob", "charles")

  "EachHelper" should "iterate over scala List[_]" in {
    val items = names.map(name => Map("name" -> name)).toList
    val html: String = template.render(Map("names" -> items)).get()
    names.foreach {
      name => html should include("name: %s" format name)
    }
  }

  it should "iterate over Seq[_]" in {
    val items = names.map(name => Map("name" -> name)).toSeq
    val html: String = template.render(Map("names" -> items)).get()
    names.foreach {
      name => html should include("name: %s" format name)
    }
  }

  it should "iterate over Array[_]" in {
    val items = names.map(name => Map("name" -> name)).toArray
    val html: String = template.render(Map("names" -> items)).get()
    names.foreach {
      name => html should include("name: %s" format name)
    }
  }

  it should "iterate over java.util.List[_]" in {
    val items = new java.util.ArrayList[AnyRef]()
    names.foreach {
      name => items.add(Map("name" -> name))
    }

    val html: String = template.render(Map("names" -> items)).get()
    names.foreach {
      name => html should include("name: %s" format name)
    }
  }

  it should "handle null context values" in {
    val html: String = template.render().get()
    html should not(be(null))
  }
}
