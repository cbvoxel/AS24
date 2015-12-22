package models

import java.time.Instant
import java.util.Date

import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBIgnore, DynamoDBAttribute, DynamoDBHashKey, DynamoDBTable}

@DynamoDBTable(tableName="car_adverts")
class CarAdvertDynamoDb {
  private var id: Int = 0
  private var title: String = ""
  private var fuel: String = "gasoline"
  private var price: Int = 0
  private var isNew: Boolean = true
  private var mileage: Int = 0
  private var firstRegistration: Date = Date.from(Instant.now())

  @DynamoDBHashKey(attributeName="Id")
  def getId = id
  def setId(id: Int) = this.id = id

  @DynamoDBAttribute(attributeName="Title")
  def getTitle = title
  def setTitle(title: String) = this.title = title

  @DynamoDBAttribute(attributeName="Fuel")
  def getFuel = fuel
  def setFuel(fuel: String) = this.fuel = fuel

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
}
