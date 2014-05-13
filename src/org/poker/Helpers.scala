package org.poker

object Helpers {
  val random = new scala.util.Random

  def randomNumber(min: Int, max: Int):
    Int = min + random.nextInt(max - min)
}
