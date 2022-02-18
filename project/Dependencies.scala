import sbt._

object Dependencies {

  case object ch {
    case object qos {
      case object logback {
        val `logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.10"
      }
    }
  }

  case object co {
    case object fs2 {
      val fs2Version = "3.2.4"
      val `fs2-io`   = "co.fs2" %% "fs2-io" % fs2Version
    }
  }

  case object io {
    case object circe {
      val circeVersion           = "0.14.1"
      val `circe-core`           = dep("core")
      val `circe-generic`        = dep("generic")
      val `circe-generic-extras` = dep("generic-extras")
      val `circe-parser`         = dep("parser")

      private def dep(artifact: String) = "io.circe" %% s"circe-$artifact" % circeVersion
    }
  }

  case object org {
    case object http4s {
      val http4sVersion         = "0.23.10"
      val `http4s-blaze-server` = dep("blaze-server")
      val `http4s-circe`        = dep("circe")
      val `http4s-client`       = dep("client")
      val `http4s-dsl`          = dep("dsl")

      private def dep(artifact: String) = "org.http4s" %% s"http4s-$artifact" % http4sVersion
    }

    case object scalatest {
      val scalatest = "org.scalatest" %% "scalatest" % "3.2.10"
    }

    case object typelevel {
      val `cats-effect` = "org.typelevel" %% "cats-effect" % "3.3.4"
    }
  }

  lazy val coreDeps = Seq(
    co.fs2.`fs2-io`,
    io.circe.`circe-core`,
    io.circe.`circe-generic`,
    io.circe.`circe-generic-extras`,
    io.circe.`circe-parser`,
    org.typelevel.`cats-effect`
  )

  lazy val coreTestDeps = Seq(
    org.scalatest.scalatest
  ).map(_ % Test)

  lazy val apiDeps = Seq(
    ch.qos.logback.`logback-classic`,
    org.http4s.`http4s-blaze-server`,
    org.http4s.`http4s-circe`,
    org.http4s.`http4s-dsl`
  )

  lazy val apiTestDeps = Seq(
    org.http4s.`http4s-client`,
    org.scalatest.scalatest
  ).map(_ % Test)
}
