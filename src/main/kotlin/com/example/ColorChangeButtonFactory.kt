package com.example

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class ColorChangeButtonFactory : StatusBarWidgetFactory {
    override fun getId(): String = "ColorChangeButton"

    override fun getDisplayName(): String = "Color Change Button"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget = ColorChangeButton(project)

    override fun disposeWidget(widget: StatusBarWidget) {}
} 