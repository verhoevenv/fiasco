package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.{Product, AllProducts}

object ProductsRequestActor {
  case class AllProducts()
  case class Product(id: Int)
}

class ProductsRequestActor extends Actor {
  override def receive = {
    case AllProducts() => sender() ! "A list of all products"

    case Product(id) => {
      val result = if(id < 1000) {
        Some("Received GET request for product " + id)
      } else {
        None
      }
      sender() ! result
    }
  }
}
