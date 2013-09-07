package com.aitrade.tools

import com.aitrade.dao.DaoManager
import com.aitrade.USStockMarket

object GetNASDAQListedCompanies extends App {

  val stockSymbolDao = DaoManager.getStockSymbolDao(USStockMarket.NASDAQ)
  val symbols = stockSymbolDao.getAllSymbolsForMarket(USStockMarket.NASDAQ)

  for(sym <- symbols) {
    println(sym.symbol + "    " + sym.name)
  }
}
