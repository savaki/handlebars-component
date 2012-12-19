package com.github.savaki.handlebars

import com.twitter.util.Future

/**
 * @author matt.ho@gmail.com
 */
trait Component {
  def apply(request: ComponentRequest): Future[ComponentResponse]
}
