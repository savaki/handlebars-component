package com.github.savaki.handlebars

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers
import com.github.jknack.handlebars.io.ClassTemplateLoader

/**
 * @author matt.ho@gmail.com
 */
class ComponentServiceTest extends FlatSpec with ShouldMatchers {
  "ComponentService" should "dynamically instantiate new packages" in {
    val service = new Handlebars(new ClassTemplateLoader())
    service.addToPackages(this.getClass.getPackage.getName)
    SampleComponent.newInstanceCount = 0

    service(ComponentRequest(service.buildContext(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1)

    service(ComponentRequest(service.buildContext(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1) // no new instances created

    service(ComponentRequest(service.buildContext(), "SampleComponent")).get()
    SampleComponent.newInstanceCount should be(1) // no new instances created
  }

  it should "render content" in {
    val service = new Handlebars(new ClassTemplateLoader())
    service.addToPackages(this.getClass.getPackage.getName)
    service(ComponentRequest(service.buildContext(), "SampleComponent")).get()
  }
}


