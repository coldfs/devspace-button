package com.example

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "DevspaceBackgroundSettings",
    storages = [Storage("DevspaceBackgroundSettings.xml")]
)
class DevspaceSettings : PersistentStateComponent<DevspaceSettings> {
    var successText: String = "ssh php-skypro"
    var command: String = "/usr/local/bin/devspace dev"

    override fun getState(): DevspaceSettings = this

    override fun loadState(state: DevspaceSettings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(project: Project): DevspaceSettings =
            project.getService(DevspaceSettings::class.java)
    }
} 