package com.aitrade.dao.impl

import org.scalatest.FunSuite
import java.util.Calendar

class YahooStockPriceDaoImplTest extends FunSuite {

  test("Test construct yahoo url with start date and end date to fetch historical stock price.") {
    val daoImpl = new YahooStockPriceDaoImpl
    val calendar = Calendar.getInstance()
    calendar.set(2013, 8, 1, 0, 0, 0)
    val startDate = calendar.getTime
    calendar.set(2013, 9, 6, 0, 0, 0)
    val endDate = calendar.getTime
    val requestUrl = daoImpl.getRequestUrl("GOOG", Some(startDate), Some(endDate))
    val expectedUrl = "http://ichart.yahoo.com/table.csv?s=GOOG&g=d&ignore=.csv&a=7&b=1&c=2013&d=8&e=6&f=2013"
    assert(requestUrl === expectedUrl)
  }

  test("Test construct yahoo url only with start date to fetch historical stock price.") {
    val daoImpl = new YahooStockPriceDaoImpl
    val calendar = Calendar.getInstance()
    calendar.set(2013, 8, 1, 0, 0, 0)
    val startDate = calendar.getTime
    val requestUrl = daoImpl.getRequestUrl("GOOG", Some(startDate), None)
    val expectedUrl = "http://ichart.yahoo.com/table.csv?s=GOOG&g=d&ignore=.csv&a=7&b=1&c=2013"
    assert(requestUrl === expectedUrl)
  }

  test("Test construct yahoo url only with end date to fetch historical stock price.") {
    val daoImpl = new YahooStockPriceDaoImpl
    val calendar = Calendar.getInstance()
    calendar.set(2013, 8, 1, 0, 0, 0)
    val endDate = calendar.getTime
    val requestUrl = daoImpl.getRequestUrl("GOOG", None, Some(endDate))
    val expectedUrl = "http://ichart.yahoo.com/table.csv?s=GOOG&g=d&ignore=.csv&d=7&e=1&f=2013"
    assert(requestUrl === expectedUrl)
  }

  test("Test construct yahoo url without both start date and end date to fetch historical stock price.") {
    val daoImpl = new YahooStockPriceDaoImpl
    val requestUrl = daoImpl.getRequestUrl("GOOG", None, None)
    val expectedUrl = "http://ichart.yahoo.com/table.csv?s=GOOG&g=d&ignore=.csv"
    assert(requestUrl === expectedUrl)
  }

  test("Test raw data pattern definition.") {
    val dataLine = "2013-09-06,882.44,883.78,873.74,879.58,1561600,879.58"

    val DataRegex = YahooStockPriceDaoImpl.RAW_DATA_PATTERN.r

    println(DataRegex.findFirstIn(dataLine))
    dataLine match {
      case DataRegex(date, open, high, low, close, volume, adjustedClose) => {
        assert(date === "2013-09-06")
        assert(open === "882.44")
        assert(high === "883.78")
        assert(low === "873.74")
        assert(close === "879.58")
        assert(volume === "1561600")
        assert(adjustedClose === "879.58")
      }
      case _ => throw new RuntimeException("There is something wrong with" +
        " the data pattern definition to process the stock price data.")
    }
  }
}
