package com.aitrade

import java.util.Date

//TODO strong type the field
case class StockPrice (date: Date,
                       open: Double,
                       high: Double,
                       low: Double,
                       close: Double,
                       volume: Double,
                       adjustedClose: Double)
