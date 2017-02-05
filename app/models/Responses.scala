package models

import play.api.libs.json.Json

/**
  * Created by sharius on 1/28/17.
  */

case class ResultResponse(id: Int, license_author: String, status: String,
                          description: Option[String], name: String, name_original: String,
                          creation_date: Option[String], uuid: String, license: Int, category: Int,
                          language: Int, muscles: List[Int], muscles_secondary: List[Int],
                          equipment: List[Int])

case class ExerciseResponse(count:Int, next: String, previous: Option[String], results: List[ResultResponse])

object Formatters {
  implicit val resultFormat = Json.format[ResultResponse]
  implicit val exerciseFormat = Json.format[ExerciseResponse]
}