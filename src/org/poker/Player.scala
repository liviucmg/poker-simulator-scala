package org.poker

import scala.actors.Actor
import Helper._

class Player(id: Int) extends Actor {
  var cards: List[Int] = List[Int]()

  var playerBet: Int = 0

  def act() {
    loop {
      react {
        case PrivateCard(card) =>
          cards = cards :+ card
//          log("Got private card " + getCardName(card) + ". My score is " + getScore() + ".");

        case CommunityCard(card) =>
          cards = cards :+ card
//          log("Saw community card " + getCardName(card) + ". My score is " + getScore() + ".");

        case Turn(currentBet) =>
          val score = getScore()
          logHand()

          val chancesToFold = (10000 - score.toFloat) / 100000

          if (getRandomFloat() < chancesToFold) {
            sender ! PlayerFold(id)
            log("Fold.")
          }
          else {
            val chancesToRaise = score.toFloat / 10000

            if (getRandomFloat() < chancesToRaise) {
              val raise = getRandomInt(50, 100)
              sender ! PlayerRaise(id, score, currentBet - playerBet + raise, raise)
              playerBet = currentBet + raise
              log("Raise by $" + raise + ".")
            }
            else {
              if (playerBet < currentBet) {
                val additionalPot = currentBet - playerBet
                playerBet = currentBet
                log("Call (give $" + additionalPot + ").")
                sender ! PlayerCall(id, score, additionalPot)
              }
              else {
                log("Check.")
                sender ! PlayerCheck(id, score)
              }
            }
          }
      }
    }
  }

  // Get the player's current score based on his private cards and the community cards.
  // Note that this implementation is just for show. It is incomplete and flawed.
  def getScore(): Int = {
    var score: Int = 0

    // How many cards of each number.
    var cardsOfEachNumber: List[Int] = List.fill(13)(0)
    for (i <- 0 to cards.length - 1) {
      val number = getCardNumber(cards(i))
      cardsOfEachNumber = cardsOfEachNumber.updated(number, cardsOfEachNumber(number) + 1)
    }

    // Count pairs, three of a kind and four of a kind.
    var pairs: List[Int] = List[Int]()
    var threeOfAKind: List[Int] = List[Int]()
    var fourOfAKind: List[Int] = List[Int]()
    for (i <- 0 to cardsOfEachNumber.length - 1) {
      cardsOfEachNumber(i) match {
        case 2 =>
          pairs = pairs :+ i

        case 3 =>
          threeOfAKind = threeOfAKind :+ i

        case 4 =>
          fourOfAKind = fourOfAKind :+ i

        case default =>
      }
    }

    if (fourOfAKind.length == 1) {
      // Four of a kind.
      score = 8000 + fourOfAKind(0)
    }
    else if (threeOfAKind.length == 1) {
      if (pairs.length == 1) {
        // Full house.
        score = 7000 + 13 * (threeOfAKind(0) + 1) + pairs(0)
      }
      else {
        // Three of a kind.
        score = 6000 + threeOfAKind(0)
      }
    }
    else if (pairs.length == 2) {
      // Two pairs.
      var min = 0
      var max = 0

      if (pairs(0) < pairs(1)) {
        min = pairs(0)
        max = pairs(1)
      }
      else {
        min = pairs(1)
        max = pairs(0)
      }

      score = 5000 + 13 * (max + 1) + min
    }
    else if (pairs.length == 1) {
      // One pair.
      score = 4000 + pairs(0)
    }

    score
  }

  def log(message: String) {
    Console.println("Player " + (id + 1) + ": " + message)
  }

  def logHand() {
    var cardNames: String = ""

    for (i <- 0 to cards.length - 1) {
      if (i > 0) {
        cardNames = cardNames + ", "
      }
      cardNames = cardNames + getCardName(cards(i))
    }

    log("My cards are: " + cardNames + " and my score is " + getScore() + ".")
  }
}
