package com.oradian.dictionarycheck

import sbt._

object DictionaryCheckPlugin extends AutoPlugin {
  object autoImport {
    lazy val dictionaryCheckSettings: Seq[Def.Setting[_]] = Seq(
      addCompilerPlugin("com.oradian.sbtdictionarycheck" %% "dictionary-check" % "0.0.1")
    )
  }

  override lazy val projectSettings = autoImport.dictionaryCheckSettings
}
