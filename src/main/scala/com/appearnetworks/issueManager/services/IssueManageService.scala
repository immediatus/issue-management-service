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
import dto._
import logic._

trait IssueManageService extends Directives with DefaultTimeout with JsonUnmarshaller with Lift {

  val actorSystem: ActorSystem

  private def issueReader = actorSystem.actorFor("/user/application/issueReader")
  private def issueManager = actorSystem.actorFor("/user/application/issueManager")

  val route =
    path("issueCreate") {
      post {
        content(as[IssueDTO]) { issue => ctx =>
          ResponseHelper.singleObjectCallback(ctx, "Object with {%s} cannot be created." format issue, StatusCodes.Created) {
            issueManager ? IssueStoreMessages.CreateIssue(issue.description, issue.reporter)
          }
        }
      }
    } ~
    path("issueDescriptionUpdate") {
      put {
        content(as[PropertyDTO]) { property => ctx =>
          ResponseHelper.singleObjectCallback(ctx, "Object with {id: %s} cannot be updated." format property.id) {
            issueManager ? IssueStoreMessages.UpdateIssueDescription(property.id, property.value)
          }
        }
      }
    } ~
    path("issueReporterUpdate") {
      put {
        content(as[PropertyDTO]) { property => ctx =>
          ResponseHelper.singleObjectCallback(ctx, "Object with {id: %s} cannot be updateded." format property.id) {
            issueManager ? IssueStoreMessages.UpdateIssueReporter(property.id, property.value)
          }
        }
      }
    } ~
    path("issueCleanup" / PathElement) { dateTime =>
      put { ctx =>
          ResponseHelper.emptyCallback(ctx, "Cleanup error.") {
            issueManager ? IssueStoreMessages.CleanupIssues(dateTime)
          }
      }
    } ~
    path("issueProcess" / PathElement) { id =>
      put { ctx =>
          ResponseHelper.singleObjectCallback(ctx, "Object with {id: %s} cannot be processed." format id) {
            issueManager ? IssueStoreMessages.ProcessIssueById(id)
          }
      }
    } ~
    path("issueDelete" / PathElement) { id =>
      delete { ctx =>
          ResponseHelper.emptyCallback(ctx, "Object with {id: %s} cannot be deleted." format id) {
            issueManager ? IssueStoreMessages.DeleteIssueById(id)
          }
      }
    }
}
