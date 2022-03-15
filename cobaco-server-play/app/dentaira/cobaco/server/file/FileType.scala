package dentaira.cobaco.server.file

sealed trait FileType

object FileType {

  case object File extends FileType

  case object Directory extends FileType

  def valueOf(fileType: String): FileType = fileType match {
    case "FILE" => File
    case "DIRECTORY" | "FOLDER" => Directory
  }

}

