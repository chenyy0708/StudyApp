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
                InstrumentationScope.PROJECT
            ) {
                it.writeToStdout.set(true)
            }
            variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
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
            if(classContext.currentClassData.className.startsWith("com.example.study.asm")) {
                return OptimizedThreadNode(nextClassVisitor)
            }
            return if (parameters.get().writeToStdout.get()) {
                TraceClassVisitor(nextClassVisitor, PrintWriter(System.out))
            } else {
                TraceClassVisitor(nextClassVisitor, PrintWriter(File("trace_out")))
            }
        }

        override fun isInstrumentable(classData: ClassData): Boolean {
            if (classData.className.startsWith("com.example.study.asm")) {
                return true
            }
//            return checkClassFile(classData.className)
            return false
        }

        private fun checkClassFile(entryName: String): Boolean {
            return (entryName.endsWith(".class") && !entryName.startsWith("R\$")
                    && "R.class" != entryName && "BuildConfig.class" != entryName)
        }
    }


}