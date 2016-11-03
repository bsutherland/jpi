package controllers

import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.libs.json._

import javax.inject._

import services.JPIService
import models.JapaneseAddress

@Singleton
class JPIController @Inject() (jpiService: JPIService) extends Controller {

  implicit val addressWrites = new Writes[JapaneseAddress] {
    def writes(address: JapaneseAddress) = Json.obj(
      "prefecture_kana" -> address.prefectureKana,
      "municipality_kana" -> address.municipalityKana,
      "neighbourhood_kana" -> address.neighbourhoodKana,
      "prefecture_kanji" -> address.prefectureKanji,
      "municipality_kanji" -> address.municipalityKanji,
      "neighbourhood_kanji" -> address.neighbourhoodKanji
    )
  }

  def address(postalCode: String) = Action {
    jpiService.getAddress(postalCode) match {
      case None => NotFound("Postal code not found")
      case Some(address) => Ok(Json.stringify(Json.toJson(address)))
          .as("application/json; charset=utf-8")
    }
  }

}
