package com.github.verhoevenv.fiasco

import com.github.verhoevenv.fiasco.transform.json.CategoryJsonProtocol._
import com.github.verhoevenv.fiasco.route.AllRoutes
import com.github.verhoevenv.fiasco.transform.json.JsonCategory
import org.specs2.mutable.Specification
import spray.http._
import spray.json._
import spray.testkit.Specs2RouteTest

class CategoryServiceSpec extends Specification with Specs2RouteTest with AllRoutes {
  def actorRefFactory = system
  
  "CategoryService" should {

    "return a list of categories when queried without id" in {
      Get("/api/v1/categories") ~> allRoutes ~> check {
        val categories = responseAs[List[JsonCategory]]
        categories.size should be greaterThan 1
      }
    }

    "return a single category when queried with a known id" in {
      Get("/api/v1/categories/1") ~> allRoutes ~> check {
        val category = responseAs[JsonCategory]
        category.id must beEqualTo(1)
      }
    }

    "not handle a category with an unknown id" in {
      Get("/api/v1/categories/1337") ~> allRoutes ~> check {
        status must be(StatusCodes.BadRequest)
      }
    }
  }


  implicit def HttpEntityToCategory(httpEntity: HttpEntity): JsonCategory =
    httpEntity.asString(HttpCharsets.`UTF-8`).parseJson.convertTo[JsonCategory]

  implicit def HttpEntityToListOfCategories(httpEntity: HttpEntity): List[JsonCategory] =
    httpEntity.asString(HttpCharsets.`UTF-8`).parseJson.convertTo[List[JsonCategory]]
}
