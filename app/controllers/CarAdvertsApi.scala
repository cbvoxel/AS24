package controllers

import java.time.Instant
import java.util.Date
import javax.inject.Inject

import logic.DynamoDbRepository
import models.CarAdvertDynamoDb
import play.api._
import play.api.libs.json.{JsValue, JsPath}
import play.api.mvc._

class CarAdvertsApi @Inject() (repository: DynamoDbRepository) extends Controller  {
  def getList = Action {
    Ok(repository.getList.map(_.toCarAdvert.toJson).mkString("[ ", ", ", " ]"))
  }

  def getById(id: String) = Action {
    val queryResult = repository.getById(id)
    if(queryResult.isEmpty) {
      NotFound
    } else {
      Ok(queryResult.get.toCarAdvert.toJson)
    }
  }

  def removeById(id: String) = Action {
    repository.removeById(id)
    Ok
  }

  def updateById(id: String) = Action(BodyParsers.parse.json) { request =>
    val entryOption = repository.getById(id)
    if(entryOption.isDefined) {
      val entry = populate(entryOption.get)(request.body)
      repository.update(entry)
      Ok(entry.toCarAdvert.toJson)
    } else {
      NotFound
    }
  }

  def add = Action(BodyParsers.parse.json) { request =>
    val entry = populate(CarAdvertDynamoDb())(request.body)
    repository.update(entry)
    Ok(entry.toCarAdvert.toJson)
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
      val date = Date.from(Instant.parse(firstRegistration.get.as[String]))
      entry.setFirstRegistration(date)
    }

    entry
  }
}
