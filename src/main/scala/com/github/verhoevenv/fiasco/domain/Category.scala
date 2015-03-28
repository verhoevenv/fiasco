package com.github.verhoevenv.fiasco.domain

import spray.json.{RootJsonFormat, DefaultJsonProtocol}

case class Category(id: Int, name: String, pathToImg: String) {

}


object CategoryJsonProtocol extends DefaultJsonProtocol {
  implicit val categoryFormat: RootJsonFormat[Category] = jsonFormat3(Category)
}
