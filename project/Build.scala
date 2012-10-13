import sbt._
import com.github.siasia._
import WebPlugin._
import PluginKeys._
import Keys._

object Build extends sbt.Build {
  import Dependencies._

  lazy val myProject = Project("issue-manager", file("."))
    .settings(
      organization  := "com.appearnetworks",
      version       := "0.1",
      scalaVersion  := "2.9.1",
      scalacOptions := Seq("-deprecation", "-unchecked", "-encoding", "utf8"),
      resolvers     ++= Dependencies.resolutionRepos,
      libraryDependencies ++=
        compile(scalaz,
                akkaActor,
                sprayServer,
                sprayCan,
                sprayBase,
                liftJson,
                liftJsonExt,
                salat,
                akkaSlf4j,
                slf4j,
                logback) ++
        test(specs2, akkaTestKit)
    )
}
