import com.typesafe.config.ConfigFactory

name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  evolutions,
  cache,
  ws,
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.github.tminglei" %% "slick-pg" % "0.12.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

fork in run := false

// code generation task
//val conf = ConfigFactory.parseFile(new File("conf/application.conf")).resolve()
//slick <<= slickCodeGenTask
//
//lazy val slick = TaskKey[Seq[File]]("gen-tables")
//lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
//  val outputDir = (dir / "slick").getPath
//  val url = conf.getString("slick.dbs.default.db.url")
//  val jdbcDriver = conf.getString("slick.dbs.default.db.driver")
//  val slickDriver = conf.getString("slick.dbs.default.driver").dropRight(1)
//  val pkg = "database"
//  val user = conf.getString("slick.dbs.default.db.user")
//  val password = conf.getString("slick.dbs.default.db.password")
//  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg, user, password), s.log))
//  val fname = outputDir + s"/$pkg/Tables.scala"
//  Seq(file(fname))
//}