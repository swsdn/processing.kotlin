package com.swsdn.processing

import processing.core.PApplet
import processing.core.PConstants
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.random.Random

class Visualisation : PApplet() {

    companion object {
        const val CELL_SIZE = 10
        const val SCREEN_X = 1440
        const val SCREEN_Y = 900
        const val GRID_X = SCREEN_X / CELL_SIZE
        const val GRID_Y = SCREEN_Y / CELL_SIZE

        fun start() {
            main("com.swsdn.processing.Visualisation")
        }
    }

    var threshold = 20

    data class Grid(
            val sizeX: Int,
            val sizeY: Int,
            val threshold: Int,
            val cells:List<List<Boolean>> = List(sizeX) {List(sizeY) { Random.nextInt(50) < threshold }}
    ) {

        private fun drawCell(x: Int, y: Int, draw: (x: Float, y: Float, size: Float) -> Unit) {
            if (cells[x][y]) {
                val xf = x.toFloat() * CELL_SIZE
                val yf = y.toFloat() * CELL_SIZE
                draw.invoke(xf, yf, (CELL_SIZE - 3).toFloat())
            }
        }

        fun draw(draw: (x: Float, y: Float, size: Float) -> Unit) {
            for (x in 0 until sizeX) {
                for (y in 0 until sizeY) {
                    drawCell(x, y, draw)
                }
            }
        }

        fun evolve(): Grid {
            val newCells: MutableList<MutableList<Boolean>> = MutableList(sizeX) { MutableList(sizeY) { false } }
            for (x in 0 until sizeX) {
                newCells[x] = cells[x].toMutableList()
                for (y in 0 until sizeY) {
                    val neighbours = getNeighboursCount(x, y)
                    if (neighbours < 2 || neighbours > 3) {
                        newCells[x][y] = false
                    } else if (neighbours == 3) {
                        newCells[x][y] = true
                    }
                }
            }
            return this.copy(cells = newCells)
        }

        private fun getNeighboursCount(x: Int, y: Int): Int {
            var count = 0
            for (i in -1..1) {
                for (j in -1..1) {
                    val xIdx = (x + i + sizeX) % sizeX
                    val yIdx = (y + j + sizeY) % sizeY
                    if (i == j) continue
                    if (cells[xIdx][yIdx]) {
                        count++
                    }
                }
            }
            return count
        }
    }

    private var grid = Grid(GRID_X, GRID_Y, threshold)

    override fun settings() {
        size(SCREEN_X, SCREEN_Y)
        Timer().schedule(100, 100) { grid = grid.evolve() }
    }

    override fun setup() {
        fill(120f, 50f, 240f)
    }

    override fun draw() {
        background(0)
        grid.draw { xf, yf, size -> rect(xf, yf, size, size) }
    }

    override fun keyPressed() {
        println("key code: $keyCode")
        if (keyCode == 70) {
            println("framerate: $frameRate")
        } else if (keyCode == 82) {
            grid = Grid(GRID_X, GRID_Y, threshold)
        } else if (keyCode == PConstants.UP) {
            threshold = (++threshold).coerceAtMost(50)
            println("threshold: $threshold")
        } else if (keyCode == PConstants.DOWN) {
            threshold = (--threshold).coerceAtLeast(0)
            println("threshold: $threshold")
        }
    }

}

fun main() {
    Visualisation.start()
}