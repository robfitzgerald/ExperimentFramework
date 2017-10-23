package edu.ucdenver.fitzgero.lib.experiment

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

trait Experiment {

  // user-defined config type
  type Config <: Any

  // contstrained by config type
  type SyncStep = (String, (Config, GlobalLog) => (StepStatus, Log))
  type AsyncStep = (String, (Config, GlobalLog) => Future[(StepStatus, Log)])
  type SyncExperiment = List[SyncStep]
  type AsyncExperiment = List[AsyncStep]

  // fixed types for any experiment
  type Id = Int
  type GlobalLog = Map[String, Map[String, String]]
  type Log = Map[String, String]
  type Context = (Map[Id, Option[StepStatus]], GlobalLog)

  /**
    * run a synchronous set of experiments
    * @param conf user-defined config object
    * @param exp a list of experiments
    * @return the final context
    */
  def runSync (conf: Config, exp: SyncExperiment): Context = {
    val initStatus = exp.indices.map(i => i -> None).toMap
    val initLog = exp.map(t => t._1 -> Map.empty[String, String]).toMap
    val initCtx: Context = (initStatus, initLog)
    exp.zipWithIndex.foldLeft(initCtx)((ctx, step) => {
      val thisStepFunction = step._1._2
      val thisStepName = step._1._1
      val thisStepId = step._2
      val thisLog = ctx._2
      val (resultStatus, resultLog) = thisStepFunction(conf, thisLog)
      (ctx._1.updated(thisStepId, Some(resultStatus)), thisLog.updated(thisStepName, resultLog))
    })
  }

  /**
    * run an asynchronous set of experiments
    * @param conf user-defined config object
    * @param exp a list of experiments
    * @return the final context
    */
  def runAsync (conf: Config, exp: AsyncExperiment): Context = {
    val initStatus = exp.indices.map(i => i -> None).toMap
    val initLog = exp.map(t => t._1 -> Map.empty[String, String]).toMap
    val initCtx: Context = (initStatus, initLog)
    exp.zipWithIndex.foldLeft(initCtx)((ctx, step) => {
      val thisStepFunction = step._1._2
      val thisStepName = step._1._1
      val thisStepId = step._2
      val thisLog = ctx._2
      val future: Future[(StepStatus, Log)] = thisStepFunction(conf, thisLog)
      val (resultStatus, resultLog) = Await.result(future, 1.hour)
      (ctx._1.updated(thisStepId, Some(resultStatus)), thisLog.updated(thisStepName, resultLog))
    })
  }
}
