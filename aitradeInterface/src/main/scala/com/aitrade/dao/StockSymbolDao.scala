package com.aitrade.dao

import com.aitrade.StockSymbol
import com.aitrade.USStockMarket._

trait StockSymbolDao {

  /**
   * Given a stock market, return a list of company symbol which is traded in the specified market.
   * @param market
   * @return a list of company symbol
   */
  def getAllSymbolsForMarket(market: USStockMarket): Seq[StockSymbol]
}
