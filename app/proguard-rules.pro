# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#You can specify any path and filename.
# 如需输出 R8 在构建项目时应用的所有规则的完整报告，用于排查R8问题
#-printconfiguration /Users/yangyichen/Desktop/full-r8-config.txt

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider

-keepclassmembers class ** implements androidx.viewbinding.ViewBinding {
    public static ** bind(***);
    public static ** inflate(***);
}

# ASM自动生成代码，生命周期初始化
-keep public class com.example.modulelike.generated.**{*;}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

#-keep class com.example.study.view.* {*;}

# 为确保对堆栈轨迹进行轨迹还原时清楚明确，您应将以下规则添加到模块的 proguard-rules.pro 文件中：
-keepattributes LineNumberTable,SourceFile
-renamesourcefileattribute SourceFile
