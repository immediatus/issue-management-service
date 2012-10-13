package com.appearnetwork.issueManager
package services

import akka.dispatch.Future
import cc.spray._
import cc.spray.http.{StatusCodes, StatusCode}

import domain.Issue
import logic._


object ResponseHelper extends JsonMarshaller with Lift {

  def singleObjectCallback(ctx: RequestContext, errorMsg: => String, statusCode: StatusCode = StatusCodes.OK)(f: Future[_]): Future[_]  =
    f.onComplete { _ match {
        case Right(Some(issue: Issue)) =>
          ctx.complete(statusCode, issue)
        case Right(None) =>
          ctx.fail(StatusCodes.NotFound, errorMsg)
        case Left(ex) =>
          ctx.fail(StatusCodes.InternalServerError, ex.toString)
      }
    }

  def listCallback(ctx: RequestContext, errorMsg: => String, statusCode: StatusCode = StatusCodes.OK)(f: Future[_]): Future[_]  =
    f.onComplete { _ match {
        case Right(Some(issues: List[_])) =>
          ctx.complete(statusCode, issues)
        case Right(Nil) =>
          ctx.fail(StatusCodes.NotFound, errorMsg)
        case Left(ex) =>
          ctx.fail(StatusCodes.InternalServerError, ex.toString)
      }
    }

  def emptyCallback(ctx: RequestContext, errorMsg: => String, statusCode: StatusCode = StatusCodes.OK)(f: Future[_]): Future[_]  =
    f.onComplete { _ match {
        case Right(Some(Unit)) =>
          ctx.complete(statusCode, "")
        case Right(None) =>
          ctx.fail(StatusCodes.NotFound, errorMsg)
        case Left(ex) =>
          ctx.fail(StatusCodes.InternalServerError, ex.toString)
      }
    }
}

