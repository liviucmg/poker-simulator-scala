import scala.actors.Actor

class PokerEngine(numberOfPlayers: Int) extends Actor {
  var players = List[Player]()
  var dealer: Dealer = null

  override def start(): Actor = synchronized {
    super.start()

    for (i <- 1 to numberOfPlayers) {
      val player = new Player
      player.start()
      players :+ player
    }

    dealer = new Dealer
    dealer.start();

    this
  }

  def act() {

  }
}