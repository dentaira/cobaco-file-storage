package dentaira.cobaco.server.test

import scalikejdbc.config.DBs

trait TestDBSettings {

  def load(): Unit = {
    DBs.setup()
  }

  load()
}
