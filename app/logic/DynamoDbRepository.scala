package logic

import com.google.inject.ImplementedBy
import models.CarAdvertDynamoDb

@ImplementedBy(classOf[DynamoDbRepositoryImpl])
trait DynamoDbRepository {
  def getList : Array[CarAdvertDynamoDb]
  def getById(uuid: String) : Option[CarAdvertDynamoDb]
  def update(entry: CarAdvertDynamoDb) : Unit
  def remove(entry: CarAdvertDynamoDb) : Unit
  def removeById(uuid: String) : Unit
}
