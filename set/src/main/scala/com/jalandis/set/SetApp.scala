package com.jalandis.set

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{ColumnConstraints, GridPane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.shape.{ArcTo, CubicCurveTo, FillRule, HLineTo, LineTo, MoveTo, Path, Polygon, Rectangle, Shape, VLineTo}

object SetApp extends JFXApp {
  val (hand, deck) = Game.pullHand(Game.shuffle(Game.getFullDeck()))
  val colInfo = new ColumnConstraints(minWidth = 100, prefWidth = 100, maxWidth = 100)
  val rowInfo = new RowConstraints(minHeight = 150, prefHeight = 150, maxHeight = 150)

  var selected = List[Card]()

  def getColor(card: Card): Color = {
    card.color match {
      case Colors.Red => Color.Red
      case Colors.Green => Color.SeaGreen
      case Colors.Purple => Color.Purple
    }
  }

  def getFill(card: Card): Color = {
    card.fill match {
      case Fills.Full => getColor(card)
      case Fills.Empty => Color.White
      case Fills.Striped => Color.White
    }
  }

  def validSet(a: Card, b: Card, c: Card): Boolean = {
    matchColor(a, b, c) && matchShape(a, b, c) && matchFill(a, b, c) && matchCount(a, b, c)
  }

  def matchColor(a: Card, b: Card, c: Card): Boolean = {
    val same = a.color.id == b.color.id && a.color.id == c.color.id
    val different = a.color.id != b.color.id && a.color.id != c.color.id && b.color.id != c.color.id

    same || different
  }

  def matchShape(a: Card, b: Card, c: Card): Boolean = {
    val same = a.shape.id == b.shape.id && a.shape.id == c.shape.id
    val different = a.shape.id != b.shape.id && a.shape.id != c.shape.id && b.shape.id != c.shape.id

    same || different
  }

  def matchFill(a: Card, b: Card, c: Card): Boolean = {
    val same = a.fill.id == b.fill.id && a.fill.id == c.fill.id
    val different = a.fill.id != b.fill.id && a.fill.id != c.fill.id && b.fill.id != c.fill.id

    same || different
  }

  def matchCount(a: Card, b: Card, c: Card): Boolean = {
    val same = a.count.id == b.count.id && a.count.id == c.count.id
    val different = a.count.id != b.count.id && a.count.id != c.count.id && b.count.id != c.count.id

    same || different
  }

  def getCard(card: Card, row: Int, col: Int): StackPane = {
    val cardView = new Rectangle {
      width = 100
      height = 150
      stroke = Color.Black
      fill = Color.White
    }
    val shapeBox = new VBox {
      spacing = 5
      alignment = Pos.Center
      children = card.shape match {
        case Shapes.Oval => List.fill(card.count.id + 1)(getOval(card))
        case Shapes.Diamond => List.fill(card.count.id + 1)(getDiamond(card))
        case Shapes.Squiggle => List.fill(card.count.id + 1)(getSquiggle(card))
      }
      onMousePressed = {

        // NO-NO
        _ => {
          selected = card :: selected
          println(selected)
          if (selected.length == 3) {
            if (validSet(selected(0), selected(1), selected(2))) {
              println("Valid set")
            } else {
              println("Invalid set")
            }

            selected = List[Card]()
          }
        }
      }
    }
    val stack = new StackPane()
    stack.getChildren().addAll(cardView, shapeBox)
    GridPane.setConstraints(stack, row, col)
    stack
  }

  def getOval(card: Card): Shape = new Path {
    elements ++= List(
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

  stage = new JFXApp.PrimaryStage {
    title.value = "Set!"
    scene = new Scene {
      fill = Color.Grey
      content = new GridPane {
        hgap = 6
        vgap = 6
        padding = Insets(18)
        for (i <- 0 until 3) {
          rowConstraints.add(rowInfo)
          columnConstraints.add(colInfo)
          children ++= Seq(
            getCard(hand(i * 4), 0, i),
            getCard(hand(i * 4 + 1), 1, i),
            getCard(hand(i * 4 + 2), 2, i),
            getCard(hand(i * 4 + 3), 3, i)
          )
        }
        rowConstraints.add(rowInfo)
        columnConstraints.add(colInfo)
      }
    }
  }
}