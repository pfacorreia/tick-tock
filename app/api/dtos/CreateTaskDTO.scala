package api.dtos

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.{Date, TimeZone, UUID}

import database.utils.DatabaseUtils._
import api.services.PeriodType.PeriodType
import api.services.SchedulingType.SchedulingType
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import database.repositories.FileRepositoryImpl

import scala.concurrent.{ExecutionContext, Future}

case class CreateTaskDTO(
                     fileName: String,
                     taskType: SchedulingType,
                     startDateAndTime: Option[String] = None,
                     periodType: Option[PeriodType] = None,
                     period: Option[Int] = None,
                     endDateAndTime: Option[String] = None,
                     occurrences: Option[Int] = None
                   )

object CreateTaskDTO {

  implicit val ec = ExecutionContext.global

  /*/**
    * Method that constructs the TaskDTO giving strings as dates and making the date format validation and conversion from string to date.
    * @param startDateAndTime Date and time of when the task is executed in a String format.
    * @param fileName Name of the file that is executed.
    * @return the taskDTO if the date received is valid. Throws an IllegalArgumentException if it's invalid.
    */
  def construct(startDateAndTime: String, fileName: String, taskType: SchedulingType, periodType: Option[PeriodType], period: Option[Int], endDateAndTime: Option[String], occurrences: Option[Int]): CreateTaskDTO = {
    //val date = getValidDate(startDateAndTime).get
    val startDate = stringToDateFormat(startDateAndTime, "yyyy-MM-dd HH:mm:ss")
    if(endDateAndTime.isDefined){

      CreateTaskDTO.apply(startDate, fileName, taskType, periodType, period, Some(stringToDateFormat(endDateAndTime.get, "yyyy-MM-dd HH:mm:ss")), occurrences)
    }
    else CreateTaskDTO.apply(startDate, fileName, taskType, periodType, period, None, occurrences)

  }*/

  /**
    * Implicit that defines how a CreateTaskDTO is read from the JSON request.
    * This implicit is used on the TaskController when Play's "validate" method is called.
    */
  implicit val createTaskReads: Reads[CreateTaskDTO] = (
      (JsPath \ "fileName").read[String] and
      (JsPath \ "taskType").read[String] and
      (JsPath \ "startDateAndTime").readNullable[String] and
      (JsPath \ "periodType").readNullable[String] and
      (JsPath \ "period").readNullable[Int] and
      (JsPath \ "endDateAndTime").readNullable[String] and
      (JsPath \ "occurrences").readNullable[Int]
    ) (CreateTaskDTO.apply _)

  /**
    * Implicit that defines how a CreateTaskDTO is written to a JSON format.
    */
  implicit val createTaskFormat: OWrites[CreateTaskDTO] = Json.writes[CreateTaskDTO]
}
