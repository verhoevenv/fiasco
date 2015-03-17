package com.github.verhoevenv.fiasco.request

import akka.actor.Actor
import com.github.verhoevenv.fiasco.request.CategoriesRequestActor.{AllCategories, Category}

object CategoriesRequestActor {
  case class AllCategories()
  case class Category(id: Int)
}

class CategoriesRequestActor extends Actor {
  override def receive = {
    case AllCategories() => sender() ! "A list of all categories"

    case Category(id) => {
      val result = if(id < 1000) {
        Some("Received GET request for category " + id)
      } else {
        None
      }
      sender() ! result
    }
  }
}