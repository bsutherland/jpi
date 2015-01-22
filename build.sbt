name := "jpi"

version := "1.0.0-SNAPSHOT"

resolvers += "Sedis repository" at "http://pk11-scratch.googlecode.com/svn/trunk/"

libraryDependencies ++= Seq(
  "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1",
  "org.json4s" %% "json4s-jackson" % "3.2.11"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
