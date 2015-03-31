package com.github.verhoevenv.fiasco.domain

trait CategoriesRepo {
  val cheese: Category = Category(1, "ScalaCheese", "/blub")
  val ham: Category = Category(2, "ScalaHam", "/blub")
  val categories = List(cheese, ham)

  def allCategories : List[Category] = categories

  def category(id: Int) : Option[Category] = categories.find(cat => cat.id == id)
}
