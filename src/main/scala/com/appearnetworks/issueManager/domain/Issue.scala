package com.appearnetwork.issueManager
package domain

import com.novus.salat.annotations.Key
import org.bson.types.ObjectId
import org.joda.time.DateTime

import IssueState._


case class Issue(
    @Key("_id") id: ObjectId = new ObjectId,
    description: String,
    reporter: String,
    state: IssueState.Value = Created,        // salat & type aliases (https://github.com/novus/salat/wiki/Enums)
    transitionDate: DateTime = new DateTime
    )
