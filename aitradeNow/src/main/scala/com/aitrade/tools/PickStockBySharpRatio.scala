package com.aitrade.tools

import com.aitrade.dao.DaoManager
import com.aitrade.{StockPrice, StockSymbol, USStockMarket}
import java.util.concurrent.{TimeUnit, Executors, ExecutorService}
import scala.collection.mutable.ArrayBuffer
import com.aitrade.performance.{PriceSeqToReturnRateSeq, CalculatorFactory}
import java.util.concurrent.atomic.AtomicInteger
import java.util.Calendar
import java.io.FileNotFoundException

object PickStockBySharpRatio extends App {

  println("This tool scan all the listed stocks and look for the best performer measured by sharp ratio.")

  // get all the listed stocks in NASDAQ market
  val stockSymbolDao = DaoManager.getStockSymbolDao()
  val allStockSymbols = stockSymbolDao.getAllSymbolsForMarket(USStockMarket.NASDAQ)
  val sharpRatioResult = ArrayBuffer.empty[(StockSymbol, Double)]

  val calendar = Calendar.getInstance()
  calendar.set(2013,8,1,0,0,0)
  val startDate = calendar.getTime

  val counter: AtomicInteger = new AtomicInteger(0)
  val failure: AtomicInteger = new AtomicInteger(0)

  val pool: ExecutorService = Executors.newFixedThreadPool(50)

  try {
    for(stockSymbol <- allStockSymbols) {
      pool.execute(new Handler(stockSymbol))
    }
  } finally {
    pool.shutdown()
  }
  try {
    pool.awaitTermination(Long.MaxValue, TimeUnit.NANOSECONDS)
  } catch {
    case e: Exception => throw e
  }

  val sortedResult = sharpRatioResult.sortBy(_._2).reverse
  println("sharp ratio, symbol, company name")
  for(res <- sortedResult) {
    println(res._2, res._1.symbol, res._1.name)
  }

  // print total failure
  println("There are totally " + failure.get() + " failures.")

  class Handler(stock: StockSymbol) extends Runnable {
    def run() {
      try {
        val stockPriceDao = DaoManager.getStockPriceDao
        val stockPriceSeq = stockPriceDao.getHistoricalStockPrice(stock.symbol, Some(startDate), None)
        val sharpRatioCalculator = CalculatorFactory.getSharpRatioCalculator()
        val priceSeq = stockPriceSeq.map{case x: StockPrice => x.adjustedClose}.toArray
        val sharpRatio = sharpRatioCalculator.getSharpRatio(PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceSeq))
        sharpRatioResult.synchronized(
          sharpRatioResult += ((stock, sharpRatio))
        )
        if(counter.addAndGet(1) % 50 == 0) {
          println("Processed " + counter.get() + " stocks.")
        }
      } catch {
        case e : FileNotFoundException => {
          failure.addAndGet(1)
          println("Failed to fetch the data for stock: " + stock.symbol)
          if(counter.addAndGet(1) % 50 == 0) {
            println("Processed " + counter.get() + " stocks.")
          }
        }
        case e: RuntimeException => {
          println("Failed to calculate sharp ratio for stock: " + stock.symbol)
          failure.addAndGet(1)
          if(counter.addAndGet(1) % 50 == 0) {
            println("Processed " + counter.get() + " stocks.")
          }
        }
        case _ => println("There is uncaught exception.")
      }
    }
  }
}
