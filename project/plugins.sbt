logLevel := Level.Debug

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.3")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")

addSbtPlugin("com.vmunier" % "sbt-play-scalajs" % "0.2.8")
