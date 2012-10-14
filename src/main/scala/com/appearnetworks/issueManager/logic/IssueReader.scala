package com.appearnetwork.issueManager
package logic

import akka.actor.{Props, Actor}
import akka.actor.ActorLogging

import core.MongoDb
import core.Settings
import core.utils.DefaultTimeout
import domain.IssueDao


class IssueReaderActor(mongoDb: MongoDb, settings: Settings) extends Actor with ActorLogging {

  import IssueReadMessages._

  val issueDao = new IssueDao(mongoDb(settings.issueCollectionName))
  import issueDao._

  def receive = {
    case GetIssue(id) =>
      val result =
        try { byId(id).unsafePerformIO }
        catch { case ex => log.error(ex, "GetIssue - byId call."); none }
      sender ! result

    case GetAllIssues() =>
      val result =
        try { all.unsafePerformIO.some }
        catch { case ex => log.error(ex, "GetAllIssues - all call."); none }
      sender ! result

    case GetAllIssuesByReporter(reporter) =>
      val result =
        try { byReporter(reporter).unsafePerformIO.some }
        catch { case ex => log.error(ex, "GetIssuesByReporter - byReporter call."); none }
      sender ! result
  }

}
