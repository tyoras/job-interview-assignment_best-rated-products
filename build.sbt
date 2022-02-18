import Dependencies._

ThisBuild / organization := "com.itravel"
ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version      := "0.1.0-SNAPSHOT"

ThisBuild / scalacOptions ++= Seq(
  "-Yrangepos",
  "-Xlint",
  "-Xlint:-byname-implicit",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Ywarn-unused",
  "-Ydelambdafy:method",
  "-language:higherKinds",
  "-encoding",
  "UTF-8"
)

lazy val `best-rated-products` = (project in file(".")).aggregate(core, api)

lazy val core = (project in file("core"))
  .settings(
    libraryDependencies ++= coreDeps ++ coreTestDeps
  )

lazy val api = (project in file("api"))
  .settings(
    libraryDependencies ++= apiDeps ++ apiTestDeps
  )
  .dependsOn(core)
