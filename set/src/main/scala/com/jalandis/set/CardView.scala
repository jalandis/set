package com.jalandis.set

import scalafx.Includes._
import scalafx.geometry.Pos
import scalafx.scene.layout.{StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ArcTo, CubicCurveTo, HLineTo, LineTo, MoveTo, Path, Rectangle, Shape, VLineTo}
import scalafx.beans.property.{ BooleanProperty, ObjectProperty }
import scalafx.collections.ObservableBuffer

class CardViewModel(val card: Card, val row: Int, val col: Int) {
  val selected = new BooleanProperty(this, "selected", false)
}

object CardView {
    def getColor(card: Card): Color = card.color match {
    case Colors.Red => Color.Red
    case Colors.Green => Color.SeaGreen
    case Colors.Purple => Color.Purple
  }

  def getFill(card: Card): Color = card.fill match {
    case Fills.Full => getColor(card)
    case Fills.Empty => Color.White
    case Fills.Striped => Color.White
  }

  def getShape(card: Card): Shape = card.shape match {
    case Shapes.Oval => getOval(card)
    case Shapes.Diamond => getDiamond(card)
    case Shapes.Squiggle => getSquiggle(card)
  }

  def getOval(card: Card): Shape = new Path {
    elements = List(
      MoveTo(35, 35),
      HLineTo(75),
      ArcTo(20, 20, 180, 75, 75, true, true),
      HLineTo(35),
      ArcTo(20, 20, 180, 35, 35, true, true)
    )
    if (card.fill == Fills.Striped) {
      elements ++= List(
        MoveTo(25, 40),
        VLineTo(70),
        MoveTo(35, 35),
        VLineTo(75),
        MoveTo(45, 35),
        VLineTo(75),
        MoveTo(55, 35),
        VLineTo(75),
        MoveTo(65, 35),
        VLineTo(75),
        MoveTo(75, 35),
        VLineTo(75),
        MoveTo(85, 40),
        VLineTo(70)
      )
    }
    fill = getFill(card)
    stroke = getColor(card)
    strokeWidth = 2d
  }

  def getDiamond(card: Card): Shape = new Path {
    elements = List(
      MoveTo(10, 50),
      LineTo(50, 30),
      LineTo(90, 50),
      LineTo(50, 70),
      LineTo(10, 50)
    )
    if (card.fill == Fills.Striped) {
      elements ++= List(
        MoveTo(15, 48),
        VLineTo(51),
        MoveTo(25, 43),
        VLineTo(55),
        MoveTo(35, 39),
        VLineTo(60),
        MoveTo(45, 34),
        VLineTo(65),
        MoveTo(55, 34),
        VLineTo(65),
        MoveTo(65, 39),
        VLineTo(60),
        MoveTo(75, 43),
        VLineTo(55),
        MoveTo(85, 48),
        VLineTo(51)
      )
    }
    fill = getFill(card)
    stroke = getColor(card)
    strokeWidth = 2d
  }

  def getSquiggle(card: Card): Shape = new Path {
    elements = List(
      MoveTo(15, 35),
      CubicCurveTo(35, 20, 65, 45, 85, 35),
      CubicCurveTo(100, 30, 90, 60, 85, 65),
      CubicCurveTo(65, 75, 45, 50, 15, 65),
      CubicCurveTo(0, 70, 10, 40, 15, 35)
    )
    if (card.fill == Fills.Striped) {
      elements ++= List(
        MoveTo(15, 36),
        VLineTo(63),
        MoveTo(25, 31),
        VLineTo(60),
        MoveTo(35, 32),
        VLineTo(60),
        MoveTo(45, 33),
        VLineTo(59),
        MoveTo(55, 35),
        VLineTo(62),
        MoveTo(65, 37),
        VLineTo(64),
        MoveTo(75, 38),
        VLineTo(65),
        MoveTo(85, 36),
        VLineTo(64)
      )
    }
    fill = getFill(card)
    stroke = getColor(card)
    strokeWidth = 2d
  }
}

class CardView(model: CardViewModel) {
  val node: StackPane = new StackPane() {
    children = List(
      new Rectangle {
        width = 100
        height = 150
        stroke = Color.Black
        fill <== when (model.selected) choose Color.LightGrey otherwise Color.White
      },
      new VBox {
        spacing = 5
        alignment = Pos.Center
        children = List.fill(model.card.count.id + 1)(CardView.getShape(model.card))

        // Updates model on card selection
        onMousePressed = { _ => model.selected() = !model.selected() }
      }
    )
  }
}
