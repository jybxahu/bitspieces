package com.aitrade.tools

import com.aitrade.dao.DaoManager
import com.aitrade.USStockMarket

object GetNASDAQListedCompanies extends App {

  println("We are working hard to fetch the symbol and company name listed in NASDAQ. Please wait a moment for the result.")
  val stockSymbolDao = DaoManager.getStockSymbolDao()
  val symbols = stockSymbolDao.getAllSymbolsForMarket(USStockMarket.NASDAQ)

  for(sym <- symbols) {
    println(sym.symbol + "    " + sym.name)
  }
}
