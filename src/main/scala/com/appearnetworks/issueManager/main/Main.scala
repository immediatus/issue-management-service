package com.appearnetwork.issueManager
package main

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

import core._


object Main {

  val system = ActorSystem("IssueManager")

  def main(args: Array[String]): Unit = {

    val app = new Application(system)
    sys.addShutdownHook { system.shutdown }
  }
}


class Application(val actorSystem: ActorSystem) extends Api with Services with SimpleDeploy {

}

