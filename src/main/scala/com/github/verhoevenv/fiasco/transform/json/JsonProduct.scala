package com.github.verhoevenv.fiasco.transform.json

import com.github.verhoevenv.fiasco.domain.{Product, ProductSauces, ProductComposition}
import spray.json._

object JsonProduct {
  def asJson(product: Product) : JsonProduct = {
    new JsonProduct(product.id, product.category.id, product.name, product.composition, product.sauces, product.internalType, product.price)
  }

  def asJson(products: List[Product]) : List[JsonProduct] = {
    products.map(asJson)
  }
}

case class JsonProduct(id: Int, categoryId: Int, name: String, composition: ProductComposition, sauces: ProductSauces, internalType: String, price: Int) {
}

object ProductCompositionJsonProtocol extends spray.json.DefaultJsonProtocol {
  implicit val productCompositionFormat: RootJsonFormat[ProductComposition] = new RootJsonFormat[ProductComposition] {
    def write(obj: ProductComposition): JsValue = listFormat[String].write(obj.composition)
    def read(json: JsValue): ProductComposition = ProductComposition(listFormat[String].read(json))
  }
}

object ProductSaucesJsonProtocol extends DefaultJsonProtocol {
  implicit val productSaucesFormat: RootJsonFormat[ProductSauces] = new RootJsonFormat[ProductSauces] {
    def write(obj: ProductSauces): JsValue = listFormat[String].write(obj.sauces)
    def read(json: JsValue): ProductSauces = ProductSauces(listFormat[String].read(json))
  }
}

object JsonProductProtocol extends DefaultJsonProtocol {
  implicit val jsonProductFormat: RootJsonFormat[JsonProduct] = new RootJsonFormat[JsonProduct] {
    def write(p: JsonProduct) = JsObject(
      "id" -> JsNumber(p.id),
      "categoryId" -> JsNumber(p.categoryId),
      "name" -> JsString(p.name),
      "composition" -> ProductCompositionJsonProtocol.productCompositionFormat.write(p.composition),
      "sauces" -> ProductSaucesJsonProtocol.productSaucesFormat.write(p.sauces),
      "type" -> JsString(p.internalType),
      "price" -> JsNumber(p.price)
    )

    def read(value: JsValue) = {
      value.asJsObject.getFields("id", "categoryId", "name", "composition", "sauces", "type", "price") match {
        case Seq(JsNumber(id), JsNumber(categoryId), JsString(name), jsComposition, jsSauces, JsString(internalType), JsNumber(price)) =>
          val composition: ProductComposition = ProductCompositionJsonProtocol.productCompositionFormat.read(jsComposition)
          val sauces: ProductSauces = ProductSaucesJsonProtocol.productSaucesFormat.read(jsSauces)

          new JsonProduct(
            id.toInt,
            categoryId.toInt,
            name,
            composition,
            sauces,
            internalType,
            price.toInt
          )
        case _ => throw new DeserializationException("Color expected")
      }
    }
  }
}
