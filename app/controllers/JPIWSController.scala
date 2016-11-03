package controllers

import play.api.mvc._
import play.api.libs.streams._
import play.api.Logger

import javax.inject._

import akka.actor._
import akka.stream.Materializer

import services.JPIService
import models.JapaneseAddress

class JPIWSController @Inject() (
    implicit system: ActorSystem, materializer: Materializer, jpiService: JPIService
) extends Controller {

  object JPIWebSocketActor {
    def props(out: ActorRef) = Props(new JPIWebSocketActor(out))
  }

  class JPIWebSocketActor @Inject() (out: ActorRef) extends Actor {
    val StripChars = "〒ー- ".toSet
    val ValidPostcode = "([0-9]{7})".r

    def receive = {
      case msg: String =>
        val normalized = msg.trim.filterNot(StripChars)
        normalized match {
          case ValidPostcode(c) =>
            val addr = jpiService.getAddress(normalized)
            val reply = addr.map { a =>
              s"〒${a.postalCode} ${a.prefectureKanji} ${a.municipalityKanji} ${a.neighbourhoodKanji}"
            }.getOrElse("不明な郵便番号")
            out ! (reply)
          case _ => out ! ("無効な郵便番号")
        }

    }
  }

  def index = Action { implicit request =>
    Ok(views.html.index())
  }

  // No Cross-Site checking implemented..
  def chat: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => JPIWebSocketActor.props(out))
  }
}
