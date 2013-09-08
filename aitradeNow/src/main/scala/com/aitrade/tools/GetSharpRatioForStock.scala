package com.aitrade.tools

import com.aitrade.dao.DaoManager
import java.util.Calendar
import com.aitrade.performance.{PriceSeqToReturnRateSeq, CalculatorFactory}
import com.aitrade.StockPrice

object GetSharpRatioForStock extends App {

  println("Calculate sharp ratio for the stock ... ")
  val stockPriceDao = DaoManager.getStockPriceDao()
  val calendar = Calendar.getInstance()
  calendar.set(2013,1,1,0,0,0)
  val startDate = calendar.getTime
  val priceSeq = stockPriceDao.getHistoricalStockPrice(args(0), Some(startDate), None)
  val priceArray = priceSeq.map{case x: StockPrice => x.adjustedClose}.toArray
  for(p <- priceArray) println(p)

  val srCalculator = CalculatorFactory.getSharpRatioCalculator()
  val sr = srCalculator.getSharpRatio(PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceArray))
  val mean = srCalculator.getMean(PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceArray))
  val sd = srCalculator.getStandardDeviation(PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceArray))
  println("Sharp ratio for stock " + args(0) + " since 2013-01-01 is " + sr)
  println("Mean is " + mean)
  println("Standard deviation is " + sd)
}
