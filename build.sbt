name := "handlebars-component"

organization := "com.github.savaki"

version := "SNAPSHOT"

resolvers += "Twitter Repo" at "http://maven.twttr.com"

{
    val finagleVersion = "5.3.9"
    libraryDependencies ++= Seq(
        "com.twitter" % "finagle-core" % finagleVersion withSources(),
        "com.twitter" % "finagle-native" % finagleVersion withSources(),
        "com.twitter" % "finagle-redis" % finagleVersion withSources(),
        "com.twitter" % "finagle-http" % finagleVersion withSources()
    )
}

{
	val handlebarsVersion = "0.6.1"
	libraryDependencies ++= Seq(
		"com.github.jknack" % "handlebars" % handlebarsVersion withSources()
	)
}

{
    libraryDependencies ++= Seq(
        "org.scalatest" %% "scalatest" % "1.8" % "test" withSources()
    )
}

