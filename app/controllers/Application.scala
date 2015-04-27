package controllers

import model.Model._
import play.api.libs.json._
import play.api.mvc._
import service.{Service, ServiceV1, ServiceV2}

import scala.concurrent.Future

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  //NOTE: this currently doesn't return the same header as received as content type, but we could do that by applying a Global filter
  // or composing actions
  def user(id: Int) = VAction { vrequest =>
    val user = vrequest.service.getUser(id)
    val json = Json.toJson(user)
    Ok(json)
  }

}


class VersionRequest[A](val service: Service, request: Request[A]) extends WrappedRequest[A](request)

object VAction extends ActionBuilder[VersionRequest] with ActionTransformer[Request, VersionRequest] {

  def transform[A](request: Request[A]) = Future.successful {
    System.out.println("transform")
    val service: Service = request.headers.get("Accept").collect {
      case VHeaders.V1 => new ServiceV1
      case VHeaders.V2 => new ServiceV2
    }.getOrElse(new ServiceV1)

    new VersionRequest(service, request)
  }
}

object VHeaders {
  val V1 = "application/vnd.company.app.user-v1+json"
  val V2 = "application/vnd.company.app.user-v2+json"
}
