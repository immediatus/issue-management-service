package com.appearnetwork.issueManager
package core

import akka.actor.{ActorRef, Props}
import akka.util.Timeout
import cc.spray._
import http.{StatusCodes, HttpResponse}

import registry.ServiceRouts


trait Services {
  this: Api =>

  protected lazy val routes = ServiceRouts(actorSystem, settings)

  private val svc: Route => ActorRef = route => actorSystem.actorOf(
    Props(new HttpService(route, {
      case _ => HttpResponse(StatusCodes.BadRequest)
    })))

  protected lazy val rootService = actorSystem.actorOf(
    props = Props(new RootService(
      svc(routes.head),
      routes.tail.map(svc):_*
    )),
    name = "root-service"
  )
}
