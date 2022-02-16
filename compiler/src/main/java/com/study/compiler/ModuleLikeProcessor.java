package com.study.compiler;

import com.example.common.BaseAppLike;
import com.example.common.annotation.ModuleLike;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.study.interfaces.Const;
import com.study.interfaces.LikeModel;
import com.sun.tools.javac.code.Symbol;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by chenyy on 2022/2/11.
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ModuleLikeProcessor extends AbstractProcessor {

    private final LinkedHashMap<String, LikeModel> mLikeMaps = new LinkedHashMap<>();
    private String mHash = null;


    protected Filer filer;
    protected Types types;
    protected Elements elements;

    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        types = processingEnvironment.getTypeUtils();
        elements = processingEnvironment.getElementUtils();
        Map<String, String> options = processingEnv.getOptions();
        if (options!=null) {
            moduleName = options.get(Const.KEY_MODULE_NAME);
        }
        System.out.println("ModuleLikeProcessor:" + moduleName);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ModuleLike.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("================ModuleLikeProcessor" + annotations.toString() + "-----" + roundEnv.processingOver() + "------" + roundEnv.toString() + "================");

        if (!annotations.isEmpty() && !roundEnv.processingOver()) {
            Set<? extends Element> annotatedWith = roundEnv.getElementsAnnotatedWith(ModuleLike.class);
            for (Element element : annotatedWith) {
                Symbol.ClassSymbol cls = (Symbol.ClassSymbol) element;
                boolean isLike = isModuleLike(element);
                if (isLike) {
                    if (mHash == null) {
                        mHash = hash(cls.className());
                    }
                    String className = cls.toString();
                    mLikeMaps.put(className, new LikeModel(className, cls.getSimpleName() + "_" + mHash));
                }

            }
        } else if (roundEnv.processingOver()) { // 生成类
            MethodSpec load = MethodSpec.methodBuilder("load")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(BaseAppLike.class)
                    .addParameter(String.class, "clazzName")
                    .beginControlFlow("try")
                    .addStatement("$T clazz = $T.forName(clazzName)", Class.class, Class.class)
                    .addStatement("return ($T) clazz.newInstance()", BaseAppLike.class)
                    .nextControlFlow("catch($T e)", Exception.class)
                    .addStatement("e.printStackTrace()")
                    .endControlFlow()
                    .addStatement("return null")
                    .build();
            MethodSpec.Builder initBuild = MethodSpec.methodBuilder("init")
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addStatement("$T<$T> moduleLikes = new $T<>()", ArrayList.class, BaseAppLike.class, ArrayList.class);
            for (Map.Entry<String, LikeModel> entry : mLikeMaps.entrySet()) {
                initBuild.addStatement("moduleLikes.add(load($S))", entry.getValue().getClassName());
            }
            MethodSpec init = initBuild.beginControlFlow("for ($T moduleLike : moduleLikes)", BaseAppLike.class)
                    .addStatement(" moduleLike.onCreate()")
                    .endControlFlow()
                    .build();
            TypeSpec ServiceLoader = TypeSpec.classBuilder("ModuleInit_" + mHash)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(init)
                    .addMethod(load)
                    .build();
            JavaFile javaFile = JavaFile.builder(Const.MODULE_LIKE_PACKAGE_NAME, ServiceLoader)
                    .build();
            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    /**
     * 非抽象类
     */
    public boolean isConcreteType(Element element) {
        return element instanceof TypeElement && !element.getModifiers().contains(
                Modifier.ABSTRACT);
    }

    /**
     * 非抽象子类
     */
    public boolean isConcreteSubType(Element element, String className) {
        return isConcreteType(element) && isSubType(element, className);
    }

    /**
     * 从字符串获取TypeName对象，包含Class的泛型信息
     */
    public TypeName typeName(String className) {
        return TypeName.get(typeMirror(className));
    }

    public static String getClassName(TypeMirror typeMirror) {
        return typeMirror == null ? "" : typeMirror.toString();
    }

    public boolean isSubType(TypeMirror type, String className) {
        return type != null && types.isSubtype(type, typeMirror(className));
    }

    public boolean isSubType(Element element, String className) {
        return element != null && isSubType(element.asType(), className);
    }

    public boolean isSubType(Element element, TypeMirror typeMirror) {
        return element != null && types.isSubtype(element.asType(), typeMirror);
    }

    /**
     * 从字符串获取TypeElement对象
     */
    public TypeElement typeElement(String className) {
        return elements.getTypeElement(className);
    }

    /**
     * 从字符串获取TypeMirror对象
     */
    public TypeMirror typeMirror(String className) {
        return typeElement(className).asType();
    }

    public boolean isModuleLike(Element element) {
        return isConcreteSubType(element, Const.MODULE_LIKE_CLASS);
    }

    public static String hash(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return Integer.toHexString(str.hashCode());
        }
    }
}

