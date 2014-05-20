package org.poker

import Helper.randomNumber

object Main {
  case class CommunityCard (card: Int)
  case class PrivateCard (card: Int)

  def main(args: Array[String]) {
    val numberOfPlayers = randomNumber(2, 22)

    Console.println("Starting game with " + numberOfPlayers + " players.");

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