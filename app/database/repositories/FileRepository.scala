package database.repositories

import java.util.UUID

import api.dtos.FileDTO
import slick.dbio.DBIO

import scala.concurrent.Future

trait FileRepository {

  /**
    * Selects all rows from the files table on the database.
    *
    * @return
    */
  def selectAllFiles: Future[Seq[FileDTO]]

  /**
    *
    */
  def selectFileById(id: String): Future[FileDTO]

  /**
    * Deletes all rows from the files table on the database.
    *
    * @return
    */
  def deleteAllFiles: Future[Int]

  /**
    *
    */
  def deleteFileById(id: String): Future[Int]

  /**
    * Creates the files table on the database.
    */
  def createFilesTable: Future[Unit]

  /**
    * Drops the files table on the database.
    */
  def dropFilesTable: Future[Unit]

  /**
    * Checks if a corresponding file row exists on the database by providing its fileId.
    *
    * @param fileId Id of the file on the database.
    * @return true if row exists, false if not.
    */
  def existsCorrespondingFileId(fileId: String): Future[Boolean]

  /**
    * Checks if a corresponding file row exists on the database by providing the fileName.
    *
    * @param fileName Name of the file given by the user on the database.
    * @return true if row exists, false if not.
    */
  def existsCorrespondingFileName(fileName: String): Future[Boolean]

  /**
    * Retrieves a fileId of a row on the database by providing the fileName.
    *
    * @param fileName Name of the file given by the user on the database.
    */
  def selectFileIdFromFileName(fileName: String): Future[String]

  /**
    * Retrieves a fileName of a row on the database by providing the fileId.
    *
    * @param fileId Id of the file on the database.
    */
  def selectFileNameFromFileId(fileId: String): Future[String]

  /*
  /**
    * Retrieves a storageName of a row on the database by providing the fileName.
    *
    * @param fileName Name of the file given by the user on the database.
    */
  def selectStorageNameFromFileName(fileName: String): Future[String]

  /**
    * Retrieves a fileName of a row on the database by providing the storageName.
    *
    * @param storageName Name of the file on the storage folder on the database.
    */
  def selectFileNameFromStorageName(storageName: String): Future[String]
  */
  /**
    * Method that inserts a file (row) on the files table on the database.
    *
    * @param file FileDTO to be inserted on the database.
    */
  def insertInFilesTable(file: FileDTO): Future[Boolean]
}
