import Dependencies._

unmanagedJars in Compile += Attributed.blank(file(System.getenv("JAVA_HOME") + "/jre/lib/ext/jfxrt.jar"))

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.jalandis",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Set",
    libraryDependencies += "org.scalafx" %% "scalafx" % "8.0.102-R11",
    libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
  )
