package com.swsdn.processing

import processing.core.PApplet
import java.time.zone.ZoneRulesProvider

class Visualisation : PApplet() {

    companion object {
        fun start() {
            main("com.swsdn.processing.Visualisation")
        }
    }

    var size = 30f

    override fun settings() {
        size(600, 600)
    }

    override fun setup() {
        noCursor()
        fill(120f,50f,240f)
    }

    override fun draw() {
        background(0)
        ellipse(mouseX.toFloat(), mouseY.toFloat(), size, size)
    }

    override fun keyPressed() {
        println("keya code: $keyCode")
        if (keyCode == RIGHT) {
            size += 5
        } else if (keyCode == LEFT) {
            size -= 5
        }else if (keyCode == 70) {
            println("framerate: $frameRate")
        }
    }

}

fun main() {
    Visualisation.start()
}