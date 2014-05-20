package org.poker

import scala.actors.Actor
import org.poker.Main.{PrivateCard, CommunityCard}
import Helper._

class Player(id: Int) extends Actor {
  var cards: List[Int] = List[Int]()

  def act() {
    loop {
      react {
        case PrivateCard(card) =>
          cards = cards :+ card
          Console.println("Player " + id + ": Got private card " + getCardName(card) + ". My score is " + getScore() + ".");

        case CommunityCard(card) =>
          cards = cards :+ card
          Console.println("Player " + id + ": Got community card " + getCardName(card) + ". My score is " + getScore() + ".");
      }
    }
  }

  // Get the player's current score based on his private card and the community cards.
  def getScore(): Int = {
    var score: Int = 0

    // How many cards of each number.
    var cardsOfEachNumber: List[Int] = List.fill(13)(0);
    for (i <- 0 to cards.length - 1) {
      val number = getCardNumber(cards(i));
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
}
