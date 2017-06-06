package com.jalandis.set

object Colors extends Enumeration {
  val Red, Green, Purple = Value
}

object Shapes extends Enumeration {
  val Diamond, Squiggle, Oval = Value
}

object Fills extends Enumeration {
  val Empty, Striped, Full = Value
}

object Counts extends Enumeration {
  val Single, Double, Triple = Value
}

class Card(val color: Colors.Value, val shape: Shapes.Value, val fill: Fills.Value, val count: Counts.Value) {
  override def toString(): String = "(" + color + ", " + shape + ", " + fill + ", " + count + ")"
}
