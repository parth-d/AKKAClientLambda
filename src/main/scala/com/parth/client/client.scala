package com.parth.client

import akka.actor.ActorSystem
import akka.http.scaladsl.client.RequestBuilding.Get
//import akka.actor.TypedActor.dispatcher
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpMethods, HttpResponse, HttpRequest}
import akka.stream.ActorMaterializer

import scala.concurrent.duration._
import scala.concurrent.Future

object client {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher

  def sendRequest(): Future[String] = {
//    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get("https://a94he0mou3.execute-api.us-east-2.amazonaws.com/default/bucketextract?range=5&time=09:34:09.000&bucket=logsinput&key=log.log"))
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(2.seconds))
    entityFuture.map(entity => entity.data.utf8String)
  }

  def main(args: Array[String]): Unit ={
    sendRequest().foreach(println )
  }
}
