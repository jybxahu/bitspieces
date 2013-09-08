package com.aitrade.performance

object CalculatorFactory {

  def getSharpRatioCalculator(): SharpRatioCalculator = {
    new SharpRatioCalculatorImpl
  }
}
