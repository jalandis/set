package com.jalandis.setgame

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
    val cardViews = new ObservableBuffer[CardView]()

    def getCards(count: Int): List[Card] = {
      var (hand, remaining) = Game.dealCards(deck, count)
      deck = remaining
      hand
    }

    def removeCardView(cardView: CardView) {
      model.cardViews -= cardView
      view.cardGrid.getChildren().remove(cardView.node)
    }

    def addCardView(row: Int, col: Int) {
      if (deck.size != 0) {
        var hand = model.getCards(1)
        val cardViewModel = new CardViewModel(hand(0), row, col)
        val cardView = new CardView(cardViewModel)
        GridPane.setConstraints(cardView.node, row, col)

        model.cardViews += cardView
        view.cardGrid.getChildren().add(cardView.node)
      }
    }
    def replaceCardView(cardView: CardView) {
      removeCardView(cardView)
      addCardView(cardView.model.row, cardView.model.col)
    }

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
    var initialHand = model.getCards(12)

    val cardGrid = new GridPane {
      hgap = 6
      vgap = 6
      padding = Insets(18)

      // Resize stage with new cards
      // TODO: Test other syntax
      width onChange { stage.width = width.get }

      children = for {
        i <- 0 until 3
        j <- 0 until 4
      } yield {
        val cardView = new CardView(new CardViewModel(initialHand(i * 4 + j), j, i))
        model.cardViews += cardView
        GridPane.setConstraints(cardView.node, j, i)
        cardView.node
      }
    }

    val scene = new Scene {
      fill = Color.Grey
      content = new VBox {
        fillWidth = true
        children = Seq(
          new ToolBar {
            content = Seq(
              new Button {
                text = "New Game"
                onAction = { ae => {
                  logger.error("Button not supported yet")
                }}
              },
              new Button {
                text = "More Cards"
                onAction = { ae => {
                  val row = model.cardViews.size / 3
                  model.addCardView(row, 0)
                  model.addCardView(row, 1)
                  model.addCardView(row, 2)
                }}
              }
            )
          }, cardGrid)
      }
    }
  }
}
