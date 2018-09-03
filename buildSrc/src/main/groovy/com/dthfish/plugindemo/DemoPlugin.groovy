package com.dthfish.plugindemo

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println "========================"
        println "hello gradle plugin!"
        println "========================"
        def personExtension = project.extensions.create("person", PersonExtension.class)
        project.afterEvaluate {
            println("Name is ${personExtension.name}")
        }

//        def android = project.extensions.findByType(AppExtension)
//        def transform = new DemoTransform()
//        android.registerTransform(transform)

    }

}