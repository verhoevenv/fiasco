package com.github.verhoevenv.fiasco.domain

case class Product(id: Int, category: Category, name: String, composition: ProductComposition, sauces: ProductSauces, internalType: String, price: Int) {

}

case class ProductComposition(composition: List[String])
case class ProductSauces(sauces: List[String])
