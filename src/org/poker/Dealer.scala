package org.poker

import scala.actors.Actor
import org.poker.Helper._
import scala.util.control.Breaks._

class Dealer(players: List[Player]) extends Actor {
  // The number of remaining cards in the deck.
  var numberOfCardsInDeck = 52

  // True if the card is still in the deck, false otherwise.
  var cardsInDeck = Array.fill[Boolean](52)(true)

  var currentBet: Int = 50

  var currentPot: Int = 0

  var currentPlayer: Int = -1

  // The last player of this round.
  var lastPlayer: Int = 0

  var numberOfCommunityCardsDrawn = 0

  var playerHasFolded = Array.fill[Boolean](players.length)(false)

  var playerScores = Array.fill[Int](players.length)(0)

  var numberOfPlayersWhoHaveFolded = 0

  def act() {
    // Deal 2 cards to each player.
    log("Dealing 2 cards to each player.")
    for (i <- 0 to players.length - 1) {
      for (j <- 1 to 2) {
        val card = drawCard()

        players(i) ! PrivateCard(card)
      }
    }

    // Deal 3 community cards and notify all players.
    for (j <- 1 to 3) {
      drawCommunityCard()
    }

    // Allow the first player to make his turn.
    log("Starting bet is $" + currentBet + ".")
    lastPlayer = players.length - 1
    nextTurn()

    loop {
      react {
        case PlayerCheck(id, score) =>
          playerScores = playerScores.updated(id, score)
          nextTurn()

        case PlayerCall(id, score, additionalPot) =>
          playerScores = playerScores.updated(id, score)
          currentPot = currentPot + additionalPot
          nextTurn()

        case PlayerRaise(id, score, additionalPot, additionalBet) =>
          playerScores = playerScores.updated(id, score)
          currentPot = currentPot + additionalPot
          currentBet = currentBet + additionalBet
          lastPlayer = currentPlayer - 1
          if (lastPlayer == -1) {
            lastPlayer = players.length - 1
          }
          nextTurn()

        case PlayerFold(id) =>
          playerHasFolded = playerHasFolded.updated(id, true)
          numberOfPlayersWhoHaveFolded = numberOfPlayersWhoHaveFolded + 1
          nextTurn()
      }
    }
  }

  def nextTurn() {
    log("Current bet is $" +  currentBet + " and current pot is $" + currentPot + ".")

    do {
      // Check if all have folded except one.
      if (numberOfPlayersWhoHaveFolded == players.length - 1) {
        for (i <- 0 to players.length - 1) {
          if (!playerHasFolded(i)) {
            playerWins(i)
          }
        }
      }

      // Check if the round has ended.
      if (currentPlayer == lastPlayer) {
        if (numberOfCommunityCardsDrawn < 5) {
          drawCommunityCard()

          lastPlayer = currentPlayer
        }
        else {
          // Get player with maximum score.
          var maxScoreId = 0
          for (j <- 0 to players.length - 1) {
            if (playerScores(j) > playerScores(maxScoreId)) {
              maxScoreId = j
            }
          }
          playerWins(maxScoreId)
        }
      }

      // Move on to the next player.
      currentPlayer = currentPlayer + 1
      if (currentPlayer == players.length) {
        currentPlayer = 0
      }
    } while (playerHasFolded(currentPlayer))

    players(currentPlayer) ! Turn(currentBet)
  }

  // Deal a community card and notify all players.
  def drawCommunityCard() {
    val card = drawCard()
    numberOfCommunityCardsDrawn = numberOfCommunityCardsDrawn + 1
    System.out.println()
    log("New community card: " + getCardName(card) + ".");

    for (i <- 0 to players.length - 1) {
      players(i) ! CommunityCard(card)
    }
  }

  // Draw a random card from the deck.
  def drawCard(): Int = {
    val n = getRandomInt(1, numberOfCardsInDeck)

    // Get the nth available card from the deck.
    var j: Int = 0
    var card: Int = -1
    breakable {
      for (i: Int <- 0 to cardsInDeck.length - 1) {
        if (cardsInDeck(i)) {
          j += 1
          if (j == n) {
            card = i
            break()
          }
        }
      }
    }

    // Mark the card as being removed from the deck.
    if (card != -1) {
      cardsInDeck = cardsInDeck.updated(card, false)
      numberOfCardsInDeck -= 1
    }

    card
  }

  // Given player wins.
  def playerWins(id: Int) {
    log("Player " + (id + 1) + " has won the game, totalling $" + currentPot + ".")
    System.exit(0)
  }

  def log(message: String) {
    Console.println("Dealer: " + message)
  }
}
