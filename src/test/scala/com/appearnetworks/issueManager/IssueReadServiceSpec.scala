package com.appearnetwork.issueManager

import akka.util.Duration
import cc.spray.http._
import cc.spray.http.HttpMethods._
import cc.spray.test.SprayTest
import org.specs2.mutable._
import org.specs2.specification._
import scalaz.effects._

import services.IssueReadService
import services.dto._
import domain.Issue
import core.MongoDb
import domain.IssueDao


class IssueReadServiceSpec extends Specification
  with SprayTest
  with IssueReadService
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


  "The IssueService when GET" should {

    "/getByReporter/... - return empty list" in {
      val response = testService(HttpRequest(
        GET,
        "/getByReporter/FakeReporterForTest"),
        duration) { route }.
      response

      response.content.as[List[Issue]] mustEqual Right(Nil)
    }

    "/getByReporter/... - return empty list" in {
      testService(HttpRequest(
        GET,
        "/getByReporter/%s" format issue.reporter),
        duration) { route }.
      response.
      content.as[List[Issue]] match {
        case Right(list) => list.find(_.id == issue.id) must beSome[Issue]
      }
    }

    "/get/... - return a 404 if the record is not found (with exeption in log)" in {
      testService(HttpRequest(
        GET,
        "/get/5077bbf644aE4afa3b14f603"),
        duration) { route }.
      response.
      status mustEqual StatusCodes.NotFound
    }

    "/get/... - return a Json serialized issue." in {
      testService(HttpRequest(
        GET,
        "/get/%s" format issue.id.toString),
        duration) { route }.
      response.
      content.as[Issue] match { 
        case Right(x) => x.id mustEqual issue.id
      }
    }

  }

//  issueDao.removeIO(issue).unsafePerformIO

}
