package com.aitrade.dao.impl

import org.scalatest.FunSuite

class NASDAQStockSymbolDaoImplTest extends FunSuite {

  test("test raw data pattern definition") {

    val header = "Symbol|Security Name|Market Category|Test Issue|Financial Status|Round Lot Size"
    val dataLine = "AAIT|iShares MSCI All Country Asia Information Technology Index Fund|G|N|N|100"

    val DataRegex = NASDAQStockSymbolDaoImpl.RAW_DATA_PATTERN.r

    header match {
      case DataRegex(symbol, name, category, testIssue, financialStatus, lotSize) => {
        assert(symbol === "Symbol")
        assert(name === "Security Name")
        assert(category === "Market Category")
        assert(testIssue === "Test Issue")
        assert(financialStatus === "Financial Status")
        assert(lotSize === "Round Lot Size")
      }
      case _ => throw new RuntimeException("There is something wrong with the data " +
        "pattern definition to process the header.")
    }

    dataLine match {
      case DataRegex(symbol, name, category, test, status, lot) => {
        assert(symbol === "AAIT")
        assert(name === "iShares MSCI All Country Asia Information Technology Index Fund")
        assert(category === "G")
        assert(test === "N")
        assert(status === "N")
        assert(lot === "100")
      }
      case _ => throw new RuntimeException("There is something wrong with the data " +
        "pattern definition to process data line.")
    }
  }

}
