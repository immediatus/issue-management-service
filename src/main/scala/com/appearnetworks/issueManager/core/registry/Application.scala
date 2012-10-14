package com.appearnetwork.issueManager
package core.registry

import akka.actor.{Props, Actor}
import akka.routing.FromConfig
import akka.pattern.ask

import core._
import logic._


class ApplicationActor extends Actor {

  def receive = {
    case ContextInit(settings) =>

      val mongoDb = new MongoDb(settings)

      val reader = context.actorOf(
        Props().
        withCreator(
          new IssueReaderActor(
            mongoDb,
            settings)
          ).
        withRouter(FromConfig()),
        "issueReader")

       context.actorOf(
        Props().
        withCreator(
          new IssueManagerActor(
            mongoDb,
            settings)
          ).
        withRouter(FromConfig()),
        "issueManager")

      sender ! ContextInited()

    case ContextClose() =>
      context.children.foreach(context.stop _)
  }
}
