package org.poker

import Helper.getRandomInt

object Main {
  def main(args: Array[String]) {
    val numberOfPlayers = getRandomInt(3, 4)

    Console.println("Starting game with " + numberOfPlayers + " players.")

    // Create players.
    var players = List[Player]()
    for (i <- 0 to numberOfPlayers - 1) {
      val player = new Player(i)
      player.start()
      players = players :+ player
    }

    // Create dealer.
    val dealer = new Dealer(players)
    dealer.start()
  }
}