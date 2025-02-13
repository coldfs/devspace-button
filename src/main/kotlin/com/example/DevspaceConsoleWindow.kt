package com.example

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.swing.JTextArea
import kotlinx.coroutines.*
import com.intellij.openapi.application.ApplicationManager
import java.io.File
import java.io.IOException

class DevspaceConsoleWindow : ToolWindowFactory, Disposable {
    companion object {
        private var textArea: JTextArea? = null
        private var tailProcess: Process? = null
        private var job: Job? = null
        private var currentProject: Project? = null
        private var onLineRead: ((String) -> Unit)? = null
        
        fun setOnLineReadListener(listener: (String) -> Unit) {
            onLineRead = listener
        }
        
        fun startTailProcess() {
            stopTailProcess()
            
            try {
                val projectPath = currentProject?.basePath
                if (projectPath == null) {
                    appendMessage("Error: Cannot determine project path")
                    return
                }

                val settings = DevspaceSettings.getInstance(currentProject!!)
                val commandParts = settings.command.split(" ").toTypedArray()
                val executable = ProcessUtils.findExecutableInPath(commandParts[0]) ?: commandParts[0]
                
                val processBuilder = ProcessBuilder(
                    listOf(executable) + commandParts.drop(1)
                ).apply {
                    redirectErrorStream(true)
                    directory(File(projectPath))
                    environment()["PATH"] = System.getenv("PATH")
                }
                
                try {
                    tailProcess = processBuilder.start()
                } catch (e: IOException) {
                    appendMessage("Error starting process: ${e.message}")
                    appendMessage("Command: $executable")
                    appendMessage("Working directory: ${processBuilder.directory()}")
                    appendMessage("PATH: ${processBuilder.environment()["PATH"]}")
                    return
                }
                
                job = CoroutineScope(Dispatchers.IO).launch {
                    val reader = BufferedReader(InputStreamReader(tailProcess!!.inputStream))
                    
                    while (isActive) {
                        val line = reader.readLine() ?: break
                        ApplicationManager.getApplication().invokeLater {
                            textArea?.append("$line\n")
                            onLineRead?.invoke(line)
                        }
                    }
                }
            } catch (e: Exception) {
                ApplicationManager.getApplication().invokeLater {
                    textArea?.append("Error: ${e.message}\n")
                }
            }
        }
        
        fun stopTailProcess() {
            job?.cancel()
            tailProcess?.destroy()
            job = null
            tailProcess = null
        }
        
        fun appendMessage(message: String) {
            ApplicationManager.getApplication().invokeLater {
                textArea?.append("$message\n")
            }
        }
    }

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        currentProject = project
        val contentFactory = ContentFactory.getInstance()
        
        textArea = JTextArea().apply {
            isEditable = false
        }
        
        val scrollPane = JBScrollPane(textArea)
        val content = contentFactory.createContent(scrollPane, "", false)
        toolWindow.contentManager.addContent(content)
    }
    
    override fun dispose() {
        stopTailProcess()
        currentProject = null
        onLineRead = null
    }
} 