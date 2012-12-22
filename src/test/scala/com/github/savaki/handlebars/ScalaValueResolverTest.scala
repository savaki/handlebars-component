package com.github.savaki.handlebars

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author matt.ho@gmail.com
 */
class ScalaValueResolverTest extends FlatSpec with ShouldMatchers {
  "ScalaValueResolver" should "resolve scala objects" in {
    val expectedValue: String = "matt"
    val sample = new ScalaObject
    sample.name = expectedValue
    val resolver = new ScalaValueResolver
    resolver.resolve(sample, "name") should be(expectedValue)
  }
}

class ScalaObject {
  var name: String = null
}
