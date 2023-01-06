package dentaira.cobaco.server.file

import scalikejdbc.{AutoSession, DBSession}

import java.util.UUID

trait FileRepository {

  def generateId(): UUID

  def findById(id: String)(implicit session: DBSession = AutoSession): Option[StoredFile]

  def save(storedFile: StoredFile)(implicit session: DBSession = AutoSession)

  def delete(id: String)(implicit session: DBSession = AutoSession)

  def update(file: StoredFile)(implicit session: DBSession = AutoSession)

}
