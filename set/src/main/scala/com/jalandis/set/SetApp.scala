package com.jalandis.set

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.layout.{ColumnConstraints, GridPane, RowConstraints, StackPane, VBox}
import scalafx.scene.paint.Color
import scalafx.scene.Scene
import scalafx.scene.shape.{ArcTo, CubicCurveTo, HLineTo, LineTo, MoveTo, Path, Rectangle, Shape, VLineTo}
import scalafx.collections.ObservableBuffer
import scalafx.collections.ObservableBuffer.Add

object SetApp extends JFXApp {
  private val model = new ViewModel()
  private val view = new View(model)

  stage = new JFXApp.PrimaryStage {
    title.value = "Set!"
    scene = view.scene
  }

  private class ViewModel {
    var (hand, deck) = Game.dealCards(Game(), 12)
    val cardViewModels = new ObservableBuffer[CardViewModel]()

    cardViewModels.onChange((buffer, changes) => {
       changes.toList match {
        case (List(Add(position, elements))) => {
          elements.foreach(x => {
            x.selected.onChange((_, _, newValue) => {
              val selected = cardViewModels.filter(_.selected()).map(_.card)
              if (selected.size == 3) {
                if (Game.validSet(selected(0), selected(1), selected(2))) {
                  println("Valid Set")
                } else {
                  println("Invalid Set")
                }
              }
            })
          })
        }
      }
    })
  }

  private class View(model: ViewModel) {
    val cardWidth = 100
    val cardHeight = 150
    val colInfo = new ColumnConstraints(minWidth = cardWidth, prefWidth = cardWidth, maxWidth = cardWidth)
    val rowInfo = new RowConstraints(minHeight = cardHeight, prefHeight = cardHeight, maxHeight = cardHeight)

    val scene = new Scene {
      fill = Color.Grey
      content = new GridPane {
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
          val cardViewModel = new CardViewModel(model.hand(i * 4 + j), j + 1, i)
          val cardView = new CardView(cardViewModel)
          model.cardViewModels += cardViewModel
          GridPane.setConstraints(cardView.node, j + 1, i)
          cardView.node
        }
      }
    }
  }
}