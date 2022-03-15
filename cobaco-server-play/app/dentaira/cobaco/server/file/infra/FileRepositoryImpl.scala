package dentaira.cobaco.server.file.infra

import dentaira.cobaco.server.file.{FileRepository, FileType, StoredFile}
import scalikejdbc._

import java.sql.Types
import java.util.UUID
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl() extends FileRepository {

  implicit val fileTypeParameterBinder: ParameterBinderFactory[FileType] = ParameterBinderFactory {
    value => (stmt, index) => stmt.setObject(index, value.toString.toUpperCase, Types.OTHER)
  }

  override def generateId(): UUID = UUID.randomUUID()

  override def findById(id: String)(implicit session: DBSession = AutoSession): Option[StoredFile] = {
    val f = StoredFile.syntax("f")
    withSQL {
      select
        .from(StoredFile as f)
        .where.eq(f.id, id)
    }.map(StoredFile(f)).first().apply()
  }

  override def save(file: StoredFile)(implicit session: DBSession = AutoSession) = {
    withSQL {
      val f = StoredFile.column
      insert.into(StoredFile)
        .columns(f.id, f.name, f.path, f.`type`, f.content, f.size)
        .values(file.id, file.name, file.path.toString, fileTypeParameterBinder.apply(file.`type`), file.content, file.size)
    }.update().apply()
  }

  override def delete(id: String)(implicit session: DBSession = AutoSession) = {
    withSQL {
      deleteFrom(StoredFile).where.eq(StoredFile.column.id, id)
    }.update().apply()
  }

  override def update(file: StoredFile)(implicit session: DBSession = AutoSession) = {
    delete(file.id)
    save(file)
  }

}
