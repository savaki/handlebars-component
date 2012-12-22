package com.github.savaki.handlebars

import com.twitter.util.Future

/**
 * @author matt.ho@gmail.com
 */
class SampleComponent extends Component {
  SampleComponent.newInstanceCount = SampleComponent.newInstanceCount + 1

  def apply(request: ComponentRequest): Future[ComponentResponse] = {
    Future.value {
      ComponentResponse(Map("name" -> "matt"))
    }
  }
}

object SampleComponent {
  var newInstanceCount = 0
}

