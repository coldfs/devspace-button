package com.example

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "TailPluginSettings",
    storages = [Storage("TailPluginSettings.xml")]
)
class TailPluginSettings : PersistentStateComponent<TailPluginSettings> {
    var successText: String = "this one"
    var command: String = "tail -f log.txt"

    override fun getState(): TailPluginSettings = this

    override fun loadState(state: TailPluginSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): TailPluginSettings =
            project.getService(TailPluginSettings::class.java)
    }
} 