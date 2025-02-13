package com.example

import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import java.awt.Color
import java.awt.Dimension
import javax.swing.JButton
import javax.swing.JComponent
import org.jetbrains.annotations.NotNull

class ColorChangeButton : CustomStatusBarWidget {
    private val button = JButton().apply {
        background = Color.RED
        preferredSize = Dimension(20, 20)
    }
    
    private var isYellow = false
    private var isTailRunning = false
    
    init {
        button.addActionListener {
            isYellow = !isYellow
            button.background = if (isYellow) Color.YELLOW else Color.RED
            
            if (isYellow) {
                TimeConsoleWindow.startTailProcess()
                isTailRunning = true
            } else {
                TimeConsoleWindow.stopTailProcess()
                isTailRunning = false
            }
        }
    }

    @NotNull
    override fun ID(): String = "ColorChangeButton"

    @NotNull
    override fun getComponent(): JComponent = button

    override fun install(statusBar: StatusBar) {}

    override fun dispose() {
        if (isTailRunning) {
            TimeConsoleWindow.stopTailProcess()
        }
    }
} 