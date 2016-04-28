import sbt._
import Keys._

object finaglezk extends Build {
  val finagleVersion = "6.33.0"
  val clientVersion = "0.2.0"

  val buildSettings = Defaults.coreDefaultSettings ++ Seq(
//    name := "finagle-ZooKeeper",
    organization := "com.twitter",
    version := clientVersion,
    scalaVersion := "2.11.7"
//    scalacOptions := Seq(
//      "-feature",
//      "-language:implicitConversions",
//      "-language:postfixOps",
//      "-unchecked",
//      "-deprecation",
//      "-encoding", "utf8",
//      "-Ywarn-adapted-args"
//    )
//    crossScalaVersions := Seq("2.10.5", "2.11.7")
    //    scalacOptions ++= Seqsbt("-unchecked", "-deprecation", "-feature")
  )

  lazy val baseSettings = Seq(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.4",
      "com.twitter" %% "finagle-core" % finagleVersion,
      "junit" % "junit" % "4.12",
      "org.mockito" % "mockito-all" % "1.10.19" % "test"
    )
  )

  lazy val root = Project(
    id = "finagle-ZooKeeper",
    base = file(".")
  ).settings(buildSettings).aggregate(core, integration, example)

  lazy val core = Project(
    id = "finalge-ZooKeeper-Core",
    base = file("core"),
    settings = buildSettings ++ baseSettings
  )

  lazy val example = Project(
    id = "example",
    base = file("example"),
    settings = buildSettings
  ).dependsOn(core)

  lazy val integration = Project(
    id = "integration",
    base = file("integration"),
    settings = buildSettings ++ testSettings
  ).dependsOn(core, example)

  lazy val runTests = taskKey[Unit]("Runs configurations and tests")
  lazy val testSettings = Seq(
    runTests := IntegrationTest.integrationTestTask(testOnly in Test).value,
    parallelExecution := false
  )
}
