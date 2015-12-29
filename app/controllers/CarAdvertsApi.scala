package controllers

import javax.inject.Inject

import logic.DynamoDbRepository
import models.CarAdvertDynamoDb
import play.api.libs.json.JsValue
import play.api.mvc._

class CarAdvertsApi @Inject() (repository: DynamoDbRepository) extends Controller  {

  def getList = Action { request =>
    val allCarAdverts = repository.getList.map(_.toCarAdvert)
    val sortByOption = request.getQueryString("sortby").map(_.toLowerCase)
    var sortByResultHeaderEntry = ""
    val sortedListOfCarAdverts = sortByOption match {
      case Some("fuel") =>
        sortByResultHeaderEntry = "fuel"
        allCarAdverts.sortBy(_.fuel)
      case Some("isnew") =>
        sortByResultHeaderEntry = "isnew"
        allCarAdverts.sortBy(_.isNew)
      case Some("mileage") =>
        sortByResultHeaderEntry = "mileage"
        allCarAdverts.sortBy(_.mileage)
      case Some("price") =>
        sortByResultHeaderEntry = "price"
        allCarAdverts.sortBy(_.price)
      case Some("title") =>
        sortByResultHeaderEntry = "title"
        allCarAdverts.sortBy(_.title)
      case Some("firstregistration") =>
        sortByResultHeaderEntry = "firstregistration"
        allCarAdverts.sortBy(_.firstRegistration)
      case Some("id") =>
        sortByResultHeaderEntry = "id"
        allCarAdverts.sortBy(_.id)
      case None =>
        sortByResultHeaderEntry = "id"
        allCarAdverts.sortBy(_.id)
      case Some(x) =>
        sortByResultHeaderEntry = "warning_not_known_" + x
        allCarAdverts.sortBy(_.id)
    }
    Ok(sortedListOfCarAdverts.map(_.toJson).mkString("[ ", ", ", " ]"))
      .withHeaders("SortBy-Key-Used" -> sortByResultHeaderEntry)
      .withHeaders("Content-Type" -> "application/json")
  }

  def getById(id: String) = Action {
    val queryResult = repository.getById(id)
    if(queryResult.isEmpty) {
      NotFound.withHeaders("Content-Type" -> "application/json")
    } else {
      Ok(queryResult.get.toCarAdvert.toJson)
        .withHeaders("Content-Type" -> "application/json")
    }
  }

  def removeById(id: String) = Action {
    repository.removeById(id)
    Ok.withHeaders("Content-Type" -> "application/json")
  }

  def updateById(id: String) = Action { request => updateActionBody(request, id) }

  private def updateActionBody(request: Request[AnyContent], id: String): Result = {
    val jsonBody = request.body.asJson
    if(jsonBody.isEmpty) {
      return BadRequest.withHeaders("Content-Type" -> "application/json")
    }
    val entryOption = repository.getById(id)
    if(entryOption.isDefined) {
      val entry = populate(entryOption.get)(jsonBody.get)
      onValidationPass(entry){
        repository.update(entry)
        Ok(entry.toCarAdvert.toJson)
          .withHeaders("Content-Type" -> "application/json")
      }
    } else {
      NotFound.withHeaders("Content-Type" -> "application/json")
    }
  }

  def add = Action(BodyParsers.parse.json) { request =>
    val entry = populate(CarAdvertDynamoDb())(request.body)
    onValidationPass(entry) {
      repository.update(entry)
      Ok(entry.toCarAdvert.toJson)
        .withHeaders("Content-Type" -> "application/json")
    }
  }

  def populate(entry: CarAdvertDynamoDb)(jsValue: JsValue) : CarAdvertDynamoDb = {
    val title = jsValue \ "title" toOption
    val fuel = jsValue \ "fuel" toOption
    val price = jsValue \ "price" toOption
    val isNew = jsValue \ "isNew" toOption
    val mileage = jsValue \ "mileage" toOption
    val firstRegistration = jsValue \ "firstRegistration" toOption

    if (title.isDefined) entry.setTitle(title.get.as[String])
    if (fuel.isDefined) entry.setFuel(fuel.get.as[String])
    if (price.isDefined) entry.setPrice(price.get.as[Int])
    if (isNew.isDefined) entry.setIsNew(isNew.get.as[Boolean])
    if (mileage.isDefined) entry.setMileage(mileage.get.as[Int])
    if (firstRegistration.isDefined) {
      val date = firstRegistration.get.as[String]
      entry.setFirstRegistration(date)
    }

    entry
  }

  def onValidationPass(entry: CarAdvertDynamoDb)(onSuccess: => Result) : Result = {
    val validationErrors = entry.validate
    if(validationErrors.isEmpty) {
      onSuccess
    } else {
      BadRequest(s"""{ "errors":${validationErrors.mkString("[ ", ", ", " ]")} }""")
        .withHeaders("Content-Type" -> "application/json")
    }
  }
}
