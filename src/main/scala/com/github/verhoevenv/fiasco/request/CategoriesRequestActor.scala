package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import com.github.verhoevenv.fiasco.domain.CategoriesRepo
import com.github.verhoevenv.fiasco.request.CategoriesRequests.{AllCategoriesRequest, CategoryRequest}

object CategoriesRequests {
  case class AllCategoriesRequest()
  case class CategoryRequest(id: Int)
}

class CategoriesRequestActor extends Actor with CategoriesRepo {
  override def receive = {
    case AllCategoriesRequest() => sender() ! allCategories
    case CategoryRequest(id) => sender() ! category(id)
  }
}