object Main {
  val random = new scala.util.Random

  def randomNumber(min: Int, max: Int):
    Int = min + random.nextInt(max - min)

  def main(args: Array[String]) {
    val numberOfPlayers = randomNumber(2, 22)

    new PokerEngine(numberOfPlayers)
  }
}