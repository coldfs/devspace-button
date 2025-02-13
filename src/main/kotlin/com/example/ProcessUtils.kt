package com.example

import java.io.File

object ProcessUtils {
    fun findExecutableInPath(command: String): String? {
        val paths = System.getenv("PATH")?.split(File.pathSeparator) ?: return null
        
        return paths.map { path -> 
            File(path, command)
        }.find { file ->
            file.exists() && file.canExecute()
        }?.absolutePath
    }
} 