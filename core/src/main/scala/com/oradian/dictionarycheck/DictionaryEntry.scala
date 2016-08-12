package com.oradian.dictionarycheck

import java.io._
import scala.collection.immutable.VectorBuilder

sealed trait Level
object Level {
  case object Info extends Level
  case object Warning extends Level
  case object Error extends Level

  def fromString(level: String): Level = level.toLowerCase match {
    case "info" => Info
    case "warning" => Warning
    case "error" => Error
    case _ => sys.error("Unknown level: " + level)
  }
}

case class DictionaryEntry(pattern: String, level: Level, message: String)

case class Dictionary(entries: Seq[DictionaryEntry]) {
  private[this] val lookup = entries flatMap { entry =>
    val pattern = entry.pattern
    val upperPattern = pattern.toUpperCase
    val lowerPattern = pattern.toLowerCase
    val sentencePattern = pattern.head.toUpper + pattern.tail

    val targets = Seq(pattern, upperPattern, lowerPattern, sentencePattern)
    targets.distinct map { target => target -> entry }
  } toMap

  def get(pattern: String): Option[DictionaryEntry] =
    lookup.get(pattern)
}

object Dictionary {
  private[this] val LineEntry = """([^;]+?);([^;]+?);(.*)""".r

  def parse(path: String): Dictionary = {
    val reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))
    try {
      val entries = new VectorBuilder[DictionaryEntry]

      var line: String = null
      while ({ line = reader.readLine(); line != null }) {
        val LineEntry(patternStr, levelStr, messageStr) = line
        entries += DictionaryEntry(
          pattern = patternStr.trim,
          level = Level.fromString(levelStr),
          message = messageStr.trim
        )
      }

      new Dictionary(entries.result)
    } finally {
      reader.close()
    }
  }
}