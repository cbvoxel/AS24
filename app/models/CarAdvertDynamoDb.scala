package models

import java.time.Instant
import java.util.{UUID, Date}

import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBIgnore, DynamoDBAttribute, DynamoDBHashKey, DynamoDBTable}

@DynamoDBTable(tableName="car_adverts")
class CarAdvertDynamoDb {
  private var id: String = UUID.randomUUID().toString
  private var title: String = ""
  private var fuel: String = "gasoline"
  private var price: Int = 0
  private var isNew: Boolean = true
  private var mileage: Int = 0
  private var firstRegistration: Date = Date.from(Instant.now())

  @DynamoDBHashKey(attributeName="id")
  def getId = id
  def setId(id: String) = {
    // check to see if id is a valid UUID
    this.id = UUID.fromString(id).toString
  }

  @DynamoDBAttribute(attributeName="Title")
  def getTitle = title
  def setTitle(title: String) = this.title = title

  @DynamoDBAttribute(attributeName="Fuel")
  def getFuel = fuel
  def setFuel(fuel: String) = {
    if(!Seq("gasoline","diesel").contains(fuel)) {
      throw new UnsupportedOperationException("Either use gasoline or diesel (lower case).")
    }
    this.fuel = fuel
  }

  @DynamoDBAttribute(attributeName="Price")
  def getPrice = price
  def setPrice(price: Int) = this.price = price

  @DynamoDBAttribute(attributeName="IsNew")
  def getIsNew = isNew
  def setIsNew(isNew: Boolean) = this.isNew = isNew

  @DynamoDBAttribute(attributeName="Mileage")
  def getMileage = mileage
  def setMileage(mileage: Int) = this.mileage = mileage

  @DynamoDBAttribute(attributeName="FirstRegistration")
  def getFirstRegistration = firstRegistration
  def setFirstRegistration(firstRegistration: Date) = this.firstRegistration = firstRegistration

  @DynamoDBIgnore
  def toCarAdvert = {
    CarAdvert(id,title,fuel,price,isNew,mileage,firstRegistration)
  }

  override def toString = {
    s"id=$id, title=$title, fuel=$fuel, price=$price, isNew=$isNew, mileage=$mileage, firstRegistration=$firstRegistration"
  }
}

object CarAdvertDynamoDb{
  def apply(id: String) = {
    val carAdvert = new CarAdvertDynamoDb()
    carAdvert.setId(id)
    carAdvert
  }
  def apply() = new CarAdvertDynamoDb()
}
