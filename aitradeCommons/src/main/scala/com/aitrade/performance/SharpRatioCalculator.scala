package com.aitrade.performance

trait SharpRatioCalculator {

  /**
   * Given a list of return rate, calculate the sharp ratio for the sequence
   * @param returnRateSeq a list of return rate
   * @return sharp ratio mean return rate divided by standard deviation
   */
  def getSharpRatio(returnRateSeq: Array[Double]): Double

  def getMean(returnRateSeq: Array[Double]): Double

  def getStandardDeviation(returnRateSeq: Array[Double]): Double
}
