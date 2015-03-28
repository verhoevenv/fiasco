package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import com.github.verhoevenv.fiasco.domain.Category
import com.github.verhoevenv.fiasco.request.CategoriesRequests.{AllCategoriesRequest, CategoryRequest}

object CategoriesRequests {
  case class AllCategoriesRequest()
  case class CategoryRequest(id: Int)
}

trait CategoriesRepo {
  val categories = List(Category(1, "ScalaCheese", "/blub"), Category(2, "ScalaHam", "/blub"))

  def allCategories : List[Category] = categories

  def category(id: Int) : Option[Category] = categories.find(cat => cat.id == id)
}

class CategoriesRequestActor extends Actor with CategoriesRepo {
  override def receive = {
    case AllCategoriesRequest() => sender() ! allCategories
    case CategoryRequest(id) => sender() ! category(id)
  }
}