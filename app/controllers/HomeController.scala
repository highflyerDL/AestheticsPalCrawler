package controllers

import javax.inject._

import dao.ExerciseRepository
import models.{Exercise, ExerciseResponse, ResultResponse}
import play.api._
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}
import play.api.mvc._
import models.Formatters._

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(ws: WSClient, eRepo: ExerciseRepository) extends Controller {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def crawl = Action {
    var initPage:Int = 1
    doTheCrawl
    def doTheCrawl: Unit = {
      doCrawl(initPage) onComplete {
        case Success(result) => initPage+=1; doTheCrawl
        case Failure(err) => println(err)
      }
    }
    Ok("ok")
  }

  def doCrawl(pageNumber:Int):Future[String] = {
    val p:Promise[String] = Promise()
    val url = "https://wger.de/api/v2/exercise/"
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest =
      request.withHeaders("Accept" -> "application/json")
        .withQueryString("page" -> pageNumber.toString)
        .withQueryString("language" -> "2")
    val futureResponse: Future[WSResponse] = complexRequest.get()
    implicit val exerciseReader = Json.reads[ExerciseResponse]
    futureResponse onComplete {
      case Success(res) =>
        val json = Json.parse(res.body)
        val exerciseFromJson: JsResult[ExerciseResponse] = Json.fromJson[ExerciseResponse](json)
        exerciseFromJson match {
          case JsSuccess(e: ExerciseResponse, path: JsPath) =>  p.success(e.next)
          case e: JsError => println("Errors: " + JsError.toJson(e).toString()); p.failure(null)
        }
        val result = (json \ "results").as[Seq[ResultResponse]]
        result.foreach(exerciseObject => {
          val e = Exercise(exerciseObject.id, exerciseObject.name, exerciseObject.description,
            exerciseObject.muscles, exerciseObject.muscles_secondary, List())
          //          val e = Exercise(1, "a", Some("sd"), List(), List(1), null)
          val a = eRepo.insert(e)
          a onComplete{
            case Success(posts) => println(posts);
            case Failure(t) => println("An error has occured: " + t.printStackTrace());
          }
        }
        )
      case Failure(err) => println(err.getMessage);
    }
    p.future
  }
}
