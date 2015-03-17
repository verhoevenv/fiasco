package com.github.verhoevenv.fiasco

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._

class ProductsServiceSpec extends Specification with Specs2RouteTest with ProductsService {
  def actorRefFactory = system
  
  "ProductsService" should {

    "return a list of products when queried without id" in {
      Get("/api/v1/products") ~> allRoutes ~> check {
        responseAs[String] must contain("products")
      }
    }

    "return a single product when queried with a known id" in {
      Get("/api/v1/products/1") ~> allRoutes ~> check {
        responseAs[String] must contain("1")
      }
    }

    "not handle a product with an unknown id" in {
      Get("/api/v1/products/1337") ~> allRoutes ~> check {
        status must be(StatusCodes.BadRequest)
      }
    }
  }
}
