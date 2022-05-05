package com.example.plugin.agp

import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.util.TraceClassVisitor
import java.io.File
import java.io.PrintWriter

/**
 * Created by chenyy on 2022/4/28.
 */

class ASMPlugin : Plugin<Project> {
    override fun apply(project: Project) {

        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)

        androidComponents.onVariants { variant ->
            variant.transformClassesWith(
                ExampleClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.writeToStdout.set(true)
            }
            variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

    companion object {

        private const val threadClass = "java/lang/Thread"

        private const val threadFactoryClass = "java/util/concurrent/ThreadFactory"

        private const val threadFactoryNewThreadMethodDesc =
            "newThread(Ljava/lang/Runnable;)Ljava/lang/Thread;"

    }

    interface ExampleParams : InstrumentationParameters {
        @get:Input
        val writeToStdout: Property<Boolean>
    }

    abstract class ExampleClassVisitorFactory :
        AsmClassVisitorFactory<ExampleParams> {

        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor
        ): ClassVisitor {

            return if (parameters.get().writeToStdout.get()) {
                TraceClassVisitor(nextClassVisitor, PrintWriter(System.out))
            } else {
                TraceClassVisitor(nextClassVisitor, PrintWriter(File("trace_out")))
            }
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            if (classData.className.startsWith("com.example.study.asm")) {
                println("==========isInstrumentable${classData.className}====classAnnotations:${classData.classAnnotations}====classAnnotations:${classData.interfaces}====classAnnotations:${classData.superClasses}")
                return true
            }
            return false
        }
    }
}