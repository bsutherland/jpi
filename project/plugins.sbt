resolvers ++= Seq(
	"Typesafe Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
	"sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.9")

// Static checking tools
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")
addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.16")
addSbtPlugin("de.johoop" % "cpd4sbt" % "1.2.0")
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.2.0")
