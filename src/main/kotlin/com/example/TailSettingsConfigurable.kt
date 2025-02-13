package com.example

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

class TailSettingsConfigurable(private val project: Project) : Configurable {
    private var successTextField: JBTextField? = null
    private var commandTextField: JBTextField? = null
    private val settings get() = TailPluginSettings.getInstance(project)

    override fun getDisplayName(): String = "Tail Plugin Settings"

    override fun createComponent(): JComponent {
        successTextField = JBTextField(settings.successText)
        commandTextField = JBTextField(settings.command)

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Success Text: "), successTextField!!)
            .addLabeledComponent(JBLabel("Command: "), commandTextField!!)
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }

    override fun isModified(): Boolean {
        return successTextField?.text != settings.successText ||
               commandTextField?.text != settings.command
    }

    override fun apply() {
        settings.successText = successTextField?.text ?: "this one"
        settings.command = commandTextField?.text ?: "tail -f log.txt"
    }

    override fun reset() {
        successTextField?.text = settings.successText
        commandTextField?.text = settings.command
    }
} 