package controllers

import play.api.mvc._

import java.io.{BufferedReader, InputStreamReader}
import java.net.URL
import java.util.zip.ZipInputStream

import play.api.Play.current
import com.typesafe.plugin.RedisPlugin
import play.api.cache.Cache
import play.api.libs.json._

object JPI extends Controller {

  final val JapaneseURI = "http://www.post.japanpost.jp/zipcode/dl/kogaki/zip/ken_all.zip"
  final val TestURI = "http://localhost:9000/assets/ken_all.zip"
  final val CSVEncoding = "SJIS"

  final val PostalCodeMin = 0
  final val PostalCodeMax = 9999999
  final val PostalCodeFmt = "%07d"

  final val CSVFields = List(
    ("prefecture_kana", 3),
    ("municipality_kana", 4),
    ("neighbourhood_kana", 5),
    ("prefecture_kanji", 6),
    ("municipality_kanji", 7),
    ("neighbourhood_kanji", 8)
  )

  /**
   Lookup a numeric postal code in the Redis cache
  */
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
        case Some(j) => Ok(j)
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
    val inStream = new ZipInputStream(new URL(TestURI).openStream)
    val csvFile = Option(inStream.getNextEntry)
    csvFile match {
      case Some(f) => {
        val reader = new BufferedReader(new InputStreamReader(inStream, CSVEncoding))
        for (line <- Iterator.continually(Option(reader.readLine)).takeWhile(!_.isEmpty)) {
          val elements = line.get.split(",")
          val code = stripQuotes(elements(2))
          Cache.set(code, parseCSVRow(elements).toString) 
        }
        Ok("Cache rebuilt")
      }
      case None => InternalServerError("Cache rebuild failed")
    }
  }

  /**
    Format a numeric postal code as a String
  */
  private def fmt(postalCode: Integer) : String = {
    PostalCodeFmt.format(postalCode)
  }

  /**
    Strip trailing and leading quotes from a String
  */
  private def stripQuotes(s : String) : String = {
    s.stripPrefix("\"").stripSuffix("\"")
  }

  /**
    Parse and convert a CSV row into a JSON object according to mapping in CSVFields
  */
  private def parseCSVRow(row : Array[String]) : JsObject = {
    var strippedRow = row map { stripQuotes(_) }
    var fields = CSVFields map {
      f => f._1 -> Json.toJsFieldJsValueWrapper(strippedRow(f._2))
    }
    Json.obj( fields: _* )
  }

}
