package com.appearnetwork.issueManager
package services

import cc.spray.http._
import cc.spray.http.MediaTypes._
import cc.spray.typeconversion._
import net.liftweb.json._
import net.liftweb.json.JsonAST.{JString, JValue}
import net.liftweb.json.Serialization._
import org.bson.types.ObjectId
import net.liftweb.json.ext.DateTimeSerializer


trait Lift {
  implicit val liftJsonFormats = DefaultFormats + DateTimeSerializer + new ObjectIdSerializer
}

trait JsonUnmarshaller {
  this: Lift =>

  implicit def liftJsonUnmarshaller[A :Manifest] = new SimpleUnmarshaller[A] {
    val canUnmarshalFrom = ContentTypeRange(`application/json`) :: Nil
    def unmarshal(content: HttpContent) = protect {
      val jsonSource = DefaultUnmarshallers.StringUnmarshaller(content).right.get
      parse(jsonSource).extract[A]
    }
  }
}

trait JsonMarshaller {
  this: Lift =>

  implicit def liftJsonMarshaller[A <: AnyRef] = new SimpleMarshaller[A] {
    val canMarshalTo = ContentType(`application/json`) :: Nil
    def marshal(value: A, contentType: ContentType) = {
      val jsonSource = write(value)
      DefaultMarshallers.StringMarshaller.marshal(jsonSource, contentType)
    }
  }
}


class ObjectIdSerializer extends Serializer[ObjectId] {
  private val ObjectIdClass = classOf[ObjectId]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), ObjectId] = {
    case (TypeInfo(ObjectIdClass, _), json) => json match {
      case JString(s) if (ObjectId.isValid(s)) => new ObjectId(s)
      case x => throw new MappingException("Can't convert " + x + " to ObjectId")
    }
  }

  def serialize(implicit formats: Formats): PartialFunction[Any, JValue] = {
    case x: ObjectId => JString(x.toString)
  }
}
