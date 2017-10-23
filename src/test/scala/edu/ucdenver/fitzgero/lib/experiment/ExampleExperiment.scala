package edu.ucdenver.fitzgero.lib.experiment

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ExampleExperiment extends Experiment with App {
  case class Config (n: Int)

  val (ctx, log) = runSync(Config(12345), List(Steps.step1, Steps.step2))
  ctx.foreach(println)
  log.foreach(println)
}
