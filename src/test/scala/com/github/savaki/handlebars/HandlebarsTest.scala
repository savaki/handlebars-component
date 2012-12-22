package com.github.savaki.handlebars

import com.github.jknack.handlebars.io.FileTemplateLoader
import java.net.URI
import com.twitter.util.Future
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec
import com.github.jknack.handlebars.internal.ComponentTemplate

/**
 * @author matt.ho@gmail.com
 */
class HandlebarsTest extends FlatSpec with ShouldMatchers {
  "Handlebars" should "render template with component" in {
    val service = new Handlebars(new FileTemplateLoader("src/main/webapp"))
    service.addToPackages(this.getClass.getPackage.getName)

    val template: ComponentTemplate = service.compile(new URI("sample"))
    val htmlFuture: Future[String] = template.render()
    val html: String = htmlFuture.get()

    html should include("hello %s" format "matt")
    html should include("before")
    html should include("after")
  }
}

