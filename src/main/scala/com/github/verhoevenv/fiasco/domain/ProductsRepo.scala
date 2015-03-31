package com.github.verhoevenv.fiasco.domain

trait ProductsRepo extends CategoriesRepo {
  val products = List(
    Product(1, cheese, "Broodje ScalaKaas", ProductComposition(List("scala", "kaas")), ProductSauces(List("awesome")), "Club", 5),
    Product(2, ham, "Broodje ScalaHam", ProductComposition(List("scala", "ham")), ProductSauces(List("awesome")), "Club", 3)
  )

  def allProducts : List[Product] = products

  def product(id: Int) : Option[Product] = products.find(cat => cat.id == id)
}
