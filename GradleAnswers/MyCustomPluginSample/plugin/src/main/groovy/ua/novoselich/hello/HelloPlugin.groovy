package ua.novoselich.hello

import org.gradle.api.Plugin
import org.gradle.api.Project


class HelloPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("hello", HelloExtension.class)

        project.tasks.create([name:"sayMyName", type:HelloTask], {
            values = project.hello
        })

    }
}