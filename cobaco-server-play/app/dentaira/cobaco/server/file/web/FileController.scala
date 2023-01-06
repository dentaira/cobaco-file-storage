package dentaira.cobaco.server.file.web

import dentaira.cobaco.server.file.{FileRepository, FileType, StoredFile}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}

import java.nio.file.Paths
import javax.inject.Inject

class FileController @Inject()(cc: ControllerComponents, repository: FileRepository) extends AbstractController(cc) {

  // TODO Reads,WritesをFormatにリファクタリングしてみる

  implicit val storedFileReads: Reads[StoredFile] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "name").read[String] and
      (JsPath \ "path").read[String] and // TODO PathReads
      (JsPath \ "type").read[String] and // TODO FileTypeReads
      (JsPath \ "size").read[Int]
    ) ((id, name, path, t, size) => StoredFile(id, name, Paths.get(path), FileType.valueOf(t), null, size))

  // TODO コンビネータを使って書き直してみる
  implicit val storedFileWrites: Writes[StoredFile] = new Writes[StoredFile] {
    override def writes(file: StoredFile): JsValue = Json.obj(
      "id" -> file.id,
      "name" -> file.name,
      "path" -> file.path.toString,
      "type" -> file.`type`.toString,
      "size" -> file.size
    )
  }

  def get(id: String) = Action { implicit request =>
    Ok(Json.toJson(repository.findById(id)))
  }

  def put = Action(parse.json) { implicit request =>
    val fileResult = request.body.validate[StoredFile]
    fileResult.fold(errors => {
      BadRequest(Json.obj("message" -> JsError.toJson(errors)))
    },
      file => {
        repository.save(file)
        Ok(Json.obj("message" -> ("file saved.")))
      })
  }

  def delete(id: String) = Action { implicit request =>
    repository.delete(id)
    Ok(Json.obj("message" -> "delete success."))
  }

  def post = Action(parse.json) { implicit request =>
    val fileResult = request.body.validate[StoredFile]
    fileResult.fold(errors => {
      BadRequest(Json.obj("message" -> JsError.toJson(errors)))
    },
      file => {
        repository.update(file)
        Ok(Json.obj("message" -> ("file updated.")))
      })
  }

}
