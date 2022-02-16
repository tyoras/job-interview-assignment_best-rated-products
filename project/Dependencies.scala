import sbt._

object Dependencies {

  case object co {
    case object fs2 {
      val fs2Version = "3.2.4"
      val `fs2-io`   = "co.fs2" %% "fs2-io" % fs2Version
    }
  }

  case object io {
    case object circe {
      val circeVersion    = "0.14.1"
      val `circe-core`    = dep("core")
      val `circe-generic` = dep("generic")
      val `circe-parser`  = dep("parser")

      private def dep(artifact: String) = "io.circe" %% s"circe-$artifact" % circeVersion
    }
  }

  case object org {
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
    io.circe.`circe-parser`,
    org.typelevel.`cats-effect`
  )

  lazy val coreTestDeps = Seq(
    org.scalatest.scalatest
  ).map(_ % Test)

  lazy val apiDeps = Seq(
  )

  lazy val apiTestDeps = Seq(
    org.scalatest.scalatest
  ).map(_ % Test)
}
