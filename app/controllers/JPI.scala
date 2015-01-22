package controllers

import play.api.mvc._

import java.io.{BufferedReader, InputStreamReader}
import java.net.URL
import java.util.zip.ZipInputStream

import play.api.Play.current
import com.typesafe.plugin.RedisPlugin
import play.api.cache.Cache
import play.api.libs.json._
//import org.json4s.NoTypeHints
//import org.json4s.jackson.Serialization

object JPI extends Controller {

  final val JapaneseURI = "http://www.post.japanpost.jp/zipcode/dl/kogaki/zip/ken_all.zip"
  final val TestURI = "http://localhost:9000/assets/ken_all.zip"
  final val CSVEncoding = "SJIS"

  final val PostalCodeMin = 0
  final val PostalCodeMax = 9999999
  final val PostalCodeFmt = "%07d"

  def fmt(postalCode: Integer) : String = {
    PostalCodeFmt.format(postalCode)
  }

  def lookup(postalCode: Integer) = Action {
    if (postalCode < PostalCodeMin || postalCode > PostalCodeMax) {
      NotFound(
        "Postal code outside valid range (%s to %s)".format(
          fmt(PostalCodeMin), fmt(PostalCodeMax)
        )
      )
    } else {
      val json: Option[String] = Cache.getAs[String](postalCode.toString)
      json match {
        case None => NotFound("Postal code not found")
        case Some(j) => Ok(j).as("application/json")
      }
    }
  }

  /**
    Imports postal codes from a zipped CSV reference as follows:
    - Download the complete code directory from the specified URL
    - Unzips the first file and parses the CSV rows
    - Serializes CSV rows into JSON
    - Inserts JSON objects into Redis hash
   */
  def buildCache() = Action {
    val inStream = new ZipInputStream(new URL(TestURI).openStream())
    val csvFile = inStream.getNextEntry
    if (null != csvFile) {
      val reader = new BufferedReader(new InputStreamReader(inStream, CSVEncoding))
      var line = reader.readLine()
      //implicit val formats = Serialization.formats(NoTypeHints)
      while (line != null) {
        val elements = line.split(",")
        val code = stripQuotes(elements(2))
        //Cache.set(code, Serialization.write(parseCSVRow(elements)))
        Cache.set(code, parseCSVRow(elements).toString)
        line = reader.readLine()
      }
      Ok("Cache rebuilt")
    } else {
      InternalServerError("Cache rebuild failed")
    }
  }

  private def stripQuotes(s : String) : String = {
    s.stripPrefix("\"").stripSuffix("\"")
  }

  private def parseCSVRow(row : Array[String]) : JsObject = {
    //row.foreach(stripQuotes)
    var r = row map { stripQuotes(_) }
    return Json.obj(
      "prefecture_kana" -> r(3),
      "municipality_kana" -> r(4),
      "neighbourhood_kana" -> r(5),
      "prefecture_kanji" -> r(6),
      "municipality_kanji" -> r(7),
      "neighbourhood_kanji" -> r(8)
    )
  }



}
