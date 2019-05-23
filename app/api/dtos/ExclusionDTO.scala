package api.dtos

import java.util.Date

import api.services.Criteria.Criteria
import api.services.DayType.DayType
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class ExclusionDTO(
  exclusionId: String,
  taskId: String,
  exclusionDate: Option[Date] = None,
  day: Option[Int] = None,
  dayOfWeek: Option[Int] = None,
  dayType: Option[DayType] = None,
  month: Option[Int] = None,
  year: Option[Int] = None,
  criteria: Option[Criteria] = None)

object ExclusionDTO {

  /**
   * Implicit that defines how a TaskDTO is written and read.
   */

  implicit val exclusionReads: Reads[ExclusionDTO] = (
    (JsPath \ "exclusionId").read[String] and
    (JsPath \ "taskId").read[String] and
    (JsPath \ "exclusionDate").readNullable[Date] and
    (JsPath \ "day").readNullable[Int] and
    (JsPath \ "dayOfWeek").readNullable[Int] and
    (JsPath \ "dayType").readNullable[DayType] and
    (JsPath \ "month").readNullable[Int] and
    (JsPath \ "year").readNullable[Int] and
    (JsPath \ "criteria").readNullable[Criteria])(ExclusionDTO.apply _)

  implicit val exclusionFormat: OWrites[ExclusionDTO] = Json.writes[ExclusionDTO]
}
