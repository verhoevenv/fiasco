package com.github.verhoevenv.fiasco.route

import akka.actor.Actor
import spray.http.StatusCodes
import spray.routing._
import spray.util.LoggingContext


class AllRoutesActor extends Actor with AllRoutes {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(allRoutes)
}

trait AllRoutes extends HttpService with ProductsService with CategoryService {


  def allRoutes : Route =
    pathPrefix("api" / "v1") {
       categoryRoutes ~ productsRoute
    }

}