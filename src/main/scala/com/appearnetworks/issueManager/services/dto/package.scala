package com.appearnetwork.issueManager
package services

import domain.Issue


package object dto {
  implicit def fromIssue2IssueDto(issue: Issue): IssueDTO =
    IssueDTO(issue.id.toString, issue.description, issue.reporter, issue.state.toString, issue.transitionDate)
}
