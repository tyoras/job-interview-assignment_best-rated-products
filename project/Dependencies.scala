import sbt._

object Dependencies {

  case object co {
    case object fs2 {
      val fs2Version = "3.2.4"
      val `fs2-io`   = "co.fs2" %% "fs2-io" % fs2Version
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
