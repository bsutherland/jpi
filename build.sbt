name := "jpi"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.5"

resolvers += "Sedis repository" at "http://pk11-scratch.googlecode.com/svn/trunk/"

libraryDependencies ++= Seq(
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1",
  "org.json4s" %% "json4s-jackson" % "3.2.11"
)

import de.johoop.cpd4sbt.CopyPasteDetector._

cpdSettings

lazy val root = (project in file(".")).enablePlugins(PlayScala)
