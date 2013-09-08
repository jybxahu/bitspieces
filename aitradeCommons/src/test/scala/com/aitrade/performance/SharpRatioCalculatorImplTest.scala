package com.aitrade.performance

import org.scalatest.FunSuite
import org.junit.Assert

class SharpRatioCalculatorImplTest extends FunSuite {

  test("Test sharp ratio calculation with zero standard deviation.") {
    val returnRateSeq = Array(0.001,0.001,0.001,0.001,0.001)
    val sharpRatioCalculator = new SharpRatioCalculatorImpl
    intercept[Exception] {
      sharpRatioCalculator.getSharpRatio(returnRateSeq)
    }
  }
  test("Test mean calculation of happy case.") {
    val rates = Array(0.01,0.02,0.03,0.04,0.05)
    val sharpRatioCalculator = new SharpRatioCalculatorImpl
    val sharpRatio = sharpRatioCalculator.getSharpRatio(rates)
    Assert.assertEquals(sharpRatio, 2.121, 0.001)
  }

  test("Test mean calculation.") {
    val rates = Array(0.01,0.02,0.03,0.04,0.05)
    val sharpRatioCalculator = new SharpRatioCalculatorImpl
    val mean = sharpRatioCalculator.getMean(rates)
    Assert.assertEquals(mean, 0.03, 0.001)
  }

  test("Test standard deviation calculation.") {
    val rates = Array(0.01,0.02,0.03,0.04,0.05)
    val sharpRatioCalculator = new SharpRatioCalculatorImpl
    val sd = sharpRatioCalculator.getStandardDeviation(rates)
    Assert.assertEquals(sd, 0.014, 0.001)
  }
}
