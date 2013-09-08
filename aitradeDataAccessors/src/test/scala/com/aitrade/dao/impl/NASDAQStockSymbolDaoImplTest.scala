package com.aitrade.dao.impl

import org.scalatest.FunSuite

class NASDAQStockSymbolDaoImplTest extends FunSuite {

  test("Test raw data pattern definition") {

    val dataLine = "AAIT|iShares MSCI All Country Asia Information Technology Index Fund|G|N|N|100"

    val DataRegex = NASDAQStockSymbolDaoImpl.RAW_DATA_PATTERN.r

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
