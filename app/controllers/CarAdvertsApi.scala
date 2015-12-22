package controllers

import javax.inject.Inject

import logic.DynamoDbRepository
import play.api._
import play.api.mvc._

class CarAdvertsApi @Inject() (repository: DynamoDbRepository) extends Controller  {
  def list = Action {
    Ok
  }
}
