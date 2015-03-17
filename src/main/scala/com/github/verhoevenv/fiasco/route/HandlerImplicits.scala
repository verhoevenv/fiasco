package com.github.verhoevenv.fiasco.route

import akka.util.Timeout
import spray.routing.HttpService
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait HandlerImplicits extends HttpService {
  implicit val timeout = Timeout(5.seconds)
  implicit val executionContext : ExecutionContext = actorRefFactory.dispatcher
}
