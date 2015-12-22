package models

import java.util.Date

case class CarAdvert(id: Int,
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
}


