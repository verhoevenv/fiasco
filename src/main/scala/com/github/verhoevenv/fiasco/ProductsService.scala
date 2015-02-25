package com.github.verhoevenv.fiasco

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

class ProductsServiceActor extends Actor with ProductsService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(productsRoute)
}


// this trait defines our service behavior independently from the service actor
trait ProductsService extends HttpService {

  val productsRoute : Route =
    path("products") {
      get {
        complete {
          "A list of all products"
        }
      }
    } ~
    path("products" / IntNumber) { id =>
      get {
          if(id < 1000) {
            complete("Received GET request for product " + id)
          } else {
            complete(HttpResponse(StatusCodes.BadRequest))
          }
      }
    }
}