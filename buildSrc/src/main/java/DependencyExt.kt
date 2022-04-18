import org.gradle.api.artifacts.dsl.DependencyHandler

/**
 * Created by chenyy on 2022/4/18.
 */

fun DependencyHandler.implementationList(dependencies: List<String>) {
    dependencies.forEach { lib ->
        add("implementation", lib)
    }
}

fun DependencyHandler.apiList(dependencies: List<String>) {
    dependencies.forEach { lib ->
        add("api", lib)
    }
}

fun DependencyHandler.kaptList(dependencies: List<String>) {
    dependencies.forEach { lib ->
        add("kapt", lib)
    }
}
