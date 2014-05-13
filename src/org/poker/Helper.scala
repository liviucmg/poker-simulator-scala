package org.poker

object Helper {
  val random = new scala.util.Random

  def randomNumber(min: Int, max: Int):
    Int = min + random.nextInt(max - min)
}
