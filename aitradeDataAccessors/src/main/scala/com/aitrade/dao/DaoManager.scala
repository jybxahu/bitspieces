package com.aitrade.dao

import com.aitrade.dao.impl.{YahooStockPriceDaoImpl, StockSymbolDaoImpl}
import com.aitrade.USStockMarket.USStockMarket

object DaoManager {

  //TODO whether to create symbol dao based on market
  def getStockSymbolDao(): StockSymbolDao = {
    new StockSymbolDaoImpl
  }

  def getStockPriceDao(): StockPriceDao = {
    new YahooStockPriceDaoImpl
  }

}
