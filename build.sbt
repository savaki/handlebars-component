name := "handlebars-component"

organization := "com.github.savaki"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

resolvers += "Twitter Repo" at "http://maven.twttr.com"

{
    val finagleVersion = "5.3.9"
    libraryDependencies ++= Seq(
        "com.twitter" % "finagle-core" % finagleVersion % "provided" withSources(),
        "com.twitter" % "finagle-native" % finagleVersion % "provided" withSources(),
        "com.twitter" % "finagle-redis" % finagleVersion % "provided" withSources(),
        "com.twitter" % "finagle-http" % finagleVersion % "provided" withSources()
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

