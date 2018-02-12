
name := "seahorse-test"

version := "1.0"
scalaVersion := "2.11.8"

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "ai.deepsense" %% "seahorse-executor-deeplang" % "1.4.2" % Provided,
  "org.apache.spark" %% "spark-core" % "2.1.1" % Provided,
  "org.apache.spark" %% "spark-sql" % "2.1.1" % Provided,
  "org.apache.spark" %% "spark-mllib" % "2.1.1" % Provided,
  // For the purposes of this test, this dependency is somewhat arbitrary, except for the
  // fact that it also has transitive dependencies that trigger Reflections errors
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.275"
)

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

val shadePrefixes = Seq(
  "com.amazonaws",
  "org.apache.http"
)

assemblyShadeRules in assembly := shadePrefixes.map(p ⇒ ShadeRule.rename(s"$p.**" -> s"shaded.$p.@1").inAll)
