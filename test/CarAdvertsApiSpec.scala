import logic.DynamoDbRepository
import models.CarAdvertDynamoDb
import org.junit.runner._
import org.specs2.mock._
import org.specs2.mutable._
import org.specs2.runner._
import play.Logger
import play.api.inject.bind
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class CarAdvertsApiSpec extends Specification with Mockito {

  val firstAdvert = CarAdvertDynamoDb("1d4c551c-c3e1-4fff-97a3-de630a89ac35")
  firstAdvert.setTitle("Audi A4 Avant")
  firstAdvert.setFuel("gasoline")
  firstAdvert.setPrice(8644)
  firstAdvert.setIsNew(false)
  firstAdvert.setMileage(45233)
  firstAdvert.setFirstRegistration("2010-08-30")
  val secondadvert = CarAdvertDynamoDb("4a0d32bf-25c9-4bf5-9b31-ed6e5e6415e7")
  secondadvert.setTitle("BMW 116d Schwarz")
  secondadvert.setFuel("diesel")
  secondadvert.setPrice(28644)
  secondadvert.setIsNew(true)

  val dynamoDbRepositoryMock = mock[DynamoDbRepository]
  dynamoDbRepositoryMock.getList returns Array( firstAdvert, secondadvert )
  dynamoDbRepositoryMock.getById("1d4c551c-c3e1-4fff-97a3-de630a89ac35") returns Some(firstAdvert)
  dynamoDbRepositoryMock.getById("4a0d32bf-25c9-4bf5-9b31-ed6e5e6415e7") returns Some(secondadvert)
  dynamoDbRepositoryMock.getById("wrongid") returns None

  implicit lazy val fakeApplication = new GuiceApplicationBuilder()
    .overrides(bind[DynamoDbRepository].to(dynamoDbRepositoryMock))
    .build

  "CarAdvertsApi" should {

    "send 404 on a bad request" in new WithApplication(fakeApplication) {
      val nope = route(FakeRequest(GET, "/boum")).get
      status(nope) must equalTo(NOT_FOUND)
    }

    "GET returns a (implicitly) sorted (id) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "id")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.id)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (id) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=id")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "id")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.id)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (title) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=title")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "title")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.title)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (fuel) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=fuel")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "fuel")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.fuel)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (price) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=price")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "price")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.price)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (isNew) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=isnew")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "isnew")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.isNew)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (mileage) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=mileage")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "mileage")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.mileage)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (firstRegistration) list of json car adverts" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=firstregistration")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "firstregistration")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.firstRegistration)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET returns a sorted (id) list of json car adverts with sort-warning" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/?sortby=whatisthis")).get

      status(home) must equalTo(OK)
      headers(home) must havePair("SortBy-Key-Used", "warning_not_known_whatisthis")
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (
        Array( firstAdvert.toCarAdvert, secondadvert.toCarAdvert )
          .sortBy(_.id)
          .map(_.toJson)
          .mkString("[ ", ", ", " ]")
      )
    }

    "GET with Id returns a car advert with valid id" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/1d4c551c-c3e1-4fff-97a3-de630a89ac35")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain (firstAdvert.toCarAdvert.toJson.toString)
    }

    "GET with Id returns a car advert with invalid id" in new WithApplication(fakeApplication) {
      val home = route(FakeRequest(GET, "/adverts/wrongid")).get

      status(home) must equalTo(NOT_FOUND)
    }

    "Put returns OK for valid data" in new WithApplication(fakeApplication) {
      val contentData = """"title":"Test","fuel":"gasoline","price":30000,"isNew":true"""
      val home = route(
        FakeRequest(PUT, "/adverts/")
          .withHeaders("Content-Type" -> "application/json")
          .withBody(s"{$contentData} ")
      ).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")
      contentAsString(home) must contain(contentData)
    }

    "Put returns BAD_REQUEST for incomplete data" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(PUT, "/adverts/")
          .withHeaders("Content-Type" -> "application/json")
          .withBody("""{"title":"Test"}""")
      ).get

      status(home) must equalTo(BAD_REQUEST)
      contentType(home) must beSome.which(_ == "application/json")
    }

    "Put returns BAD_REQUEST for empty data" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(PUT, "/adverts/")
          .withHeaders("Content-Type" -> "application/json")
          .withBody("""""")
      ).get

      status(home) must equalTo(BAD_REQUEST)
    }

    "Put returns BAD_REQUEST for invalid data" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(PUT, "/adverts/")
          .withHeaders("Content-Type" -> "application/json")
          .withBody("""{"title":"Test"}""")
      ).get

      status(home) must equalTo(BAD_REQUEST)
      contentType(home) must beSome.which(_ == "application/json")
    }

    "Post returns OK with modified data for valid data" in new WithApplication(fakeApplication) {
      val contentData = """"title":"Test","fuel":"gasoline","price":30000,"isNew":true"""
      val home = route(
        FakeRequest(POST, "/adverts/4a0d32bf-25c9-4bf5-9b31-ed6e5e6415e7")
          .withHeaders("Content-Type" -> "application/json")
          .withBody(s"{$contentData}")
      ).get
      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "application/json")
    }

    "Post returns BAD_REQUEST for empty data" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(POST, "/adverts/1d4c551c-c3e1-4fff-97a3-de630a89ac35")
//          .withHeaders("Content-Type" -> "application/json")
          .withBody("""""")
      ).get

      status(home) must equalTo(BAD_REQUEST)
      contentType(home) must beSome.which(_ == "application/json")
    }

    "Post returns BAD_REQUEST for invalid data" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(POST, "/adverts/1d4c551c-c3e1-4fff-97a3-de630a89ac35")
          .withHeaders("Content-Type" -> "application/json")
          .withBody("""{"isNew":true}""")
      ).get

      status(home) must equalTo(BAD_REQUEST)
      contentType(home) must beSome.which(_ == "application/json")
    }

    "Post returns NOT_FOUND for unknown id" in new WithApplication(fakeApplication) {
      val home = route(
        FakeRequest(POST, "/adverts/wrongid")
          .withHeaders("Content-Type" -> "application/json")
          .withBody("""{"title":"Test"}""")
      ).get

      status(home) must equalTo(NOT_FOUND)
    }
  }
}
