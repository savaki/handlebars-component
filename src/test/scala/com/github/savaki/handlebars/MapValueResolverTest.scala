package com.github.savaki.handlebars

import org.scalatest.FlatSpec
import org.scalatest.matchers.ShouldMatchers

/**
 * @author matt.ho@gmail.com
 */
class MapValueResolverTest extends FlatSpec with ShouldMatchers {
  "MapValueResolver" should "handle scala Map" in {
    val expectedKey: String = "hello"
    val expectedValue: String = "world"
    val map = Map[String, Any](expectedKey -> expectedValue)
    val resolver = new MapValueResolver()
    resolver.resolve(map, expectedKey) should be(expectedValue)
  }
}
