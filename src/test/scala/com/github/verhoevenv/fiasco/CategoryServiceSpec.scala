package com.github.verhoevenv.fiasco

import com.github.verhoevenv.fiasco.route.AllRoutes
import org.specs2.mutable.Specification
import spray.http._
import spray.testkit.Specs2RouteTest

class CategoryServiceSpec extends Specification with Specs2RouteTest with AllRoutes {
  def actorRefFactory = system
  
  "CategoryService" should {

    "return a list of categories when queried without id" in {
      Get("/api/v1/categories") ~> allRoutes ~> check {
        responseAs[String] must contain("categories")
      }
    }

    "return a single category when queried with a known id" in {
      Get("/api/v1/categories/1") ~> allRoutes ~> check {
        responseAs[String] must contain("1")
      }
    }

    "not handle a category with an unknown id" in {
      Get("/api/v1/categories/1337") ~> allRoutes ~> check {
        status must be(StatusCodes.BadRequest)
      }
    }
  }
}
