package com.jalandis.setgame

import scala.util.Random

object Game {
  type Deck = List[Card]

  def apply(): Deck = Random.shuffle(for {
    colorId <- List.range(0, Colors.maxId)
    shapeId <- List.range(0, Shapes.maxId)
    fillId  <- List.range(0, Fills.maxId)
    countId <- List.range(0, Counts.maxId)
  } yield (new Card(Colors(colorId), Shapes(shapeId), Fills(fillId), Counts(countId))))

  def dealCards(cards: Deck, count: Int): (Deck, Deck) = cards.splitAt(count)

  def validSet(cards: Deck): Boolean = {
    if (cards.size == 3) {
      val a :: b :: c :: rest = cards
      validColor(a, b, c) && validShape(a, b, c) && validFill(a, b, c) && validCount(a, b, c)
    } else false
  }

  def validColor(a: Card, b: Card, c: Card): Boolean = valid(a.color.id, b.color.id, c.color.id)
  def validShape(a: Card, b: Card, c: Card): Boolean = valid(a.shape.id, b.shape.id, c.shape.id)
  def validFill(a: Card, b: Card, c: Card): Boolean = valid(a.fill.id, b.fill.id, c.fill.id)
  def validCount(a: Card, b: Card, c: Card): Boolean = valid(a.count.id, b.count.id, c.count.id)

  def valid(a: Int, b: Int, c: Int): Boolean = {
    val same = a == b && a == c
    val different = a != b && a != c && b != c

    same || different
  }
}
