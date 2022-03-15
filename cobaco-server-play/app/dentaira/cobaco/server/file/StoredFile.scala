package dentaira.cobaco.server.file

import scalikejdbc._

import java.io.InputStream
import java.nio.file.{Path, Paths}

case class StoredFile(id: String,
                      name: String,
                      path: Path,
                      `type`: FileType,
                      content: InputStream,
                      size: Long) {
}

object StoredFile extends SQLSyntaxSupport[StoredFile] {

  override val tableName = "file"

  def apply(id: String, name: String, path: Path, `type`: FileType, content: InputStream, size: Long): StoredFile = {
    new StoredFile(id, name, path, `type`, content, size)
  }

  def apply(s: SyntaxProvider[StoredFile])(rs: WrappedResultSet): StoredFile = apply(s.resultName)(rs)

  def apply(s: ResultName[StoredFile])(rs: WrappedResultSet) = new StoredFile(
    id = rs.string(s.id),
    name = rs.string(s.name),
    path = Paths.get(s.path),
    `type` = FileType.valueOf(rs.string(s.`type`)),
    content = rs.binaryStream(s.content),
    size = rs.long(s.size)
  )
}
