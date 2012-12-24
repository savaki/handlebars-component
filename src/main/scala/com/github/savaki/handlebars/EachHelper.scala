package com.github.savaki.handlebars

import com.github.jknack.handlebars.{Options, Helper}
import scala.collection.JavaConversions._
import java.lang.Iterable
import org.apache.commons.lang3.StringUtils

/**
 * @author matt.ho@gmail.com
 */
class EachHelper extends Helper[Object] {
  def apply(context: Object, options: Options): CharSequence = {
    if (context == null) {
      return StringUtils.EMPTY
    }

    val buffer: StringBuilder = new StringBuilder(512)

    val elements: Traversable[AnyRef] = context match {
      case i: Iterable[_] => i.toSeq.asInstanceOf[Seq[AnyRef]]
      case a: Array[_] => a.toSeq.asInstanceOf[Seq[AnyRef]]
      case t: Traversable[_] => t.asInstanceOf[Seq[AnyRef]]
      case _ => throw new RuntimeException("unable to traverse type, %s" format context.getClass.getCanonicalName)
    }

    elements.foreach {
      element => {
        val context = Handlebars.newContext(element)
        buffer.append(options.fn.apply(context))
      }
    }

    buffer.toString()
  }
}
