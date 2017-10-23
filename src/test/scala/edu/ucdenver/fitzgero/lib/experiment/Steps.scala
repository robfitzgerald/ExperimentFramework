package edu.ucdenver.fitzgero.lib.experiment

import edu.ucdenver.fitzgero.lib.experiment.ExampleExperiment._

object Steps {
  val step1: SyncStep = ("My First Step", (conf: Config, log: GlobalLog) => {
    println("doing step 1")
    println(s"$conf")
    Thread.sleep(1000)
    (StepSuccess(Some("yah")), Map("foo" -> "bar")) // ._2.updated("My First Step", "blah" -> "foo")
  })
  val step2: SyncStep = ("My Second Step", (conf: Config, log: GlobalLog) => {
    println("doing step 2")
    Thread.sleep(1000)
    (StepFailure(Some("blarg")), Map("noog" -> "floog"))
    //  log(ctx, "My Second Step", "foo", "bar")
  })
}
