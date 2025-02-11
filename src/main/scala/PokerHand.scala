object PokerHand {
  val figuresIndex: Map[Char, Int] = List('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
    .zipWithIndex.toMap

  case class Card(figure: Char, power: Int)

  object Card {
    def parse(input: String): Card = input.toList match {
      case List(figure, _) => Card(figure, figuresIndex(figure))
    }
  }

  case class CardOccurrence(occurrence: Int, cardPower: Int, figure: Char)

  object CardOccurrence {
    val create: ((Char, Array[Card])) => CardOccurrence = {
      case ((figure, cards)) => CardOccurrence(cards.size, cards.head.power, figure)
    }
  }

  implicit val cardOccurrenceOrdering: Ordering[CardOccurrence] =
    Ordering.by[CardOccurrence, (Int, Int)](co => (co.occurrence, co.cardPower)).reverse

  def evaluate(hand: String): String = {
    val cardOccurrences: List[CardOccurrence] = hand
      .split(" ")
      .map(Card.parse)
      .groupBy(_.figure)
      .map(CardOccurrence.create)
      .toList
      .sorted

    val maxCardOccurrence = cardOccurrences.head
    if (maxCardOccurrence.occurrence == 3) {
      if (cardOccurrences(1).occurrence == 2) {
        s"Full House of : ${maxCardOccurrence.figure} and ${cardOccurrences(1).figure}"
      } else {
        s"threes of : ${maxCardOccurrence.figure}"
      }
    } else if (maxCardOccurrence.occurrence == 2) {
      if (cardOccurrences(1).occurrence == 2) {
        s"two pairs of : ${maxCardOccurrence.figure}, ${cardOccurrences(1).figure}"
      } else {
        "pair of : " + maxCardOccurrence.figure
      }
    } else {
      "high card : " + maxCardOccurrence.figure
    }
  }
}
