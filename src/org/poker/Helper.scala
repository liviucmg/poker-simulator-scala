package org.poker

object Helper {
  val random = new scala.util.Random

  def randomNumber(min: Int, max: Int): Int = {
    min + random.nextInt(max - min)
  }

  def getCardNumber(card: Int): Int = {
    card % 13
  }

  def getCardColor(card: Int): Int = {
    card % 4
  }

  def getCardName(card: Int): String = {
    val number = {
      getCardNumber(card) match {
        case 9 => "Jack"
        case 10 => "Queen"
        case 11 => "King"
        case 12 => "Ace"
        case default => default + 2
      }
    }

    val color = {
      getCardColor(card) match {
        case 0 => "Spades"
        case 1 => "Hearts"
        case 2 => "Diamonds"
        case 3 => "Clubs"
      }
    }

    number + " of " + color
  }
}
