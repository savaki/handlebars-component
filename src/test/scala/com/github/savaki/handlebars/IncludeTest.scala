package com.github.savaki.handlebars

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.github.jknack.handlebars.io.FileTemplateLoader
import java.net.URI
import com.github.jknack.handlebars.internal.ComponentTemplate
import com.twitter.util.Future

/**
 * @author matt.ho@gmail.com
 */
class IncludeTest extends FlatSpec with ShouldMatchers {
  "included template" should "import underlying content" in {
    val handlebars = new Handlebars(new FileTemplateLoader("src/main/webapp"))
    handlebars.addToPackages(getClass.getPackage.getName)

    val template: ComponentTemplate = handlebars.compile(new URI("include"))
    val htmlFuture: Future[String] = template.render()
    val html: String = htmlFuture.get()

    // from sample.hbs
    html should include("hello matt")
    html should include("before")
    html should include("after")

    // from include.hbs
    html should include("the time has come the walrus said")
  }

  it should "handle multiple levels of includes" in {
    val handlebars = new Handlebars(new FileTemplateLoader("src/main/webapp"))
    handlebars.addToPackages(getClass.getPackage.getName)

    val template: ComponentTemplate = handlebars.compile(new URI("multilevel_include"))
    val htmlFuture: Future[String] = template.render()
    val html: String = htmlFuture.get()

    // from sample.hbs
    html should include("hello matt")
    html should include("before")
    html should include("after")

    // from include.hbs
    html should include("the time has come the walrus said")
  }
}
