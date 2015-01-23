resolvers ++= Seq(
	"Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
	"sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "0.94.6")
