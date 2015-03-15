package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import akka.actor.Actor.Receive
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.AllProducts

object ProductsRequestActor {
  case class AllProducts()
}

class ProductsRequestActor extends Actor {
  override def receive = {
    case AllProducts() => sender() ! "A list of all products"
  }
}
