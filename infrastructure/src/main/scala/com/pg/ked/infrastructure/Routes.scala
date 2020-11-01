package com.pg.ked.infrastructure

import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{_enhanceRouteWithConcatenation, complete, get, handleExceptions, path, pathPrefix}
import akka.http.scaladsl.server.{ExceptionHandler, Route}
import com.pg.ked.infrastructure.health.Health
import com.typesafe.scalalogging.Logger

object Routes {
  private val logger = Logger(getClass)

  private def apiEndpoints: Route =
    pathPrefix("api") {
      Health.enpoints
    }

  private val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: Throwable =>
      val message = Option(e.getMessage).getOrElse(s"${e.getClass.getName} (No Error message supplied)")
      logger.error(message, e)
      complete(InternalServerError)
  }

  val endpoints: Route = handleExceptions(exceptionHandler) {
    path("") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Welcome to ZIO XKE API</h1>"))
      }
    } ~ apiEndpoints
  }
}
