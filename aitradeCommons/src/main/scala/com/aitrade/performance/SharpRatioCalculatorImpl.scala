package com.aitrade.performance

import org.apache.commons.math3.stat.descriptive.moment.{Mean, StandardDeviation}

class SharpRatioCalculatorImpl extends SharpRatioCalculator {
  /**
   * Given a list of return rate, calculate the sharp ratio for the sequence
   * @param returnRateSeq a list of return rate
   * @return sharp ratio mean return rate divided by standard deviation
   */
  def getSharpRatio(returnRateSeq: Array[Double]): Double = {
    val mean = getMean(returnRateSeq)
    val sd = getStandardDeviation(returnRateSeq)
    if(sd != 0d) {
      mean/sd
    } else {
      throw new RuntimeException("Standard deviation is zero while calculating the sharp ratio.")
    }
  }

  /**
   * Calculate the standard deviation
   */
  def getStandardDeviation(rates: Array[Double]):Double = {
    val standardDeviation: StandardDeviation = new StandardDeviation
    //TODO look at whether bias correction should be used for sharp ratio calculation
    standardDeviation.setBiasCorrected(false)
    standardDeviation.evaluate(rates)
  }

  /**
   * Calculate the mean
   */
  def getMean(rates: Array[Double]): Double = {
    val mean: Mean = new Mean
    mean.evaluate(rates)
  }
}
