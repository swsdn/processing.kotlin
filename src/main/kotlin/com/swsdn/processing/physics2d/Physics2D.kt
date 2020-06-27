package com.swsdn.processing.physics2d

import processing.core.PApplet
import processing.core.PVector

fun main() {
    Physics2D.start()
}

class Physics2D : PApplet() {

    companion object {
        const val SCREEN_X = 1440
        const val SCREEN_Y = 900

        fun start() {
            main("com.swsdn.processing.physics2d.Physics2D")
        }
    }

    class Mover(
            private val pos: PVector = PVector(SCREEN_X / 5f, SCREEN_Y / 5f),
            private val vel: PVector = PVector(0f, 0f),
            private val acc: PVector = PVector(0f, 0f)
    ) {
        private val size = 48f

        fun display(draw: (x: Float, y: Float, size: Float) -> Unit) {
            draw.invoke(pos.x, pos.y, size)
        }

        fun update() {
            vel.add(acc)
            pos.add(vel)
            acc.mult(0f)
        }

        fun bounce() {
            if (pos.x <= size/2f || pos.x >= SCREEN_X - size/2) vel.x = vel.x * -1f
            if (pos.y <= size/2f || pos.y >= SCREEN_Y - size/2) vel.y = vel.y * -1f
            edges()
        }

        private fun edges() {
            val sizeHalf = size/2f
            if (pos.x < sizeHalf) pos.x = sizeHalf
            if (pos.x > SCREEN_X - sizeHalf) pos.x = SCREEN_X.toFloat() - sizeHalf
            if (pos.y < sizeHalf) pos.y = sizeHalf
            if (pos.y > SCREEN_Y - sizeHalf) pos.y = SCREEN_Y.toFloat() - sizeHalf
        }

        fun applyForce(force: PVector) {
            acc.add(force)
        }


    }

    private var mover = Mover()

    override fun settings() {
        size(SCREEN_X, SCREEN_Y)
//        Timer().schedule(100, 75) { grid = grid.evolve() }
    }

    override fun setup() {
        fill(120f, 50f, 240f)
    }

    override fun draw() {
        background(0)

        val gravity = PVector(0f, 1.981f)
        mover.applyForce(gravity)

        val wind = PVector(.1f, 0f)
        mover.applyForce(wind)

        mover.update()
        mover.bounce()
        mover.display { x, y, s -> ellipse(x, y, s, s) }
    }

    override fun keyPressed() {
        if (keyCode == 70) {
            println("framerate: $frameRate")
        } else if (keyCode == 82) {
            println("restart")
            mover = Mover()
        }
    }

}