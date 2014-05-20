package org.poker

import Helper.randomNumber

object Main {
  case class CommunityCard (card: Int)

  def main(args: Array[String]) {
    val numberOfPlayers = randomNumber(2, 22)

    val pokerEngine = new PokerEngine(numberOfPlayers)
    pokerEngine.start()
  }
}