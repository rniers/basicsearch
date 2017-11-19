
lazy val commonSettings = Seq(
  organization := "com.basicsearch",
  version := "0.1.0",
  scalaVersion := "2.12.4",
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  ),
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
) ++ scalafmtSettings

lazy val versions = new {
  val finatra = "17.11.0"
  val guice = "4.1.0"
  val cats = "1.0.0-RC1"
  val logback = "1.2.3"
  val scalatest = "3.0.4"
  val mockito = "1.9.5"
  val specs2 = "2.4.17"
  val junit = "4.12"
  val scalacheck = "1.13.4"
}

lazy val scalafmtSettings = Seq(
  scalafmtOnCompile := true,
  scalafmtTestOnCompile := true
)

lazy val client = (project in file("client"))
  .settings(
    commonSettings,
    name := "client",
    libraryDependencies ++= commonDependencies ++ Seq(
      "com.twitter" %% "finagle-http" % "17.11.0"
    )
  )

lazy val server = (project in file("server"))
  .settings(
    commonSettings,
    name := "server",
    libraryDependencies ++= commonDependencies ++ Seq(
      "org.typelevel" %% "cats-core" % versions.cats,
      "com.twitter" %% "finatra-http" % versions.finatra,
      "ch.qos.logback" % "logback-classic" % versions.logback,
      // Test
      "ch.qos.logback" % "logback-classic" % versions.logback % "test",

      "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
      "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

      "com.twitter" %% "finatra-http" % versions.finatra % "test",
      "com.twitter" %% "inject-server" % versions.finatra % "test",
      "com.twitter" %% "inject-app" % versions.finatra % "test",
      "com.twitter" %% "inject-core" % versions.finatra % "test",
      "com.twitter" %% "inject-modules" % versions.finatra % "test",
      "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",
    ),
    mainClass := Some("com.basicsearch.server.App")
  )
  .dependsOn(client)

lazy val root = project.in(file("."))
  .settings(
    publishArtifact := false,
  )
  .aggregate(
    client,
    server
  )

lazy val commonDependencies = Seq(
  // Test
  "junit" % "junit" % versions.junit % "test",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2-mock" % versions.specs2 % "test"
)

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Ypartial-unification",
  "-deprecation",
  "-encoding",
  "utf8"
)
