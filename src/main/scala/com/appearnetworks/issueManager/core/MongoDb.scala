package com.appearnetwork.issueManager
package core

import com.mongodb.casbah.MongoConnection
import com.mongodb._


final class MongoDb(settings: Settings) {

  import settings._

  def apply(coll: String) = connection(coll)

  lazy val connection = MongoConnection(server, options)(mongoDbName)

  private lazy val server = new ServerAddress(mongoHost, mongoPort)

  private val options = new MongoOptions {
    connectionsPerHost = mongoConnectionsPerHost
    autoConnectRetry = mongoAutoConnectRetry
    connectTimeout = mongoConnectTimeout
    threadsAllowedToBlockForConnectionMultiplier = mongoBlockingThreads
  }
}
