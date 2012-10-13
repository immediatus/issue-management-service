package com.appearnetwork.issueManager
package services

import akka.actor.ActorSystem
import akka.dispatch._
import akka.pattern.ask
import cc.spray._
import cc.spray.directives.PathElement
import cc.spray.http.MediaTypes._
import cc.spray.http.{StatusCode, StatusCodes}
import net.liftweb.json.{Formats, DefaultFormats}

import core.utils.DefaultTimeout
import domain.Issue
import logic._


trait IssueReadService extends Directives with DefaultTimeout with JsonMarshaller with Lift {

  val actorSystem: ActorSystem

  private def issueReader = actorSystem.actorFor("/user/application/issueReader")

  val route =
    path("") {
      get { completeWith { IssueServiceDescription() } }
    } ~
  path("get" / PathElement) { id =>
      get { ctx =>
          ResponseHelper.singleObjectCallback(ctx, "Object with {id:%s} not found." format id) {
            issueReader ? IssueReadMessages.GetIssue(id)
          }
      }
    } ~
    path("getByReporter" / PathElement) { reporter =>
      get { ctx =>
          ResponseHelper.listCallback(ctx, "Objects with {reporter:%s} not found." format reporter) {
            issueReader ? IssueReadMessages.GetAllIssuesByReporter(reporter)
          }
      }
    } ~
    path("getAll") {
      get { ctx =>
        ResponseHelper.listCallback(ctx, "Objects read error.") {
          issueReader ? IssueReadMessages.GetAllIssues()
        }
      }
    }
}

case class IssueServiceDescription(
  name: String = "IssueManagementService",
  version: String = "1.0.0",
  description: String = "Issue mamagement service."
)

