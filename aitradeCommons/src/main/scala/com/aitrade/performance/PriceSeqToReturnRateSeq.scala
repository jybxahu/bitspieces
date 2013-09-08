package com.aitrade.performance

import org.apache.commons.math3.analysis.function.Log

/**
 * Util class to translate the price sequence to return rate sequence
 */
object PriceSeqToReturnRateSeq {

  def getReturnRateSeqFromPriceSeq(priceSeq: Array[Double]): Array[Double] = {
    require(priceSeq.length > 1, "The length of price sequence should be greater than 1, otherwise, it could not be translated.")
    val logarithm = new Log
    priceSeq.sliding(2).collect{case x: Array[Double] => (x(0), x(1))}.map{case (x1, x2) => logarithm.value(x2/x1)}.toArray
  }
}
