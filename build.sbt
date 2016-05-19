import org.scalajs.core.tools.classpath.CompleteClasspath
import org.scalajs.core.tools.corelib.CoreJSLibs
import org.scalajs.core.tools.io.{VirtualJSFile, MemVirtualJSFile}
import org.scalajs.jsenv.{JSEnv, JSConsole}
import org.scalajs.core.tools.logging.Logger
import sbt.Project.projectToRef
import org.scalajs.core.ir.Utils._
lazy val scalaV = "2.11.7"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  pipelineStages := Seq(scalaJSProd/*, gzip*/),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "com.lihaoyi" %% "utest" % "0.3.1" % "test",
    "com.github.benhutchison" %% "prickle" % "1.1.9",
    "com.vmunier" % "play-scalajs-sourcemaps_2.11" % "0.1.0",
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.13"
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
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1",
    //"com.lihaoyi" %%% "scalatags" % "0.5.4",
    "com.timushev" %%% "scalatags-rx" % "0.1.0"
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
    "com.lihaoyi" %%% "pprint" % "0.3.6",
    "com.github.benhutchison" %%% "prickle" % "1.1.9",
    "com.typesafe.akka" % "akka-testkit_2.11" % "2.3.13",
    "com.typesafe.akka" % "akka-actor_2.11" % "2.3.13"
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  ).jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value
