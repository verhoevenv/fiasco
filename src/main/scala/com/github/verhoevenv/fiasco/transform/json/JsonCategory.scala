package com.github.verhoevenv.fiasco.transform.json

import com.github.verhoevenv.fiasco.domain.Category
import spray.json.{RootJsonFormat, DefaultJsonProtocol}

object JsonCategory {
  def asJson(category: Category) : JsonCategory = {
    new JsonCategory(category.id, category.name, category.pathToImg)
  }
  def asJson(categories: List[Category]) : List[JsonCategory] = {
    categories.map(asJson)
  }
}

case class JsonCategory(id: Int, name: String, pathToImg: String) {

}


object CategoryJsonProtocol extends DefaultJsonProtocol {
  implicit val categoryFormat: RootJsonFormat[JsonCategory] = jsonFormat3(JsonCategory.apply)
}