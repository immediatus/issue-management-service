package com.appearnetwork.issueManager
package logic

import org.joda.time.DateTime

import domain._


object IssueStoreMessages {
  sealed trait IssueStoreMessage
  case class CreateIssue(descritpion: String, reporter: String) extends IssueStoreMessage
  case class UpdateIssueDescription(id: String, description: String) extends IssueStoreMessage
  case class UpdateIssueReporter(id: String, reporter: String) extends IssueStoreMessage
  case class DeleteIssueById(id: String) extends IssueStoreMessage
  case class ProcessIssueById(id: String) extends IssueStoreMessage
  case class CleanupIssues(time: String) extends IssueStoreMessage
}

object IssueReadMessages {
  sealed trait IssueReadMessage
  case class GetIssue(id: String) extends IssueReadMessage
  case class GetAllIssues() extends IssueReadMessage
  case class GetAllIssuesByReporter(reporter: String) extends IssueReadMessage
}
