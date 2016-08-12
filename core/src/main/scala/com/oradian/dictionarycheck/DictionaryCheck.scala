package com.oradian.dictionarycheck

import scala.tools.nsc.{Global, Phase}
import scala.tools.nsc.plugins.{Plugin, PluginComponent}
import com.softwaremill.debug.DebugMacros._

class DictionaryCheck(val global: Global) extends Plugin {
  override val name = "dictionary-check"
  override val components = List[PluginComponent](DictionaryLookup)
  override val description = "Raise notifications on blacklisted words"

  private[this] var dictionaryPath = "dictionary.txt"

  override val optionsHelp = Some(
    s"""  -P:dictionary-check:input:<dictionary>  Dictionary file (default: dictionary.txt)"""
  )

  override def init(options: List[String], error: String => Unit): Boolean =
    options forall { option =>
      if (option.startsWith("input:")) {
        dictionaryPath = option.substring("input:".length)
        true
      } else {
        error("Unsupported option: " + option)
        false
      }
    }

  private[this] object DictionaryLookup extends PluginComponent {
    override val runsAfter = List("typer")
    override val runsBefore = List("patmat")
    override val phaseName = "dictionary-lookup"

    val global = DictionaryCheck.this.global
    import global._

    override def newPhase(prev: Phase): Phase = new StdPhase(prev) {
      val dictionary = Dictionary.parse(dictionaryPath)

      override def apply(unit: global.CompilationUnit): Unit = {
        unit.body foreach {
          case t @ q"$mods val $tname: $tpt = $expr" =>
            dictionary.get(tname.toString.trim) match {
              case Some(DictionaryEntry(_, Level.Info, message)) =>
                reporter.info(t.pos, message, false)
              case Some(DictionaryEntry(_, Level.Warning, message)) =>
                reporter.warning(t.pos, message)
              case Some(DictionaryEntry(_, Level.Error, message)) =>
                reporter.error(t.pos, message)
              case _ =>
            }

          case _ =>
        }
      }
    }
  }
}
