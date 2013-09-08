package com.aitrade.performance

import org.scalatest.FunSuite

class PriceSeqToReturnSeqTest extends FunSuite {

  test("Translate price sequence to return rate sequence.") {
    val priceSeq =  Array(1d,2d,1d,2d,1d)
    val returnSeq = PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceSeq)
    assert(returnSeq.length === 4)
    assert(returnSeq === Array(0.6931471805599453, -0.6931471805599453, 0.6931471805599453, -0.6931471805599453))
  }

  test("Translate price sequence to return rate sequence with only one price item.") {
    val priceSeq =  Array(1d)
    intercept[IllegalArgumentException] {
      PriceSeqToReturnRateSeq.getReturnRateSeqFromPriceSeq(priceSeq)
    }
  }
}
