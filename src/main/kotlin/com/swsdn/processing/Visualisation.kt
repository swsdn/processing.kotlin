package com.swsdn.processing

import processing.core.PApplet
import processing.core.PConstants
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class Visualisation : PApplet() {

    companion object {
        const val CELL_SIZE = 5
        const val SCREEN_X = 1400
        const val SCREEN_Y = 900
        const val GRID_X = SCREEN_X / CELL_SIZE
        const val GRID_Y = SCREEN_Y / CELL_SIZE

        fun start() {
            main("com.swsdn.processing.Visualisation")
        }
    }

    var threshold = 10

    data class Grid(
            val sizeX: Int,
            val sizeY: Int,
            val threshold: Int,
            val cells: Array<Array<Boolean>> = Array(sizeX) { Array(sizeY) { Random.nextInt(50) < threshold } }
    ) {

        private fun drawCell(x: Int, y: Int, draw: (x: Float, y: Float, size: Float) -> Unit) {
            if (cells[x][y]) {
                val xf = x.toFloat() * CELL_SIZE
                val yf = y.toFloat() * CELL_SIZE
                draw.invoke(xf, yf, CELL_SIZE.toFloat())
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
            val cells = cells.copyOf()
            for (x in 0 until sizeX) {
                cells[x] = this.cells[x].copyOf()
                for (y in 0 until sizeY) {
                    val neighbours = getNeighboursCount(x, y)
                    if (cells[x][y] && (neighbours < 2 || neighbours > 3)) {
                        cells[x][y] = false
                    } else if (!cells[x][y] && neighbours == 3) {
                        cells[x][y] = true
                    }
                }
            }
            return Grid(sizeX, sizeY, threshold, cells)
        }

        private fun getNeighboursCount(x: Int, y: Int): Int {
            var count = 0
            for (i in -1..1) {
                for (j in -1..1) {
                    val xIdx = x + i
                    val yIdx = y + j
                    if (xIdx == yIdx || xIdx < 0 || xIdx >= sizeX || yIdx < 0 || yIdx >= sizeY) continue
                    if (cells[xIdx][yIdx]) {
                        count++
                    }
                }
            }
            return count
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Grid

            if (sizeX != other.sizeX) return false
            if (sizeY != other.sizeY) return false
            if (!cells.contentDeepEquals(other.cells)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = sizeX
            result = 31 * result + sizeY
            result = 31 * result + cells.contentDeepHashCode()
            return result
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
            threshold = (++threshold).coerceAtMost(25)
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