package edu.ucdenver.fitzgero.lib.experiment

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ExampleExperiment extends Experiment with App {
  case class Config (n: Int)

  val (ctx, log) = runSync(Config(12345), List(Steps.step1, Steps.step2))
  ctx.foreach(println)
  log.foreach(println)

  val step3: AsyncStep = ("My Fird Step", (conf: Config, log: GlobalLog) => Future {
    println("doing step 1")
    println(s"$conf")
    Thread.sleep(1000)
    (StepSuccess(Some("yah")), Map("asdf" -> "leonardo words")) // ._2.updated("My First Step", "blah" -> "foo")
  })
  val step4: AsyncStep = ("My Fort Step", (conf: Config, log: GlobalLog) => Future {
    println("doing step 2")
    Thread.sleep(1000)
    (StepFailure(Some("blarg")), Map("a silent bell" -> "path.based.yooyoy"))
  })

  val (ctx2, log2) = runAsync(Config(54321), List(step3, step4))
  ctx2.foreach(println)
  log2.foreach(println)
}
