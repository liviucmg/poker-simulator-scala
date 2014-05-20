package org.poker

object Helper {
  case class CommunityCard (card: Int)
  case class PrivateCard (card: Int)
  case class Turn (currentBet: Int)
  case class PlayerCheck (playerId: Int, score: Int)
  case class PlayerCall (playerId: Int, score: Int, additionalPot: Int)
  case class PlayerRaise (playerId: Int, score: Int, additionalPot:Int, additionalBet: Int)
  case class PlayerFold (playerId: Int)

  val random = new scala.util.Random

  def getRandomInt(min: Int, max: Int): Int = {
    min + random.nextInt(max - min)
  }

  // Get a random float between 0 and 1.
  def getRandomFloat(): Float = {
    random.nextFloat()
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
        case 9 => "J"
        case 10 => "Q"
        case 11 => "K"
        case 12 => "A"
        case default => default + 2
      }
    }

    val color = {
      getCardColor(card) match {
        case 0 => "♠"
        case 1 => "♥"
        case 2 => "♦"
        case 3 => "♣"
      }
    }

    number + color
  }
}
