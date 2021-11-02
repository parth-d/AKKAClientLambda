package com.parth.client

import akka.actor.ActorSystem
//import akka.actor.TypedActor.dispatcher
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpResponse, HttpRequest}
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.Future

object client {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  import system.dispatcher

  val request = HttpRequest(
    method = HttpMethods.POST,
    uri = "https://cels0ps0ce.execute-api.us-east-2.amazonaws.com/default/grpcServer",
    entity = HttpEntity(
      ContentTypes.`application/json`,
      s"range=5&time=09:02:00.000&bucket=logsinput&key=log.log"
    )
  )

  def sendRequest(): Future[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    System.out.println(request.toString())
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))
    entityFuture.map(entity => entity.data.utf8String)
  }

  def main(args: Array[String]): Unit ={
    sendRequest().foreach(println )
  }
}
