package com.github.verhoevenv.fiasco

import akka.actor.{ActorRef, Props, Actor}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import com.github.verhoevenv.fiasco.request.ProductsRequestActor
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.{Product, AllProducts}
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
  implicit val timeout = Timeout(5 seconds)
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