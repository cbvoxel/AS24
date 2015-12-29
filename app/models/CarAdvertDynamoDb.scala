package models

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.format.{DateTimeFormatter, DateTimeParseException}
import java.util.{Date, UUID}

import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBAttribute, DynamoDBHashKey, DynamoDBIgnore, DynamoDBTable}
import play.api.Logger

import scala.collection.mutable.ArrayBuffer

@DynamoDBTable(tableName="car_adverts")
class CarAdvertDynamoDb {
  private var id: String = UUID.randomUUID().toString
  private var title: Option[String] = None
  private var fuel: Option[String] = None
  private var price: Option[Int] = None
  private var isNew: Option[Boolean] = None
  private var mileage: Option[Int] = None
  private var firstRegistration: Option[String] = None

  @DynamoDBHashKey(attributeName="id")
  def getId = id
  def setId(id: String) = {
    // check to see if id is a valid UUID
    this.id = UUID.fromString(id).toString
  }

  @DynamoDBAttribute(attributeName="Title")
  def getTitle = title.getOrElse("")
  def setTitle(title: String) : Unit = this.title = Some(title)

  @DynamoDBAttribute(attributeName="Fuel")
  def getFuel = fuel.getOrElse("")
  def setFuel(fuel: String) : Unit = {
    if(!Seq("gasoline","diesel").contains(fuel)) {
      throw new UnsupportedOperationException("Either use gasoline or diesel (lower case).")
    }
    this.fuel = Some(fuel)
  }

  @DynamoDBAttribute(attributeName="Price")
  def getPrice = price.getOrElse(0)
  def setPrice(price: Int) : Unit = this.price = Some(price)

  @DynamoDBAttribute(attributeName="IsNew")
  def getIsNew = isNew.getOrElse(false)
  def setIsNew(isNew: Boolean) : Unit = {
    this.isNew = Some(isNew)
  }

  @DynamoDBAttribute(attributeName="Mileage")
  def getMileage = mileage.getOrElse(0)
  def setMileage(mileage: Int) : Unit = {
    if(mileage == 0) this.mileage = None
    else this.mileage = Some(mileage)
  }

  @DynamoDBAttribute(attributeName="FirstRegistration")
  def getFirstRegistration = firstRegistration.getOrElse("")
  def setFirstRegistration(firstRegistration: String) : Unit = {
    if(firstRegistration.isEmpty) {
      this.firstRegistration = None
      return
    }
    // Validation
    var date : Date = null
    try {
      val temporal = DateTimeFormatter.ISO_INSTANT.parse(firstRegistration)
      Logger.debug(temporal.toString)
      date = Date.from(Instant.from(temporal))
    } catch {
      case ex: DateTimeParseException =>
        date = new SimpleDateFormat("yyyy-MM-dd").parse(firstRegistration)
    }
    this.firstRegistration = Some(new SimpleDateFormat("yyyy-MM-dd").format(date))
  }

  def validate: Array[String] = {
    val result = ArrayBuffer.empty[String]
    if(title.isEmpty) result += "Required: title is not set!"
    if(fuel.isEmpty) result += "Required: fuel is not set!"
    if(price.isEmpty) result += "Required: price is not set!"
    if(isNew.isEmpty) result += "Required: isNew is not set!"
    if(isNew.isDefined) {
      if(isNew.get && mileage.getOrElse(0) > 0) result += "Mileage cannot be > 0 if isNew is set to true!"
      if(isNew.get && firstRegistration.isDefined) result += "FirstRegistration cannot set if isNew is set to true!"
    }
    result.toArray
  }

  @DynamoDBIgnore
  def toCarAdvert = {
    CarAdvert(id,getTitle,getFuel,getPrice,getIsNew,mileage,firstRegistration)
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
