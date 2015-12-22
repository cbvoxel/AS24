package logic

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.{Regions, Region}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.{ScanResultPage, DynamoDBScanExpression, DynamoDBMapper}
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.ScanResult
import models.CarAdvertDynamoDb

import scala.collection.mutable.ArrayBuffer

class DynamoDbRepository {
  // Setup AWS DynamoDB Communication
  val profile = new ProfileCredentialsProvider
  profile.getCredentials.getAWSSecretKey
  profile.getCredentials.getAWSAccessKeyId
  val client = new AmazonDynamoDBClient(profile)
  client.setRegion(Region.getRegion(Regions.EU_CENTRAL_1))
  val mapper = new DynamoDBMapper(client)

  def getList : Array[CarAdvertDynamoDb] = {
    val result = ArrayBuffer.empty[CarAdvertDynamoDb]
    var scanResult = mapper.scanPage(classOf[CarAdvertDynamoDb], new DynamoDBScanExpression() )
    result ++= scanResult.getResults.toArray.map(_.asInstanceOf[CarAdvertDynamoDb])
    while(scanResult.getLastEvaluatedKey != null) {
      scanResult = mapper.scanPage(
        classOf[CarAdvertDynamoDb],
        new DynamoDBScanExpression().withExclusiveStartKey(scanResult.getLastEvaluatedKey)
      )
      result ++= scanResult.getResults.toArray.map(_.asInstanceOf[CarAdvertDynamoDb])
    }
    result.toArray
  }
}
