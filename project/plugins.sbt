resolvers ++= Seq(
	"Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/",
	"sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/",
	"Linter Repository" at "https://hairyfotr.github.io/linteRepo/releases"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.7")

// Static checking tools
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.6.0")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "0.94.6")
addCompilerPlugin("com.foursquare.lint" %% "linter" % "0.1-SNAPSHOT")
addSbtPlugin("de.johoop" % "cpd4sbt" % "1.1.5")
