# Experiment Framework

a set of experiments might be stored as multiple procedural files in a directory. But many aspects of these experiments may be repeated.

this is a simple framework designed to make it easier to manage stages of an experimental testbed.

we define an experiment:

```scala
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ExampleExperiment extends Experiment with App {
  case class Config (n: Int)

  val (ctx, log) = runSync(Config(12345), List(Steps.step1, Steps.step2))
  ctx.foreach(println)
  log.foreach(println)
}
```

and we defined our steps separately:

```scala
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
```

an async (future-wrapped) version of the run method exists as well.