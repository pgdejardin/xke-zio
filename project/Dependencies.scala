import sbt._

object Dependencies {
  private val AkkaVersion     = "2.6.9"
  private val AkkaHttpVersion = "10.2.0"
  private val DoobieVersion   = "0.9.0"
  private val Json4sVersion   = "3.6.9"
  private val ZioVersion      = "1.0.3"

  lazy val akka               = "com.typesafe.akka" %% "akka-actor"       % AkkaVersion
  lazy val `akka-http`        = "com.typesafe.akka" %% "akka-http"        % AkkaHttpVersion
  lazy val `akka-stream`      = "com.typesafe.akka" %% "akka-stream"      % AkkaVersion
  lazy val `akka-http-json4s` = "de.heikoseeberger" %% "akka-http-json4s" % "1.34.0"

  lazy val `cats-core`       = "org.typelevel" %% "cats-core"       % "2.1.1"
  lazy val `doobie-core`     = "org.tpolecat"  %% "doobie-core"     % DoobieVersion
  lazy val `doobie-postgres` = "org.tpolecat"  %% "doobie-postgres" % DoobieVersion

  lazy val `json4s-ext`     = "org.json4s" %% "json4s-ext"     % Json4sVersion
  lazy val `json4s-jackson` = "org.json4s" %% "json4s-jackson" % Json4sVersion

  lazy val `logback-classic` = "ch.qos.logback"              % "logback-classic" % "1.2.3"
  lazy val `scala-logging`   = "com.typesafe.scala-logging" %% "scala-logging"   % "3.9.2"

  lazy val `typesafe-config` = "com.typesafe" % "config" % "1.3.2"

  lazy val zio                = "dev.zio" %% "zio"              % ZioVersion
  lazy val `zio-stream`       = "dev.zio" %% "zio-streams"      % ZioVersion
  lazy val `zio-interop-cats` = "dev.zio" %% "zio-interop-cats" % "2.1.4.0"
}
