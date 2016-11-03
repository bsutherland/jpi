package services

import play.api.Logger
import play.api.Environment
import play.api.cache._

import javax.inject._
import java.io.{BufferedReader, InputStreamReader, File, FileInputStream}
import java.util.zip.ZipInputStream

import com.github.tototoshi.csv._

import models.JapaneseAddress

trait JPIService {
  def getAddress(postalCode: String): Option[JapaneseAddress]
}

@Singleton
class CacheJPIService @Inject() (
  cache: CacheApi, env: Environment
) extends JPIService  {

  final val JPURI = "http://www.post.japanpost.jp/zipcode/dl/kogaki/zip/ken_all.zip"
  final val ZippedCSVPath = new File("data", "ken_all.zip").toString
  final val CSVEncoding = "SJIS"

  // Build cache at startup.
  buildCache

  /** Get address from cache by postal code */
  def getAddress(postalCode: String) : Option[JapaneseAddress] = {
    cache.get[JapaneseAddress](postalCode)
  }

  /**
    Imports postal codes from a zipped CSV reference as follows:
    - Unzip the first file and parse CSV rows into model instances
    - Insert models into cache, keyed by postal code
   */
  def buildCache() {
    Logger.info(s"Initializing cache from $ZippedCSVPath.")
    val inStream = new ZipInputStream(new FileInputStream(env.getFile(ZippedCSVPath)))
    val csvFile = Option(inStream.getNextEntry)
    csvFile.foreach { _ =>
      val reader = CSVReader.open(new InputStreamReader(inStream, CSVEncoding))
      reader.foreach { fields =>
        val code = fields(2)
        val address = JapaneseAddress(
          code, fields(3), fields(4), fields(5), fields(6), fields(7), fields(8)
        )
        cache.set(code, address)
      }
    }
  }
}
