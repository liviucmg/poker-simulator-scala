package org.poker

import scala.actors.Actor
import org.poker.Main.{PrivateCard, CommunityCard}
import Helper.getCardName

class Player(id: Int) extends Actor {
  def act() {
    loop {
      react {
        case PrivateCard(card) =>
          Console.println("Player " + id + ": Got private card " + getCardName(card));

        case CommunityCard(card) =>
          Console.println("Player " + id + ": Got community card " + getCardName(card));
      }
    }
  }
}
