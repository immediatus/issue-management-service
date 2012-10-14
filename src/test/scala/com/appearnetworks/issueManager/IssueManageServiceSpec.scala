package com.appearnetwork.issueManager

import akka.util.Duration
import cc.spray.http._
import cc.spray.http.HttpMethods._
import cc.spray.test.SprayTest
import org.specs2.mutable._
import org.specs2.specification.BeforeAfter
import scalaz.effects._

import services.IssueManageService
import services.dto._
import domain.Issue
import core.MongoDb
import domain.IssueDao


class IssueManageServiceSpec extends Specification
  with SprayTest
  with IssueManageService
  with services.JsonUnmarshaller
  with services.Lift
  with core.Api
  with core.utils.DefaultTimeout {

  val duration = Duration("5 s")

    val mongoDb = new MongoDb(settings)
  val issueDao = new IssueDao(mongoDb(settings.issueCollectionName))

  val issue =  (for {
      issue <- io { Issue( description = "Data for Testing issue magaer service", reporter = "Reporter1") }
      id <- issueDao.insertIO(issue) map (_ err "Issue can not created %s.".format(issue.toString))
      dbIssue <- issueDao.byId(id) } yield dbIssue).
      unsafePerformIO.getOrElse(Issue(description = "Not stored issue", reporter="WrongReporter"))

  val issueForDelete =  (for {
      issue <- io { Issue( description = "Data for Testing delete call in issue magaer service", reporter = "Reporter2") }
      id <- issueDao.insertIO(issue) map (_ err "Issue can not created %s.".format(issue.toString))
      dbIssue <- issueDao.byId(id) } yield dbIssue).
      unsafePerformIO.getOrElse(Issue(description = "Not stored issue", reporter="WrongReporter"))



  "The Issue Service when POST" should {
    "/issueCreate - return a 201 (Create) if the issue created" in {
      val response = testService(HttpRequest(
        POST,
        "/issueCreate",
        content = Some(
          HttpContent(
            ContentType(MediaTypes.`application/json`),
            "{\"description\":\"Creation Issue Test\",\"reporter\":\"Tester\"}"))),
        duration
        ) { route }.
        response.
        status mustEqual StatusCodes.Created
    }

    "/issueDescriptionUpdate - return a 200 if the issue updated" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueDescriptionUpdate",
        content = Some(
          HttpContent(
            ContentType(MediaTypes.`application/json`),
            "{\"id\":\"%s\",\"value\":\"Updated Description\"}" format issue.id))),
        duration
        ) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueReporterUpdate - return a 200 if the issue updated" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueReporterUpdate",
        content = Some(
          HttpContent(
            ContentType(MediaTypes.`application/json`),
            "{\"id\":\"%s\",\"value\":\"New Test Reporter\"}" format issue.id))),
        duration
        ) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueProcess/... - return a 200 if the issue processed" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueProcess/%s" format issue.id),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueCleanup/... - return a 200 if the issue processed" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueCleanup/%s" format issue.transitionDate),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueDelete/... - return a 200 if the issue deleted" in {
      val response = testService(HttpRequest(
        DELETE,
        "/issueDelete/%s" format issueForDelete.id),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }
  }
}
