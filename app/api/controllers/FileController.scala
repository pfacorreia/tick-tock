package api.controllers

import java.io.File
import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.util.UUID

import javax.inject.Singleton
import org.apache.commons.io.FilenameUtils
import play.api.mvc._
import javax.inject.Inject
import play.api.libs.json._
import java.util.UUID._

import api.dtos.FileDTO
import api.utils.DateUtils._
import api.utils.UUIDGenerator

import scala.concurrent.{ExecutionContext, Future}
import database.repositories.{FileRepository, TaskRepository, TaskRepositoryImpl}
import api.validators.Error._

@Singleton
class FileController @Inject()(cc: ControllerComponents)(implicit exec: ExecutionContext, implicit val fileRepo: FileRepository, implicit val taskRepo: TaskRepository, implicit val UUIDGen: UUIDGenerator) extends AbstractController(cc) {

  def index = Action {
    Ok("It works!")
  }

  def upload: Action[AnyContent] = Action.async { request =>
    request.body.asMultipartFormData.get.file("file").map{
      file =>
        if(FilenameUtils.getExtension(file.filename) == "jar") {
          val uuid = UUIDGen.generateUUID
          val fileName = request.body.asMultipartFormData.get.dataParts.head._2.head
          val uploadDate = getCurrentDateTimestamp
          fileRepo.existsCorrespondingFileName(fileName).map{elem =>
            if(elem) Future.successful(BadRequest(Json.toJsObject(invalidUploadFileName)))
          }
          val initialFilePath = Paths.get(s"C:/Users/Pedro/Desktop/$uuid")
          val finalFilePath = Paths.get(s"app/filestorage/$uuid" + ".jar")
          file.ref.moveTo(initialFilePath, replace = false)
          Files.move(initialFilePath, finalFilePath, StandardCopyOption.ATOMIC_MOVE)
          fileRepo.insertInFilesTable(FileDTO(uuid, fileName, uploadDate))
          Future.successful(Ok("File uploaded successfully => fileId: " + uuid + ", fileName: " + fileName + ", uploadDate: " + uploadDate))
        }
        else Future.successful(BadRequest(Json.toJsObject(invalidFileExtension)))
    }.getOrElse {
      Future.successful(BadRequest(Json.toJsObject(invalidUploadFormat)))
    }
  }

  /**
    * Method that retrieves all files in the database
    *
    * @return a list containing all the files in the database
    */
  def getAllFiles: Action[AnyContent] = Action.async {
    fileRepo.selectAllFiles.map { seq =>
      val result = JsArray(seq.map(tr => Json.toJsObject(tr)))
      Ok(result)
    }
  }

  /**
    * Method that returns the file with the given id
    *
    * @param id - identifier of the file we are looking for
    * @return the file corresponding to the id given
    */
  def getFileById(id: String): Action[AnyContent] = Action.async {
    fileRepo.selectFileById(id).map{ tr =>
      if(tr.isDefined) Ok(Json.toJsObject(tr.get))
      else BadRequest("File with id " + id + " does not exist.")
    }
  }

  /**
    * Method that deletes the file with the given id
    *
    * @param id - identifier of the file to be deleted
    * @return HTTP response Ok if the file was deleted and BadRequest if not
    */
  def deleteFile(id: String): Action[AnyContent] = Action.async {
    fileRepo.deleteFileById(id).map { i =>
      if(i > 0) { //TODO - Create file exists and check first
        Ok("File with id = " + id + " has been deleted.")
      } else BadRequest("File with id " + id + " does not exist.")
    }
  }


}
