name := "dictionary-check"

libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value
libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 10)) => Seq(
    compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary
  )
  case _ => Nil
})
libraryDependencies += "com.softwaremill.scalamacrodebug" %% "macros" % "0.4"

scalaVersion := "2.11.8"
crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.0-M5")
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Xlint"
) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
  case Some((2, 10)) => Seq(
    "-Yclosure-elim",
    "-Ydead-code",
    "-Yinline"
  )  
  case Some((2, 11)) => Seq(
    "-Yclosure-elim",
    "-Yconst-opt",
    "-Ydead-code",
    "-Yinline"
  )  
  case Some((2, 12)) => Seq(
    "-opt:_"
  )
  case _ => Nil
})
