package com.appearnetwork.issueManager
package domain


object IssueState extends Enumeration {
  type IssueState = Value

  val Created = Value("CREATED")
  val Started = Value("STARTED")
  val Done = Value("DONE")
}
