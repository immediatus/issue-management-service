package com.appearnetwork.issueManager
package logic

import akka.actor.ActorLogging
import akka.actor.{Props, Actor}
import org.joda.time.DateTime
import scala.collection.immutable.Map
import scalaz.effects._

import core.MongoDb
import core.utils.DefaultTimeout
import domain._


class IssueManagerActor(mongoDb: MongoDb) extends Actor with ActorLogging {

  import IssueStoreMessages._

  val issueDao = new IssueDao(mongoDb("issues"))
  import issueDao._

  import IssueState._

  val stateTransitions = Map(Created -> Started, Started -> Done)

  def receive = {
    case CreateIssue(_description, _reporter) =>
      val result = try { (for {
          issue <- io { Issue( description = _description, reporter = _reporter) }
          id <- insertIO(issue) map (_ err "Issue can not created %s.".format(issue.toString))
          dbIssue <- byId(id) } yield dbIssue).
          unsafePerformIO }
      catch { case ex => log.error(ex, "CreateIssue - message."); none }

      sender ! result

    case UpdateIssueDescription(id, description) =>
      val result = try { (for {
        _ <- updateById(id, description)
        issue <- byId(id) } yield issue).
        unsafePerformIO }
      catch { case ex => log.error(ex, "UpdateIssueDescription - message."); none }

      sender ! result

    case UpdateIssueReporter(id, reporter) =>
      val result = try { (for {
        _ <- changeReporterById(id, reporter)
        issue <- byId(id) } yield issue).
        unsafePerformIO }
      catch { case ex => log.error(ex, "UpdateIssueReporter - message."); none }

      sender ! result

    case DeleteIssueById(id) =>
      val result = try { (for {
        res <- removeIO(id) } yield Unit).
        unsafePerformIO.some }
      catch { case ex => log.error(ex, "DeleteIssueById - message."); none }

      sender ! result

    case ProcessIssueById(id) =>
      val result = try { (for {
        issue <- byId(id) map (_ err "No Issue with {id:%s}.".format(id))
        state <- io { stateTransitions.get(issue.state) } map (_ err "No next state for issu with {id: %s}.".format(id))
        _ <- changeStateById(id, state)
        dbIssue <- byId(id) } yield dbIssue).
        unsafePerformIO }
      catch { case ex => log.error(ex, "ProcessIssueById - message."); none }

      sender ! result

    case CleanupIssues(time) =>
      val result = try { removeLessThenTimeIO(new DateTime(time)).unsafePerformIO; Unit.some }
      catch { case ex => log.error(ex, "CleanupIssues - message."); none }

      sender ! result
  }
}
