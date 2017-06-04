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

  def getColor(card: Card): Color = {
    card.color match {
      case Colors.Red => Color.Red
      case Colors.Green => Color.Green
      case Colors.Blue => Color.Blue
    }
  }

  def getCard(card: Card, row: Int, col: Int): StackPane = {
    val cardView = new Rectangle {
      width = 100
      height = 150
      stroke = Color.Black
      fill <== when(hover) choose Color.Black otherwise Color.White
    }
    val shapeBox = new VBox {
      spacing = 5
      alignment = Pos.Center
      children = card.shape match {
        case Shapes.Oval => List.fill(card.count.id + 1)(getOval(card))
        case Shapes.Diamond => List.fill(card.count.id + 1)(getDiamond(card))
        case Shapes.Squiggle => List.fill(card.count.id + 1)(getSquiggle(card))
      }
      onMousePressed = { _ => println(card) }
    }
    val stack = new StackPane()
    stack.getChildren().addAll(cardView, shapeBox)
    GridPane.setConstraints(stack, row, col)
    stack
  }

  def getOval(card: Card): Shape = new Path {
    elements = List(
      MoveTo(35, 35),
      HLineTo(75),
      ArcTo(20, 20, 180, 75, 75, true, true),
      HLineTo(35),
      ArcTo(20, 20, 180, 35, 35, true, true)
    )
    fill = card.fill match {
      case Fills.Full => getColor(card)
      case Fills.Empty => Color.White
      case Fills.Striped => Color.Grey
    }
    stroke = getColor(card)
    strokeWidth = 2d
  }

  def getDiamond(card: Card): Shape = {
    val poly = Polygon(10, 50, 50, 30, 90, 50, 50, 70, 10, 50);
    poly.setFill(getColor(card));
    poly.setStroke(getColor(card))
    poly.setStrokeWidth(2d)
    poly
  }

  def getSquiggle(card: Card): Shape = new Path {
    elements = List(
      MoveTo(15, 35),
      CubicCurveTo(35, 20, 65, 45, 85, 35),
      CubicCurveTo(100, 30, 90, 60, 85, 65),
      CubicCurveTo(65, 75, 45, 50, 15, 65),
      CubicCurveTo(0, 70, 10, 40, 15, 35)
    )
    fill = getColor(card)
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