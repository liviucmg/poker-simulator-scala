package org.poker

import scala.actors.Actor
import org.poker.Main.CommunityCard
import Helper.getCardName

class Player(id: Int) extends Actor {
  var players = List[Player]()

  def act() {
    loop {
      react {
        case CommunityCard(card) =>
          Console.println("Player " + id + ": Got card " + getCardName(card));
      }
    }
  }
}
