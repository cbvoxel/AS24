package models

import java.text.{ParseException, Format, SimpleDateFormat}
import java.time.{LocalDate, Instant}
import java.time.format.{DateTimeParseException, DateTimeFormatter}
import java.time.temporal.{TemporalQuery, ChronoField, IsoFields, TemporalAccessor}
import java.util.{Locale, UUID, Date}

import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBIgnore, DynamoDBAttribute, DynamoDBHashKey, DynamoDBTable}
import play.api.Logger

import scala.collection.mutable.ArrayBuffer

@DynamoDBTable(tableName="car_adverts")
class CarAdvertDynamoDb {
  private var id: String = UUID.randomUUID().toString
  private var title: String = ""
  private var fuel: String = "gasoline"
  private var price: Int = 0
  private var isNew: Boolean = true
  private var mileage: Int = 0
  private var firstRegistration: String = Date.from(Instant.now()).formatted("yyyy-MM-dd")

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
  def setIsNew(isNew: Boolean) = {
    this.isNew = isNew
  }

  @DynamoDBAttribute(attributeName="Mileage")
  def getMileage = mileage
  def setMileage(mileage: Int) = {
    this.mileage = mileage
  }

  @DynamoDBAttribute(attributeName="FirstRegistration")
  def getFirstRegistration = firstRegistration
  def setFirstRegistration(firstRegistration: String) = {
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
    this.firstRegistration = new SimpleDateFormat("yyyy-MM-dd").format(date)
  }

  def validate: Array[String] = {
    val result = ArrayBuffer.empty[String]
    if(mileage > 0 && isNew) {
      result += "Mileage cannot be > 0 if isNew is set to true!"
    }
    result.toArray
  }

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
