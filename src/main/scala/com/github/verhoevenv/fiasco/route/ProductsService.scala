package com.github.verhoevenv.fiasco.route

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.github.verhoevenv.fiasco.request.ProductsRequestActor
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.{AllProducts, Product}
import spray.http._
import spray.routing._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._


trait ProductsService extends HttpService {
  implicit val timeout = Timeout(5.seconds)
  implicit val executionContext : ExecutionContext = actorRefFactory.dispatcher

  val productsRoute : Route =
    path("products") {
      get {
        val request : ActorRef = actorRefFactory.actorOf(Props[ProductsRequestActor])
        val answer = request ? AllProducts()
        onSuccess(answer) {
          v => complete(v.asInstanceOf[String])
        }
      }
    } ~
    path("products" / IntNumber) { id =>
      get {
        val request : ActorRef = actorRefFactory.actorOf(Props[ProductsRequestActor])
        val answer = request ? Product(id)
        onSuccess(answer) {
          case Some(v) => complete(v.asInstanceOf[String])
          case None => complete(HttpResponse(StatusCodes.BadRequest))
        }
      }
    }
}