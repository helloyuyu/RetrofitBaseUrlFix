package com.helloyuyu.baseurlfix.plugin

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.UnknownPluginException

/**
 * @author xjs
 * @date 2019/7/26
 */
class BaseUrlFixPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(AppPlugin::class.java)) return

        System.out.println("BaseUrlFixPlugin:${project.name}")

        val appPlugin = project.plugins.getPlugin(AppPlugin::class.java)
        appPlugin.extension.registerTransform(BaseUrlFixTransform())
    }
}