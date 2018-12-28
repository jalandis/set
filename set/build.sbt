import Dependencies._

unmanagedJars in Compile += Attributed.blank(file("/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/ext/jfxrt.jar"))

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging, DebianPlugin, WindowsPlugin)
  .settings(
    inThisBuild(List(
      organization := "com.jalandis",
      scalaVersion := "2.12.5",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "SetGame",
    version := "1.0",
    maintainer := "John Landis <jalandis@gmail.com>",
    packageSummary := "Set Game",
    packageDescription := """Set Game""",

    wixProductId := "9eecfe5d-9079-4c85-9a86-2f497833324e",
    wixProductUpgradeId := "9561fc95-94be-41d5-a669-32fa0519ada5",

    libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.144-R12",
    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.8.0",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.5" % "test"
  )
