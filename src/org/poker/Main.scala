package org.poker

import Helper.randomNumber

object Main {
  case class CommunityCard (card: Int)

  def main(args: Array[String]) {
    val numberOfPlayers = randomNumber(2, 22)

    // Create players.
    var players = List[Player]()
    for (i <- 1 to numberOfPlayers) {
      val player = new Player(i)
      player.start()
      players = players :+ player
    }

    // Create dealer.
    val dealer = new Dealer(players)
    dealer.start()
  }
}