package com.appearnetwork.issueManager
package core.registry

import akka.actor.ActorSystem
import cc.spray.Route

import core.Settings
import services._


object ServiceRouts {

  def apply(system: ActorSystem, settings: Settings): List[Route] = {

    new IssueReadService {
      val actorSystem = system
    }.route ::
    new IssueManageService {
      val actorSystem = system
    }.route ::
    Nil
  }

}
