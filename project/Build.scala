import sbt._
import Keys._

object BitsPiecesBuild extends Build {

  lazy val root = Project("root", file("."), settings = rootSetting) aggregate(algorithm)
  lazy val algorithm = Project("algorithm", file("algorithm"), settings = algorithmSetting)

  def sharedSetting = Defaults.defaultSettings ++ Seq(
    scalaVersion := "2.10.2",
    scalacOptions := Seq("-unchecked", "-optimize", "-deprecation"),

    //local maven repository
    resolvers += "Local Maven repository" at "file://" + Path.userHome.absolutePath+ "/.m2/repository",
    //spray repository
    resolvers += "spray repo" at "http://repo.spray.io",
    //type safe repository
    resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",

    libraryDependencies ++= Seq(
      "org.scalatest" % "scalatest_2.10" % "2.0.M6" % "test",
      "joda-time" % "joda-time" % "2.3",
      "com.typesafe.akka" %% "akka-actor" % "2.1.4"
    )
  )

  def rootSetting = sharedSetting ++ Seq(
    // TODO what's this mean?
    publish := {}
  )

  def algorithmSetting = sharedSetting ++ Seq(
    name := "bitspieces-algorithm"
  )
}