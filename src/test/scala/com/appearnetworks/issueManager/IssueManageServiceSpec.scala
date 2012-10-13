package com.appearnetwork.issueManager

import akka.util.Duration
import cc.spray.http._
import cc.spray.http.HttpMethods._
import cc.spray.test.SprayTest
import org.specs2.mutable._
import org.specs2.specification.BeforeAfter

import services.IssueManageService


class IssueManageServiceSpec extends Specification
  with SprayTest
  with IssueManageService
  with services.JsonUnmarshaller
  with services.Lift
  with core.Api
  with core.utils.DefaultTimeout {

  val duration = Duration("5 s")

  "The Issue Service when POST" should {
    "/issueCreate - return a 201 (Create) if the issue created" in {
      val response = testService(HttpRequest(
        POST,
        "/issueCreate",
        content = Some(
          HttpContent(
            ContentType(MediaTypes.`application/json`),
            "{\"description\":\"Hello World\",\"reporter\":\"Tester\"}"))),
        duration
        ) { route }.
        response

        response.status mustEqual StatusCodes.Created
    }

    "/issueDescriptionUpdate - return a 200 if the issue updated" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueDescriptionUpdate",
        content = Some(
          HttpContent(
            ContentType(MediaTypes.`application/json`),
            "{\"id\":\"50799bc244ae178375fbae85\",\"value\":\"Updated Description\"}"))),
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
            "{\"id\":\"50799bc244ae178375fbae85\",\"value\":\"New Test Reporter\"}"))),
        duration
        ) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueProcess/... - return a 200 if the issue processed" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueProcess/50799ea044ae0212fa29b1cd"),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueCleanup/... - return a 200 if the issue processed" in {
      val response = testService(HttpRequest(
        PUT,
        "/issueCleanup/2012-10-13T19:30:00Z"),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }

    "/issueDelete/... - return a 200 if the issue deleted" in {
      val response = testService(HttpRequest(
        DELETE,
        "/issueDelete/50799e8244ae5e3f384b2ead"),
        duration) { route }.
        response

        response.status mustEqual StatusCodes.OK
    }
  }
}
