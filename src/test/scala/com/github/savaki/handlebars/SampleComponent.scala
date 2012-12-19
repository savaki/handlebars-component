package com.github.savaki.handlebars

import com.twitter.util.Future

/**
 * @author matt.ho@gmail.com
 */
class SampleComponent extends Component {
  SampleComponent.newInstanceCount = SampleComponent.newInstanceCount + 1

  def apply(request: ComponentRequest): Future[ComponentResponse] = {
    println("SampleComponent#apply")
    val response: ComponentResponse = new ComponentResponse
    response.context.put("name", "matt")
    Future.value(response)
  }
}

object SampleComponent {
  var newInstanceCount = 0
}

