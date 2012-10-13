package com.appearnetwork.issueManager
package core

import akka.actor.Props
import akka.actor.ActorSystem
import akka.dispatch.Await
import akka.pattern.ask
import akka.util.Timeout

import registry._


case class ContextInit(settings: Settings)
case class ContextInited()
case class ContextClose()

trait Api {

  protected def actorSystem: ActorSystem
  protected def settings = new Settings(actorSystem.settings.config)

  protected val application = actorSystem.actorOf(
    props = Props[ApplicationActor],
    name = "application"
  )

  private val initTimeout = Timeout(30000)
  Await.ready(ask (application, ContextInit(settings))(initTimeout), initTimeout.duration)
}
