package com.aitrade.dao.impl

import com.aitrade.StockSymbol
import org.apache.commons.net.ftp.{FTPReply, FTPClient}
import java.io.{InputStreamReader, BufferedReader, IOException}
import scala.collection.mutable.ArrayBuffer
import scala.util.matching.Regex

class NASDAQStockSymbolDaoImpl extends GetAllSymbols {
  /**
   * Given a stock market, return a list of company symbol which is traded in the specified market.
   * @return a list of company symbol
   */
  def getAllSymbols: Seq[StockSymbol] = {
    val DataPattern = NASDAQStockSymbolDaoImpl.RAW_DATA_PATTERN.r
    val rawData = getRawData()
    val result = new ArrayBuffer[StockSymbol]()
    // rawData.drop(1) to ignore the first element of the data
    for(str <- rawData.drop(1)) {
      str match {
        case DataPattern(symbol, name, category, test, status, lot) => {
          // skip the test data
          if (test == "N") {
            result += StockSymbol(name, symbol)
          }
        }
        case _ => throw new RuntimeException("Unrecoganized data got from raw file of NASDAQ listed companied.")
      }
    }
    result
  }

  /**
   * Fetch the nasdaq listed company file and return the data as a sequence of String
   * The first element of the sequence is the header
   * @return
   */
  def getRawData(): Seq[String] = {
    val ftp = new FTPClient
    try {
      //try to connect to server
      ftp.connect(NASDAQStockSymbolDaoImpl.FTP_HOST)
      //login to server
      if(!ftp.login(NASDAQStockSymbolDaoImpl.FTP_USER_ID, NASDAQStockSymbolDaoImpl.FTP_PASSWORD)) {
        ftp.logout()
        throw new RuntimeException("Failed to login to ftp server: " + NASDAQStockSymbolDaoImpl.FTP_HOST)
      }

      // Handle the case connection is refused
      if (!FTPReply.isPositiveCompletion(ftp.getReplyCode)) {
        ftp.disconnect()
        println("FTP server refuse the connection.")
      }

      // change the current working directory
      if(!ftp.changeWorkingDirectory(NASDAQStockSymbolDaoImpl.NASDAQ_LISTED_DIRECTORY)) {
        throw new RuntimeException("Failed to change the working directory to " +
          NASDAQStockSymbolDaoImpl.NASDAQ_LISTED_DIRECTORY)
      }

      // retrieve the file which have the data of nasdaq listed company
      val inputStream = ftp.retrieveFileStream(NASDAQStockSymbolDaoImpl.NASDAQ_LISTED_FILE_NAME)
      val bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
      var hasMoreData = true;
      val result = new ArrayBuffer[String]()
      do {
        Option(bufferedReader.readLine()) match {
          case Some(data) => result += data
          case None => hasMoreData = false
        }
      } while (hasMoreData)
      //close buffered reader
      bufferedReader.close()

      //return the result
      result
    } catch {
      case e: IOException => throw e
    } finally {
      if (ftp.isConnected) {
        try {
          ftp.disconnect()
        } catch {
          case e: IOException => // do nothing
        }
      }
    }
  }
}

object NASDAQStockSymbolDaoImpl {
  //NASDAQ listed companies could be found in the following location
  //ftp://ftp.nasdaqtrader.com/SymbolDirectory/nasdaqlisted.txt
  val NASDAQ_LISTED_FILE_NAME = "nasdaqlisted.txt"
  val NASDAQ_LISTED_DIRECTORY = "/SymbolDirectory"
  val FTP_HOST = "ftp.nasdaqtrader.com"
  val FTP_USER_ID = "Anonymous"
  val FTP_PASSWORD = "guest"
  //Raw data pattern
  val RAW_DATA_PATTERN = """(.*)\|(.*)\|(.*)\|(.*)\|(.*)\|(.*)"""
}