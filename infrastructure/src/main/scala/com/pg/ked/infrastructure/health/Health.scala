package com.pg.ked.infrastructure.health

import akka.http.scaladsl.server.Directives.{complete, path}
import akka.http.scaladsl.server.{Directives, Route}
import com.pg.ked.infrastructure.serialization.RestSerializer
import de.heikoseeberger.akkahttpjson4s.Json4sSupport.marshaller

object Health extends RestSerializer {
  val enpoints: Route = path("health") {
    Directives.get {
      complete(ok())
    }
  }
}

case class ok(status: String = "UP")
