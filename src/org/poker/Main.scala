package org.poker

import Helpers.randomNumber

object Main {
  def main(args: Array[String]) {
    val numberOfPlayers = randomNumber(2, 22)

    val pokerEngine = new PokerEngine(numberOfPlayers)
    pokerEngine.start()
  }
}