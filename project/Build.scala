import sbt._
import Keys._

object BitsPiecesBuild extends Build {

  lazy val root = Project("root", file("."), settings = rootSetting) aggregate(algorithm, aitradeInterface, aitradeDataAccessors, aitradeCommons, aitradeNow)
  lazy val algorithm = Project("algorithm", file("algorithm"), settings = algorithmSetting)
  lazy val aitradeInterface = Project("aitradeInterface", file("aitradeInterface"), settings = aitradeInterfaceSetting)
  lazy val aitradeDataAccessors = Project("aitradeDataAccessors", file("aitradeDataAccessors"), settings = aitradeDataAccessorsSetting).dependsOn(aitradeInterface)
  lazy val aitradeCommons = Project("aitradeCommons", file("aitradeCommons"), settings = aitradeCommonsSetting).dependsOn(aitradeInterface)
  lazy val aitradeNow = Project("aitradeNow", file("aitradeNow"), settings = aitradeNowSetting).dependsOn(aitradeInterface, aitradeDataAccessors, aitradeCommons)

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
      "com.typesafe.akka" %% "akka-actor" % "2.1.4",
      "org.scalatest" % "scalatest_2.10" % "2.0.M6" % "test",
      "junit" % "junit" % "4.11" % "test"
    )
  )

  def rootSetting = sharedSetting ++ Seq(
    // TODO what's this mean?
    publish := {}
  )

  def algorithmSetting = sharedSetting ++ Seq(
    name := "bitspieces-algorithm"
  )

  def aitradeInterfaceSetting = sharedSetting ++ Seq(
    name := "aitradeInterface"
  )

  def aitradeDataAccessorsSetting = sharedSetting ++ Seq(
    name := "aitradeDataAccessors",

    libraryDependencies ++= Seq(
      "commons-net" % "commons-net" % "3.3"
    )
  )

  def aitradeCommonsSetting = sharedSetting ++ Seq(
    name := "aitradeCommons",

    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-math3" % "3.2"
    )
  )

  def aitradeNowSetting = sharedSetting ++ Seq(
    name := "aitradeNow"
  )
}