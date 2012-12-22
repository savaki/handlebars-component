package com.github.savaki.handlebars

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.github.jknack.handlebars.io.ClassTemplateLoader
import com.github.jknack.handlebars.internal.ComponentTemplate

/**
 * @author matt.ho@gmail.com
 */
class ComponentServiceTest extends FlatSpec with ShouldMatchers {
  val service = {
    val s = new Handlebars(new ClassTemplateLoader())
    s.addToPackages(this.getClass.getPackage.getName)
    s
  }

  "ComponentService" should "dynamically instantiate new packages" in {
    SampleComponent.newInstanceCount = 0

    service(ComponentRequest(Map(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1)

    service(ComponentRequest(Map(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1) // no new instances created

    service(ComponentRequest(Map(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1) // no new instances created
  }

  it should "invoke the underlying service" in {
    /**
     * SampleComponent sets one pair in the returned context, name -> matt
     */
    val response: ComponentResponse = service(ComponentRequest(Map(), "SampleComponent")).get()
    response should not(be(null))
    response.model.get("name") should be(Some("matt"))
  }

  it should "pass values from the initial context to the child components" in {
    val expectedName = "matt"
    val hbs =
      """
        |name => {{name}}
      """.stripMargin
    val template: ComponentTemplate = service.compileString(hbs)
    val html: String = template.render(Map("name" -> expectedName)).get()

    html should include("name => %s".format(expectedName))
  }

  it should "override top level values with component values" in {
    val expectedName: String = "frank"
    val otherValue: String = "hello world"
    val hbs =
      """
        |outside => {{name}}
        |{{#render SampleComponent}}
        | inside => {{name}}
        |other => {{other}}
        |{{/render}}
      """.stripMargin
    val template: ComponentTemplate = service.compileString(hbs)
    val html: String = template.render(Map("name" -> expectedName, "other" -> otherValue)).get()
    html should include("outside => %s" format expectedName)
    html should include("inside => %s" format "matt") // result from SampleComponent should override this value
    html should include("other => %s" format otherValue)
  }
}


