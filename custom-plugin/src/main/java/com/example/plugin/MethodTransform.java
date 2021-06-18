package com.example.plugin;

import com.android.build.api.transform.DirectoryInput;
import com.android.build.api.transform.Format;
import com.android.build.api.transform.JarInput;
import com.android.build.api.transform.QualifiedContent;
import com.android.build.api.transform.Transform;
import com.android.build.api.transform.TransformException;
import com.android.build.api.transform.TransformInput;
import com.android.build.api.transform.TransformInvocation;
import com.android.build.api.transform.TransformOutputProvider;
import com.android.build.gradle.internal.pipeline.TransformManager;
import com.android.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

/**
 * Created by chenyy on 2021/6/18.
 */

public class MethodTransform extends Transform {
    @Override
    public String getName() {
        return "StudyMethodTransform";
    }

    /**
     * 需要处理的数据类型,这里表示class文件
     */
    @Override
    public Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }

    /**
     * 作用范围,子项目和本地项目  排除外部依赖 节省编译时间
     */
    @Override
    public Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }

    /**
     * 是否支持增量编译
     */
    @Override
    public boolean isIncremental() {
        return true;
    }

    @Override
    public void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        System.out.println("--------------- MethodTransform --START------------- ");
        Collection<TransformInput> inputs = transformInvocation.getInputs();
        //TransformOutputProvider管理输出路径,如果消费型输入为空,则outputProvider也为空
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider();
        //遍历inputs
        for (TransformInput input : inputs) {
            //遍历directoryInputs
            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                //处理directoryInputs
                processDirectoryInput(directoryInput, outputProvider);
            }
            //遍历jarInputs
            for (JarInput jarInput : input.getJarInputs()) {
                //处理jarInputs
                processJarInput(jarInput, outputProvider);
            }
        }
        System.out.println("'--------------- MethodTransform--END --------------- '");
    }

    void processJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(jarInput.getFile().getAbsolutePath(), jarInput.getContentTypes(), jarInput.getScopes(), Format.JAR);
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        System.out.println("拷贝文件 $dest -----");
        try {
            FileUtils.copyFile(jarInput.getFile(), dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void processDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        File dest = outputProvider.getContentLocation(directoryInput.getName(), directoryInput.getContentTypes(), directoryInput.getScopes(), Format
                .DIRECTORY);
        //将修改过的字节码copy到dest,就可以实现编译期间干预字节码的目的
        System.out.println("拷贝文件夹 $dest -----");
        try {
            FileUtils.copyDirectory(directoryInput.getFile(), dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
