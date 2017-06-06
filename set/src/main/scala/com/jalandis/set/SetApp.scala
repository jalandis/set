package com.jalandis.set

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{ColumnConstraints, GridPane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.Scene
import scalafx.scene.shape.{ArcTo, CubicCurveTo, HLineTo, LineTo, MoveTo, Path, Rectangle, Shape, VLineTo}
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.{ Add, Remove }
import com.typesafe.scalalogging.Logger
import scalafx.scene.control.{ Button, ToolBar }

object SetApp extends JFXApp {
  private val logger = Logger("set")
  private val model = new ViewModel()
  private val view = new View(model)

  stage = new JFXApp.PrimaryStage {
    title.value = "Set!"
    scene = view.scene
  }

  private class ViewModel {
    private var deck = Game()
    def getCards(count: Int): List[Card] = {
      var (hand, remaining) = Game.dealCards(deck, count)
      deck = remaining
      hand
    }

    def replaceCardView(oldCardView: CardView) {
      model.cardViews -= oldCardView
      view.cardGrid.getChildren().remove(oldCardView.node)

      if (deck.size != 0) {
        var hand = model.getCards(1)
        val cardViewModel = new CardViewModel(hand(0), oldCardView.model.row, oldCardView.model.col)
        val cardView = new CardView(cardViewModel)
        GridPane.setConstraints(cardView.node, cardView.model.row, cardView.model.col)

        model.cardViews += cardView
        view.cardGrid.getChildren().add(cardView.node)
      }
    }

    val cardViews = new ObservableBuffer[CardView]()
    cardViews.onChange((buffer, changes) => {
       changes.toList match {
        case (List(Remove(position, elements))) => logger.info("Removing element from list")
        case (List(Add(position, elements))) => {
          elements.foreach(x => {
            x.model.selected.onChange((_, _, newValue) => {
              val selected = cardViews.filter(_.model.selected())
              if (Game.validSet(selected.map(_.model.card).toList)) {
                selected.foreach(replaceCardView(_))
              }
            })
          })
        }
        case _ => logger.error("Unsupported operation")
      }
    })
  }

  private class View(model: ViewModel) {
    val cardWidth = 100
    val cardHeight = 150
    val colInfo = new ColumnConstraints(minWidth = cardWidth, prefWidth = cardWidth, maxWidth = cardWidth)
    val rowInfo = new RowConstraints(minHeight = cardHeight, prefHeight = cardHeight, maxHeight = cardHeight)
    var initialHand = model.getCards(12)

    val cardGrid = new GridPane {
      hgap = 6
      vgap = 6
      padding = Insets(18)

      // 6 columns
      columnConstraints.add(colInfo)
      columnConstraints.add(colInfo)
      columnConstraints.add(colInfo)
      columnConstraints.add(colInfo)
      columnConstraints.add(colInfo)
      columnConstraints.add(colInfo)

      // 4 rows
      rowConstraints.add(rowInfo)
      rowConstraints.add(rowInfo)
      rowConstraints.add(rowInfo)
      rowConstraints.add(rowInfo)

      children = for {
        i <- 0 until 3
        j <- 0 until 4
      } yield {
        val cardView = new CardView(new CardViewModel(initialHand(i * 4 + j), j + 1, i))
        model.cardViews += cardView
        GridPane.setConstraints(cardView.node, j + 1, i)
        cardView.node
      }
    }

    val scene = new Scene {
      fill = Color.Grey
      content = new VBox {
        children = Seq(
          new ToolBar {
            prefWidth = 500
            content = Seq(
              new Button {
                text = "More Cards"
              }, new Button {
                text = "New Game"
              })
          }, cardGrid)
      }
    }
  }
}
