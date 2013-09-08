package com.aitrade.tools

import com.aitrade.dao.DaoManager

object GetHistoricalStockPrice extends App {

  println("We are working hard to fetch the historical stock price for you. Please wait a moment for the result.")
  val stockPriceDao = DaoManager.getStockPriceDao()
  val stockPriceSeqs = stockPriceDao.getHistoricalStockPrice("GOOG", None, None)
  for(price <- stockPriceSeqs) {
    println(price.adjustedClose)
  }
}
