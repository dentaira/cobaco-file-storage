name := "CobacoServerPlay"

version := "1.0"

lazy val `CobacoServerPlay` = (project in file(".")).enablePlugins(PlayScala)


resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  jdbc, ehcache, ws, specs2 % Test, guice,
  "org.postgresql" % "postgresql" % "42.3.1",
  "org.scalikejdbc" %% "scalikejdbc" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-config" % "3.5.0",
  "org.scalikejdbc" %% "scalikejdbc-play-initializer" % "2.8.0-scalikejdbc-3.5",
  "org.scalikejdbc" %% "scalikejdbc-test" % "3.5.0" % "test",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
)
      