package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import com.github.verhoevenv.fiasco.domain.ProductsRepo
import com.github.verhoevenv.fiasco.request.ProductsRequestActor.{ProductRequest, AllProducts}

object ProductsRequestActor {
  case class AllProducts()
  case class ProductRequest(id: Int)
}

class ProductsRequestActor extends Actor with ProductsRepo {
  override def receive = {
    case AllProducts() => sender() ! allProducts
    case ProductRequest(id) => sender() ! product(id)
  }
}
