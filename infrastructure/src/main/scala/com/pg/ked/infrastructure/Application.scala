package com.pg.ked.infrastructure

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import com.typesafe.scalalogging.Logger

object Application extends App {
  private val logger = Logger(getClass)
  private val config = ConfigFactory.load()

  private implicit val system: ActorSystem = ActorSystem("zio-xke")

  logger.info("STARTING SERVER")

  Http()
    .newServerAt("0.0.0.0", config.getInt("server.port"))
    .bind(Router.routes)

  logger.info("SERVER STARTED")
}

object Router {
  val routes: Route = Routes.endpoints
}
