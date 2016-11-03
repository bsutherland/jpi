name := "jpi"

version := "1.1.0-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += "Sedis repository" at "http://pk11-scratch.googlecode.com/svn/trunk/"

libraryDependencies ++= Seq(
  cache,
  "com.github.tototoshi" %% "scala-csv" % "1.3.3",
  "org.json4s" %% "json4s-jackson" % "3.4.2"
)

import de.johoop.cpd4sbt.CopyPasteDetector._

lazy val root = (project in file(".")).enablePlugins(PlayScala)
