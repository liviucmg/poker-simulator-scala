package org.poker

import scala.actors.Actor
import org.poker.Helper._
import scala.util.control.Breaks._
import org.poker.Main.CommunityCard

class Dealer(players: List[Player]) extends Actor {
  // The number of remaining cards in the deck.
  var numberOfCardsInDeck = 52

  // True if the card is still in the deck, false otherwise.
  val cardsInDeck = Array.fill[Boolean](52)(true)

  def act() {
    // Deal 2 cards to each player.
    for (i <- 1 to players.length) {
      for (j <- 1 to 2) {
        val card = drawCard()
      }
    }

    // Deal 3 community cards and notify all players.
    for (j <- 1 to 3) {
      val card = drawCard()

      for (i <- 0 to players.length - 1) {
        players(i) ! CommunityCard(card)
      }
    }
  }

  // Draw a random card from the deck.
  def drawCard(): Int = {
    val n = randomNumber(1, numberOfCardsInDeck)

    // Get the nth available card from the deck.
    var j: Int = 0
    var card: Int = -1
    breakable {
      for (i: Int <- 0 to cardsInDeck.length - 1) {
        if (cardsInDeck(i)) {
          j += 1
          if (j == n) {
            numberOfCardsInDeck -= 1
            card = i
            break()
          }
        }
      }
    }

    // Return the card picked.
    card
  }
}
