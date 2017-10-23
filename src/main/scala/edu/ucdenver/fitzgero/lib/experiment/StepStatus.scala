package edu.ucdenver.fitzgero.lib.experiment

// closed algebra of status values
sealed trait StepStatus
case class StepSuccess(msg: Option[String]) extends StepStatus
case class StepFailure(msg: Option[String]) extends StepStatus
