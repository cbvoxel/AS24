package models

import java.util.Date
import play.api.libs.json._

case class CarAdvert(id: String,
                     title: String,
                     fuel: String,
                     price: Int,
                     isNew: Boolean,
                     mileage: Option[Int],
                     firstRegistration: Option[String]) {
  def toCarAdvertDynamoDb : CarAdvertDynamoDb = {
    val carAdvert = new CarAdvertDynamoDb()
    carAdvert.setId(id)
    carAdvert.setTitle(title)
    carAdvert.setFuel(fuel)
    carAdvert.setPrice(price)
    carAdvert.setIsNew(isNew)
    if(mileage.isDefined) carAdvert.setMileage(mileage.get)
    if(firstRegistration.isDefined) carAdvert.setFirstRegistration(firstRegistration.get)
    carAdvert
  }

  def toJson = {
    implicit val locationWrites = new Writes[CarAdvert] {
      def writes(carAdvert: CarAdvert) = {
        val json = Json.obj(
          "id" -> carAdvert.id,
          "title" -> carAdvert.title,
          "fuel" -> carAdvert.fuel,
          "price" -> carAdvert.price,
          "isNew" -> carAdvert.isNew
        )
        if(carAdvert.mileage.isDefined) json + "mileage" -> carAdvert.mileage.get
        if(carAdvert.firstRegistration.isDefined) json + "firstRegistration" -> carAdvert.firstRegistration.get.toString
        json
      }
    }

    Json.toJson(this)
  }
}


