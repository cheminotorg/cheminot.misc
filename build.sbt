lazy val buildSettings = Seq(
  organization := "org.cheminot",
  scalaVersion := "2.11.7",
  crossPaths := false
)

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xlint",
    "-Ywarn-dead-code",
    "-Ywarn-unused-import"
  ),
  scalacOptions in (Compile, console) ~= (_ filterNot (_ == "-Ywarn-unused-import"))
)

lazy val cheminotmiscSettings = buildSettings ++ commonSettings

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(name := "cheminotmisc").
  settings(cheminotmiscSettings:_*).
  settings(libraryDependencies += "com.propensive" %% "rapture" % "2.0.0-M7" exclude ("javax.servlet", "servlet-api")).
  settings(libraryDependencies += "joda-time" % "joda-time" % "2.9.1").
  settings(libraryDependencies += "com.github.tototoshi" %% "scala-csv" % "1.2.2").
  settings(libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4").
  settings(libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.0.13").
  settings(libraryDependencies += "org.scala-stm" %% "scala-stm" % "0.7")

resolvers += Resolver.typesafeRepo("releases")
