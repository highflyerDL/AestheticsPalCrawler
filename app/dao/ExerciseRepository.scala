package dao

import javax.inject._

import models.Exercise
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig, HasDatabaseConfigProvider}
import slick.dbio.DBIOAction
import util.MyPostgresDriver

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by sharius on 1/27/17.
  */
@Singleton
class ExerciseRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends ExerciseTable with HasDatabaseConfigProvider[MyPostgresDriver] {
  import driver.api._

  def insert(exercise: Exercise): Future[Int] = db.run {
    exerciseTableQueryInc += exercise
  }

  def insertAll(exercises: List[Exercise]): Future[Seq[Int]] = db.run {
    exerciseTableQueryInc ++= exercises
  }
}

trait ExerciseTable {
  self: HasDatabaseConfig[MyPostgresDriver] =>

  import driver.api._

  lazy protected val exerciseTableQuery = TableQuery[ExerciseTable]
  lazy protected val exerciseTableQueryInc = exerciseTableQuery returning exerciseTableQuery.map(_.eId)

  class ExerciseTable(_tableTag: Tag) extends Table[Exercise](_tableTag, "exercise") {
    def * = (eId, name, description, muscle, muscleSecondary, image) <> (Exercise.tupled, Exercise.unapply)

    /** Database column e_id SqlType(serial), AutoInc, PrimaryKey */
    val eId = column[Int]("e_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(text) */
    val name = column[String]("name")
    /** Database column description SqlType(text), Default(None) */
    val description = column[Option[String]]("description")
    /** Database column muscle SqlType(_int4), Length(10,false), Default(None) */
    val muscle = column[List[Int]]("muscle")
    /** Database column muscle_secondary SqlType(_int4), Length(10,false), Default(None) */
    val muscleSecondary = column[List[Int]]("muscle_secondary")
    /** Database column image SqlType(_text), Length(2147483647,false), Default(None) */
    val image = column[List[String]]("image")
  }
}
