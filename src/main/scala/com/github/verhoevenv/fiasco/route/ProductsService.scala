package com.github.verhoevenv.fiasco.route

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import com.github.verhoevenv.fiasco.domain.Product
import com.github.verhoevenv.fiasco.request.ProductsRequestActor
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.{ProductRequest, AllProducts}
import com.github.verhoevenv.fiasco.transform.json.JsonProduct
import com.github.verhoevenv.fiasco.transform.json.JsonProduct._
import spray.http._
import spray.routing._
import spray.httpx.SprayJsonSupport._
import com.github.verhoevenv.fiasco.transform.json.JsonProductProtocol._


trait ProductsService extends HttpService with HandlerImplicits {

  val productsRoute : Route =
    path("products") {
      get {
        val request : ActorRef = actorRefFactory.actorOf(Props[ProductsRequestActor])
        val answer = request ? AllProducts()
        onSuccess(answer) {
          v =>
            val product: List[Product] = v.asInstanceOf[List[Product]]
            complete(asJson(product))
        }
      }
    } ~
    path("products" / IntNumber) { id =>
      get {
        val request : ActorRef = actorRefFactory.actorOf(Props[ProductsRequestActor])
        val answer = request ? ProductRequest(id)
        onSuccess(answer) {
          case Some(v) => complete(asJson(v.asInstanceOf[Product]))
          case None => complete(HttpResponse(StatusCodes.BadRequest))
        }
      }
    }
}