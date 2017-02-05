package models;

/**
 * Created by sharius on 1/17/17.
 */

case class Exercise(eId: Int, name: String, description: Option[String] = None, muscle: List[Int], muscleSecondary: List[Int], image: List[String])
