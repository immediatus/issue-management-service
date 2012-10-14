package com.appearnetwork.issueManager
package services.dto

import org.joda.time.DateTime


case class IssueDTO(
    id: String,
    description: String,
    reporter: String,
    state: String,
    transitionDate: DateTime
    )
