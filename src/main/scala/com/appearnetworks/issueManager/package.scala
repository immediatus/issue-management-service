package com.appearnetwork

import com.novus.salat.Context
import com.novus.salat.TypeHintFrequency
import com.novus.salat.StringTypeHintStrategy
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers


package object issueManager
    extends scalaz.Identitys
    with scalaz.Options
    with scalaz.Strings
    with scalaz.Lists
    with scalaz.Booleans {

  // custom salat context
  implicit val customSalatContext = new Context {
    val name = "Issue Manager Context"
    override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.Never)
  }
  RegisterJodaTimeConversionHelpers()
}
