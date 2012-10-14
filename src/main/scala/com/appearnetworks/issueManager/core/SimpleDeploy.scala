package com.appearnetwork.issueManager
package core

import akka.actor._
import cc.spray._
import cc.spray.can.server.HttpServer
import cc.spray.io.IoWorker
import cc.spray.io.pipelines.MessageHandlerDispatch
import http.{StatusCodes, HttpResponse}

import registry._


trait SimpleDeploy {
  this: Api with Services =>

  protected val ioWorker = new IoWorker(actorSystem).start()
  actorSystem.registerOnTermination { ioWorker.stop }

  protected val rootServiceActor = actorSystem.actorOf(
    Props(new SprayCanRootService(rootService))
  )

  protected val sprayCanServer = actorSystem.actorOf(
    Props(new HttpServer(
      ioWorker,
      MessageHandlerDispatch.SingletonHandler(rootServiceActor),
      actorSystem.settings.config)),
    name = "http-server"
  )

  sprayCanServer ! HttpServer.Bind(settings.serverHost, settings.serverPort)

}
