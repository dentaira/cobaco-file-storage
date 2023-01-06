package dentaira.cobaco.server.file.infra

import dentaira.cobaco.server.file.{FileType, StoredFile}
import dentaira.cobaco.server.test.TestDBSettings
import org.scalatest.flatspec.FixtureAnyFlatSpec
import scalikejdbc.scalatest.AutoRollback

import java.nio.file.Paths

class FileRepositoryImplTest extends FixtureAnyFlatSpec with AutoRollback with TestDBSettings {

  it should "test1" in { implicit session =>
    // TODO テストデータのセットアップ
    val sut = new FileRepositoryImpl()
    val actual = sut.findById("53babdc2-fb9f-4095-b886-8e94126f6e4e")
    println(actual)
  }

  it should "save" in { implicit session =>
    val sut = new FileRepositoryImpl()
    val file = new StoredFile(id = "id", name = "name", path = Paths.get("/path"), `type` = FileType.File, content = null, size = 1L)
    sut.save(file)

    println(sut.findById("id"))
  }

  it should "delete" in { implicit session =>
    val sut = new FileRepositoryImpl()
    val file = new StoredFile(id = "id", name = "name", path = Paths.get("/path"), `type` = FileType.File, content = null, size = 1L)
    sut.save(file)
    val maybeFile = sut.findById("id")
    println(maybeFile)
    assert(maybeFile.nonEmpty)

    val id = "id"
    sut.delete(id)
    val maybeEmpty = sut.findById("id")
    println(maybeEmpty)
    assert(maybeEmpty.isEmpty)
  }

  it should "update" in { implicit session =>
    val sut = new FileRepositoryImpl()
    val file = new StoredFile(id = "id", name = "name", path = Paths.get("/path"), `type` = FileType.File, content = null, size = 1L)
    sut.save(file)
    val maybeFile = sut.findById("id")
    println(maybeFile)
    assert(maybeFile.nonEmpty)

    val updateFile = new StoredFile(id = "id", name = "newfile", path = Paths.get("/newpath"), `type` = FileType.Directory,
      content = null, size = 0L)
    sut.update(updateFile)
    val maybeEmpty = sut.findById("id")
    println(maybeEmpty)
    assert(maybeEmpty.nonEmpty)
  }
}
