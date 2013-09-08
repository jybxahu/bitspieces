package com.aitrade.dao

import java.util.Date
import com.aitrade.StockPrice

trait StockPriceDao {

  /**
   * Given a stock symbol, return a list of historical daily price for the given stock.
   * startDate and endDate are optional parameter, if startDate is None, this interface
   * should return the price since the stock is listed. If the endDate is None, this
   * interface shoudl return the price to Now.
   * @param symbol stock symbol
   * @param startDate start date of the price data series
   * @param endDate end date fo the price data series
   */
  def getHistoricalStockPrice(symbol: String, startDate: Option[Date], endDate: Option[Date]): Seq[StockPrice]
}
