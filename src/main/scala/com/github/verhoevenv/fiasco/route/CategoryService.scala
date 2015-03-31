package com.github.verhoevenv.fiasco.route

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import com.github.verhoevenv.fiasco.domain.Category
import com.github.verhoevenv.fiasco.transform.json.JsonCategory
import com.github.verhoevenv.fiasco.transform.json.JsonCategory._
import spray.http._
import spray.routing._
import spray.json._
import spray.httpx.SprayJsonSupport._

import com.github.verhoevenv.fiasco.transform.json.CategoryJsonProtocol._
import com.github.verhoevenv.fiasco.request.CategoriesRequestActor
import com.github.verhoevenv.fiasco.request.CategoriesRequests.{AllCategoriesRequest, CategoryRequest}

trait CategoryService extends HttpService with HandlerImplicits {

   val categoryRoutes : Route =
     path("categories") {
       get {
         val request : ActorRef = actorRefFactory.actorOf(Props[CategoriesRequestActor])
         val answer = request ? AllCategoriesRequest()
         onSuccess(answer) {
           v => complete(v.asInstanceOf[List[JsonCategory]])
         }
       }
     } ~
     path("categories" / IntNumber) { id =>
       get {
         val request : ActorRef = actorRefFactory.actorOf(Props[CategoriesRequestActor])
         val answer = request ? CategoryRequest(id)
         onSuccess(answer) {
           case Some(v) =>
             val category: Category = v.asInstanceOf[Category]
             complete(asJson(category))
           case None => complete(HttpResponse(StatusCodes.BadRequest))
         }
       }
     }
 }