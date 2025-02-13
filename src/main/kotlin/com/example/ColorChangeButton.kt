package com.example

import com.intellij.openapi.wm.CustomStatusBarWidget
import com.intellij.openapi.wm.StatusBar
import com.intellij.util.ui.JBUI
import java.awt.*
import javax.swing.JButton
import javax.swing.JComponent
import org.jetbrains.annotations.NotNull
import javax.swing.BorderFactory
import com.intellij.ui.JBColor
import com.intellij.ui.scale.JBUIScale
import javax.swing.Icon

class ColorChangeButton : CustomStatusBarWidget {
    private val redIcon = createIcon(Color.RED)
    private val yellowIcon = createIcon(Color.YELLOW)
    private val greenIcon = createIcon(Color.GREEN)
    
    private fun createIcon(color: Color) = object : Icon {
        override fun paintIcon(c: Component?, g: Graphics, x: Int, y: Int) {
            val g2 = g.create() as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.color = color
            g2.fillOval(x + 2, y + 2, iconWidth - 4, iconHeight - 4)
            g2.dispose()
        }
        override fun getIconWidth(): Int = JBUIScale.scale(16)
        override fun getIconHeight(): Int = JBUIScale.scale(16)
    }
    
    private val button = JButton().apply {
        icon = redIcon
        preferredSize = Dimension(JBUIScale.scale(20), JBUIScale.scale(20))
        isBorderPainted = false
        isContentAreaFilled = false
        isOpaque = false
        border = BorderFactory.createEmptyBorder()
        background = JBColor.background()
    }
    
    private var isYellow = false
    private var isTailRunning = false
    
    init {
        TimeConsoleWindow.setOnLineReadListener { line ->
            if (isTailRunning && line.trim() == "this one") {
                button.icon = greenIcon
            } else if (isTailRunning) {
                button.icon = yellowIcon
            }
        }
        
        button.addActionListener {
            isYellow = !isYellow
            
            if (isYellow) {
                button.icon = yellowIcon
                TimeConsoleWindow.startTailProcess()
                isTailRunning = true
            } else {
                button.icon = redIcon
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