package com.swsdn.processing

import processing.core.PApplet

class Visualisation : PApplet() {

    companion object {
        fun start() {
            main("com.swsdn.processing.Visualisation")
        }
    }

    override fun settings() {
        size(600, 600)
    }

    override fun setup() {
        noCursor()
        fill(120f,50f,240f)
    }

    override fun draw() {
        background(0)
        val second = second().toFloat()
        ellipse(mouseX.toFloat(), mouseY.toFloat(), second, second)

    }





}