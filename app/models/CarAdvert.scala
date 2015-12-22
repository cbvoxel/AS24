package models

import java.util.Date
import play.api.libs.json._

case class CarAdvert(id: String,
                     title: String,
                     fuel: String,
                     price: Int,
                     isNew: Boolean,
                     mileage: Int,
                     firstRegistration: Date) {
  def toCarAdvertDynamoDb : CarAdvertDynamoDb = {
    val carAdvert = new CarAdvertDynamoDb()
    carAdvert.setId(id)
    carAdvert.setTitle(title)
    carAdvert.setFuel(fuel)
    carAdvert.setPrice(price)
    carAdvert.setIsNew(isNew)
    carAdvert.setMileage(mileage)
    carAdvert.setFirstRegistration(firstRegistration)
    carAdvert
  }

  def toJson = {
    implicit val locationWrites = new Writes[CarAdvert] {
      def writes(carAdvert: CarAdvert) = Json.obj(
        "id" -> carAdvert.id,
        "title" -> carAdvert.title,
        "fuel" -> carAdvert.fuel,
        "price" -> carAdvert.price,
        "isNew" -> carAdvert.isNew,
        "mileage" -> carAdvert.mileage,
        "firstRegistration" -> carAdvert.firstRegistration.toString
      )
    }

    Json.toJson(this)
  }
}


