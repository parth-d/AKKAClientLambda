package com.parth.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.{HttpEntity, HttpResponse}
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object client {
  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  import system.dispatcher
  val config: Config = ConfigFactory.load()
  val range: Int = config.getInt("parameters.range")
  val time: String = config.getString("parameters.time")
  val bucket: String = config.getString("parameters.bucket")
  val key: String = config.getString("parameters.key")
  val timeout: FiniteDuration = config.getInt("parameters.timeout").seconds

  def sendRequest(): Future[String] = {
    val responseFuture: Future[HttpResponse] = Http().singleRequest(Get(s"https://a94he0mou3.execute-api.us-east-2.amazonaws.com/default/bucketextract?range=$range&time=$time&bucket=$bucket&key=$key"))
    val entityFuture: Future[HttpEntity.Strict] = responseFuture.flatMap(response => response.entity.toStrict(timeout))
    entityFuture.map(entity => entity.data.utf8String)
  }

  def main(args: Array[String]): Unit ={
    System.out.println(Await.result(sendRequest(), timeout))
    System.exit(0)
  }
}
