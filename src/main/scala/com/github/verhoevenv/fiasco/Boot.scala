package com.github.verhoevenv.fiasco

import akka.actor._
import akka.io.IO
import akka.io.Tcp.CommandFailed
import akka.pattern.ask
import akka.util.Timeout
import com.github.verhoevenv.fiasco.route.AllRoutesActor
import spray.can.Http

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object Boot extends App {

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[AllRoutesActor], "demo-service")

  implicit val timeout = Timeout(5.seconds)
  implicit val ec : ExecutionContext = system.dispatcher

  // start a new HTTP server on port 8080 with our service actor as the handler
  val bindResult = IO(Http) ? Http.Bind(service, interface = "localhost", port = 8080)

  bindResult onComplete {
    case Success(CommandFailed(Http.Bind(_, _, _, _, _))) => system.shutdown()
    case Success(_) => {}
    case Failure(failed) => system.shutdown()
  }
}
