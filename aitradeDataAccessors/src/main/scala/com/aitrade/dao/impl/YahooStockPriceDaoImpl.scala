package com.aitrade.dao.impl

import com.aitrade.dao.StockPriceDao
import java.util.{Calendar, Date}
import com.aitrade.StockPrice
import scala.collection.mutable.ArrayBuffer
import java.net.URL
import scala.io.Source
import java.text.SimpleDateFormat

/**
 * http://code.google.com/p/yahoo-finance-managed/wiki/csvHistQuotesDownload
 */
class YahooStockPriceDaoImpl extends StockPriceDao {
  /**
   * Given a stock symbol, return a list of historical daily price for the given stock.
   * startDate and endDate are optional parameter, if startDate is None, this interface
   * should return the price since the stock is listed. If the endDate is None, this
   * interface shoudl return the price to Now.
   * @param symbol stock symbol
   * @param startDate start date of the price data series
   * @param endDate end date fo the price data series
   */
  def getHistoricalStockPrice(symbol: String, startDate: Option[Date], endDate: Option[Date]): Seq[StockPrice] = {
    val DataPattern = YahooStockPriceDaoImpl.RAW_DATA_PATTERN.r
    val rawData = getRawData(symbol, startDate, endDate)
    // verify the header format to make sure the data is returned as expected
    require(YahooStockPriceDaoImpl.RAW_DATA_HEADER == rawData.head.trim, "The returned header is different " +
      "from expected value. The returned value is " + rawData.head.trim)
    val result = ArrayBuffer.empty[StockPrice]
    // rawData.drop(1) to ignore the header
    for(str <- rawData.drop(1)) {
      str match {
        case DataPattern(date, open, high, low, close, volume, adjustedClose) => {
          result += StockPrice(
            YahooStockPriceDaoImpl.DATE_FORMATTER.parse(date),
            open.toDouble,
            high.toDouble,
            low.toDouble,
            close.toDouble,
            volume.toDouble,
            adjustedClose.toDouble
          )
        }
        case _ => throw new RuntimeException("Unrecoganized data got from yahoo server.")
      }
    }
    result
  }

  /**
   * Fetch the stock price file and return the price data as a sequence of String
   * The first element of the sequence is the header
   * @return
   */
  def getRawData(symbol: String, startDate: Option[Date], endDate: Option[Date]): Seq[String] = {

    val url = new URL(getRequestUrl(symbol, startDate, endDate))
    val in = Source.fromURL(url)
    val result = ArrayBuffer.empty[String]
    for (line <- in.getLines()) {
      result += line
    }
    // return the raw data
    result
  }

  /**
   * Construct the yahoo request url based on the request
   * @param symbol
   * @param startDate
   * @param endDate
   * @return
   */
  def getRequestUrl(symbol: String, startDate: Option[Date], endDate: Option[Date]): String = {
    val stringBuilder = new StringBuilder
    stringBuilder ++= YahooStockPriceDaoImpl.HISTORICAL_PRICE_URL_PREFIX
    stringBuilder ++= symbol
    // fetch daily price
    stringBuilder ++= "&g=d"
    // return the data as csv format
    stringBuilder ++= "&ignore=.csv"
    startDate match {
      case Some(x) => {
        val calendar = Calendar.getInstance()
        calendar.setTime(x)
        val month = calendar.get(Calendar.MONTH)
        stringBuilder ++= ("&a=" + (month - 1))
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        stringBuilder ++= ("&b=" + day)
        val year = calendar.get(Calendar.YEAR)
        stringBuilder ++= ("&c=" + year)
      }
      case None => // do nothing, nothing to be appended on the url
    }
    endDate match {
      case Some(x) => {
        val calendar = Calendar.getInstance()
        calendar.setTime(x)
        val month = calendar.get(Calendar.MONTH)
        stringBuilder ++= ("&d=" + (month - 1))
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        stringBuilder ++= ("&e=" + day)
        val year = calendar.get(Calendar.YEAR)
        stringBuilder ++= ("&f=" + year)

      }
      case None => // do nothing, nothing to be appended on the url
    }
    // return the constructed url
    stringBuilder.toString
  }
}

object YahooStockPriceDaoImpl {
  // prefix of the historical price url, client should append the symbol
  // and start date end date at the end of this url
  val HISTORICAL_PRICE_URL_PREFIX = "http://ichart.yahoo.com/table.csv?s="
  // Raw data pattern
  val RAW_DATA_PATTERN = """(\d\d\d\d-\d\d-\d\d),([0-9\.]*),([0-9\.]*),([0-9\.]*),([0-9\.]*),([0-9\.]*),([0-9\.]*)"""
  // Header format
  val RAW_DATA_HEADER = "Date,Open,High,Low,Close,Volume,Adj Close"
  // date time formatter
  val DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd")
}
