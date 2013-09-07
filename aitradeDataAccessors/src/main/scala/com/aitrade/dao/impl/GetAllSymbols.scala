package com.aitrade.dao.impl

import com.aitrade.StockSymbol

trait GetAllSymbols {

  def getAllSymbols(): Seq[StockSymbol]

}
