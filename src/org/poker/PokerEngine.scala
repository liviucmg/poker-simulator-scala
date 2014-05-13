package org.poker

import scala.actors.Actor
import Helper.randomNumber

class PokerEngine(numberOfPlayers: Int) extends Actor {
  var players = List[Player]()

  var dealer: Dealer = null

  // The number of remaining cards in the deck.
  var numberOfCardsInDeck = 52

  // True if the card is still in the deck, false otherwise.
  val cardsInDeck = Array.fill[Boolean](52)(true)

  override def start(): Actor = synchronized {
    super.start()

    for (i <- 1 to numberOfPlayers) {
      val player = new Player
      player.start()
      players :+ player
    }

    dealer = new Dealer
    dealer.start();

    this
  }

  def act() {
    // Deal 2 cards to each player.
    for (i <- 1 to numberOfPlayers) {
      for (j <- 1 to 2) {
        val card = drawCard()
      }
    }

    // Deal 3 community cards and notify all players.
    for (j <- 1 to 3) {
      for (i <- 1 to numberOfPlayers) {
        val card = drawCard()
      }
    }
  }

  // Draw a random card from the deck.
  def drawCard() {
    val n = randomNumber(1, numberOfCardsInDeck)

    // Get the nth available card from the deck.
    var j = 0;
    for (i <- 0 to cardsInDeck.length - 1) {
      if (cardsInDeck(i) == true) {
        j += 1
        if (j == n) {
          numberOfCardsInDeck -= 1
          i
        }
      }
    }
  }
}