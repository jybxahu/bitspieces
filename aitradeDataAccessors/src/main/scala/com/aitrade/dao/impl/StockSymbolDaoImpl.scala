package com.aitrade.dao.impl

import com.aitrade.dao.StockSymbolDao
import com.aitrade.{StockSymbol, USStockMarket}

class StockSymbolDaoImpl extends StockSymbolDao{
  /**
   * Given a stock market, return a list of company symbol which is traded in the specified market.
   * @param market
   * @return a list of company symbol
   */
  def getAllSymbolsForMarket(market: USStockMarket.USStockMarket): Seq[StockSymbol] = {
    market match {
      case USStockMarket.NASDAQ => new NASDAQStockSymbolDaoImpl().getAllSymbols()
    }
  }
}
