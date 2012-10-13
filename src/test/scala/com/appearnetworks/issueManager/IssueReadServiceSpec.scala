package com.appearnetwork.issueManager

import akka.util.Duration
import cc.spray.http._
import cc.spray.http.HttpMethods._
import cc.spray.test.SprayTest
import org.specs2.mutable._
import org.specs2.specification.BeforeAfter

import services.IssueReadService
import domain.Issue


class IssueReadServiceSpec extends Specification
  with SprayTest
  with IssueReadService
  with services.JsonUnmarshaller
  with services.Lift
  with core.Api
  with core.utils.DefaultTimeout {

  val duration = Duration("5 s")

  "The IssueService when GET" should {

    "/getByReporter/... - return empty list" in {
      val response = testService(HttpRequest(
        GET,
        "/getByReporter/FakeReporterForTest"),
        duration) { route }.
      response

      response.content.as[List[Issue]] mustEqual Right(Nil)
    }

    "/get/... - return a 404 if the record is not found" in {
      testService(HttpRequest(
        GET,
        "/get/5077bbf644aE4afa3b14f603"),
        duration) { route }.
      response.
      status mustEqual StatusCodes.NotFound
    }
  }
}
