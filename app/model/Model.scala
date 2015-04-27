package model

import play.api.libs.json._

object Model {

  case class User(email: String)

  implicit val userReads = Json.reads[User]
  implicit val userWrites = Json.writes[User]
}
