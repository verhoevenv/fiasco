package com.github.verhoevenv.fiasco

import com.github.verhoevenv.fiasco.route.AllRoutes
import com.github.verhoevenv.fiasco.transform.json.JsonProduct
import com.github.verhoevenv.fiasco.transform.json.JsonProductProtocol._
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import spray.json._

class ProductsServiceSpec extends Specification with Specs2RouteTest with AllRoutes {
  def actorRefFactory = system
  
  "ProductsService" should {

    "return a list of products when queried without id" in {
      Get("/v1/products") ~> allRoutes ~> check {
        val products: List[JsonProduct] = responseAs[List[JsonProduct]]
        products.size should be greaterThan 1
      }
    }

    "return a single product when queried with a known id" in {
      Get("/v1/products/1") ~> allRoutes ~> check {
        val product: JsonProduct = responseAs[JsonProduct]
        product.id must beEqualTo(1)
      }
    }

    "not handle a product with an unknown id" in {
      Get("/v1/products/1337") ~> allRoutes ~> check {
        status must be(StatusCodes.BadRequest)
      }
    }

    "return a list of matching products when queried with category id" in {
      Get("/v1/products/?categoryId=1") ~> allRoutes ~> check {
        val products: List[JsonProduct] = responseAs[List[JsonProduct]]
        products.foreach {
          p => p.categoryId must beEqualTo(1)
        }
        products.size should be greaterThanOrEqualTo 1
      }
    }
  }


  implicit def HttpEntityToCategory(httpEntity: HttpEntity): JsonProduct =
    httpEntity.asString(HttpCharsets.`UTF-8`).parseJson.convertTo[JsonProduct]

  implicit def HttpEntityToListOfCategories(httpEntity: HttpEntity): List[JsonProduct] =
    httpEntity.asString(HttpCharsets.`UTF-8`).parseJson.convertTo[List[JsonProduct]]
}
