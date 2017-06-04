package com.jalandis.set

import scala.util.Random

object Game {
  type Deck = List[Card]

  def getFullDeck(): Deck = for {
    colorId <- List.range(0, Colors.maxId)
    shapeId <- List.range(0, Shapes.maxId)
    fillId  <- List.range(0, Fills.maxId)
    countId <- List.range(0, Counts.maxId)
  } yield (new Card(Colors(colorId), Shapes(shapeId), Fills(fillId), Counts(countId)))

  def shuffle(cards: Deck): Deck = Random.shuffle(cards)
  def pullHand(cards: Deck): (Deck, Deck) = cards.splitAt(12)
}
