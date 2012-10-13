package com.appearnetwork.issueManager
package core

import com.typesafe.config.Config


final class Settings(config: Config) {

  import config._

  val serverHost = getString("server.host")
  val serverPort = getInt("server.port")

  val mongoHost = getString("mongo.host")
  val mongoPort = getInt("mongo.port")
  val mongoDbName = getString("mongo.dbName")
  val mongoConnectionsPerHost = getInt("mongo.connectionsPerHost")
  val mongoAutoConnectRetry = getBoolean("mongo.autoConnectRetry")
  val mongoConnectTimeout = millis("mongo.connectTimeout")
  val mongoBlockingThreads = getInt("mongo.threadsAllowedToBlockForConnectionMultiplier")

  val issueCollectionName = getString("issue-manager.issues.collection")

  private def millis(name: String): Int = getMilliseconds(name).toInt
  private def seconds(name: String): Int = millis(name) / 1000
}

