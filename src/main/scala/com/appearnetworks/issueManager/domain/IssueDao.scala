package com.appearnetwork.issueManager
package domain

import com.mongodb.casbah.Imports._
import com.mongodb.casbah.query.Imports.DBObject
import com.novus.salat.dao.SalatDAO
import org.bson.types.ObjectId
import org.joda.time.DateTime
import scalaz.effects._

import IssueState._


final class IssueDao(collection: MongoCollection) extends SalatDAO[Issue, ObjectId](collection) {

  private def byIdQuery(id: ObjectId): DBObject = DBObject("_id" -> id)
  private def byIdQuery(issue: Issue): DBObject = byIdQuery(issue.id)

  private def updateIO(issue: Issue)(obj: DBObject): IO[Unit] = io {
    update(byIdQuery(issue), obj)
  }

  def byId(id: String): IO[Option[Issue]] = io {
    findOneById(new ObjectId(id))
  }

  def byId(id: ObjectId): IO[Option[Issue]] = io {
    findOneById(id)
  }

  val all: IO[List[Issue]] = io {
    find(DBObject()).toList
  }

  def byReporter(reporter: String): IO[List[Issue]] = io {
    find(DBObject("reporter" -> reporter)).toList
  }

  def updateById(id: String, description: String): IO[Unit] = io {
    collection.update(byIdQuery(new ObjectId(id)), $set("description" -> description))
  }

  def changeReporterById(id: String, reporter: String): IO[Unit] = io {
    collection.update(byIdQuery(new ObjectId(id)), $set("reporter" -> reporter))
  }

  def changeStateById(id: String, state: IssueState.Value): IO[Unit] = io {
     collection.update(byIdQuery(new ObjectId(id)), $set(
      "state" -> state.toString,
      "transitionDate" -> new DateTime
    ))
  }

  def insertIO(issue: Issue) = io {
    insert(issue)
  }

  def removeIO(issue: Issue): IO[Unit] = io {
    remove(byIdQuery(issue))
  }

  def removeIO(id: String): IO[Unit] = io {
    remove(byIdQuery(new ObjectId(id)))
  }

  def removeLessThenTimeIO(time: DateTime) = io {
    remove("transitionDate" $lt time)
  }

}
