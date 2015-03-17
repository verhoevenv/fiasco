package com.github.verhoevenv.fiasco.route

import akka.actor.{ActorRef, Props}
import akka.pattern.ask
import com.github.verhoevenv.fiasco.request.CategoriesRequestActor
import com.github.verhoevenv.fiasco.request.CategoriesRequestActor.{AllCategories, Category}
import spray.http._
import spray.routing._


trait CategoryService extends HttpService with HandlerImplicits {

   val categoryRoutes : Route =
     path("categories") {
       get {
         val request : ActorRef = actorRefFactory.actorOf(Props[CategoriesRequestActor])
         val answer = request ? AllCategories()
         onSuccess(answer) {
           v => complete(v.asInstanceOf[String])
         }
       }
     } ~
     path("categories" / IntNumber) { id =>
       get {
         val request : ActorRef = actorRefFactory.actorOf(Props[CategoriesRequestActor])
         val answer = request ? Category(id)
         onSuccess(answer) {
           case Some(v) => complete(v.asInstanceOf[String])
           case None => complete(HttpResponse(StatusCodes.BadRequest))
         }
       }
     }
 }