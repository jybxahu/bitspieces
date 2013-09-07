package com.aitrade.dao

import com.aitrade.dao.impl.StockSymbolDaoImpl
import com.aitrade.USStockMarket.USStockMarket

object DaoManager {

  //TODO whether to create symbol dao based on market
  def getStockSymbolDao(market: USStockMarket): StockSymbolDao = {
    return new StockSymbolDaoImpl
  }

}
