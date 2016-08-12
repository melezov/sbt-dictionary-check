sbtPlugin := true

name := "dictionary-check"

scalaVersion := "2.10.6"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint",
  "-Yclosure-elim",
  "-Ydead-code",
  "-Yinline"
)
