/***
  *                  _           _    _______          _  ___   ___   ___   ___
  *         /\      | |         (_)  |__   __|        | |/ _ \ / _ \ / _ \ / _ \
  *        /  \   __| |_ __ ___  _ _ __ | | ___   ___ | | (_) | | | | | | | | | |
  *       / /\ \ / _` | '_ ` _ \| | '_ \| |/ _ \ / _ \| |\__, | | | | | | | | | |
  *      / ____ \ (_| | | | | | | | | | | | (_) | (_) | |  / /| |_| | |_| | |_| |
  *     /_/    \_\__,_|_| |_| |_|_|_| |_|_|\___/ \___/|_| /_/  \___/ \___/ \___/
  *                B.A. THESIS SIMON LISCHKA, reactive as hell.
  *              Beuth University of Applied Sciencies, 2015/2016
  *
  */
import sbt.Project.projectToRef
lazy val scalaV = "2.11.7"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  pipelineStages := Seq(scalaJSProd/*, gzip*/),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "com.lihaoyi" %% "utest" % "0.3.1" % "test",
    "com.github.benhutchison" %% "prickle" % "1.1.9"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).enablePlugins(PlayScala).
  aggregate(projectToRef(sharedJvm)).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  aggregate(projectToRef(sharedJs)).
  dependsOn(sharedJs)
  .settings(
    Seq(fastOptJS, fullOptJS) map {
      packageJSKey =>
        crossTarget in (Compile, packageJSKey) :=
          (baseDirectory in server).value / "public/javascripts"
    }: _*
  )

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV,
    libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "utest" % "0.3.1" % "test",
    "com.lihaoyi" %%% "pprint" % "0.3.6"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )


lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value