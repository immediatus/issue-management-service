package com.appearnetwork.issueManager
package core.utils

import akka.util.Timeout
import akka.util.duration._

trait DefaultTimeout {
  final implicit val timeout = Timeout(3 seconds)
}
