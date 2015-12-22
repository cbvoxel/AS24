import java.time.Instant
import java.util.Date

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior
import com.amazonaws.services.dynamodbv2.datamodeling.{DynamoDBMapperConfig, DynamoDBSaveExpression, DynamoDBMapper}
import models.CarAdvertDynamoDb
// Create or Update
val carAdvert = CarAdvertDynamoDb("1d4c551c-c3e1-4fff-97a3-de630a89ac35")
carAdvert.setTitle("Audi A4 Avant")
carAdvert.setFuel("gasoline")
carAdvert.setPrice(8644)
carAdvert.setIsNew(false)
carAdvert.setMileage(45233)
carAdvert.setFirstRegistration(Date.from(Instant.parse("2010-08-30T00:00:00Z")))
carAdvert.toString
// Setup AWS DynamoDB Communication
val profile = new ProfileCredentialsProvider
profile.getCredentials.getAWSSecretKey
profile.getCredentials.getAWSAccessKeyId
val client = new AmazonDynamoDBClient(profile)
client.setRegion(Region.getRegion(Regions.EU_CENTRAL_1))
val mapper = new DynamoDBMapper(client)
//val mapperConfig = new DynamoDBMapperConfig(SaveBehavior.UPDATE)
// Save or Update
mapper.save(carAdvert)
// Load
val loaded = mapper.load(CarAdvertDynamoDb("1d4c551c-c3e1-4fff-97a3-de630a89ac35"))
loaded.toString