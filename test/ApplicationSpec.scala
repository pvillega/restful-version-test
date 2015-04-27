import controllers.VHeaders
import model.Model.User
import org.junit.runner._
import org.specs2.matcher.MatchResult
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import service.{ServiceV1, ServiceV2}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "get version 1 response with Accept headers" in new WithApplication {
      val user = new ServiceV1().getUser(1)
      validateVersion(VHeaders.V1, user)
    }

    "get version 2 response with Accept headers" in new WithApplication {
      val user = new ServiceV2().getUser(2)
      validateVersion(VHeaders.V2, user)
    }

    def validateVersion(version: String, user: User): MatchResult[String] = {
      val request = FakeRequest(
        method = "GET",
        uri = "/user/1",
        headers = FakeHeaders(
          Seq("Accept" -> Seq(version))
        ),
        body = ""
      )
      val v1 = route(request).get

      status(v1) must equalTo(OK)
      contentAsString(v1) must be_==(Json.toJson(user).toString())
    }
  }
}
