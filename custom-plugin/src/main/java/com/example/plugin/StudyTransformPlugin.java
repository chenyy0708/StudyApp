package com.example.plugin;

import com.android.build.gradle.AppExtension;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * Created by chenyy on 2021/6/18.
 */

public class StudyTransformPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        System.out.println("===========  doing  ============");
        AppExtension appExtension = project.getExtensions().getByType(AppExtension.class);
        appExtension.registerTransform(new MethodTransform());
    }
}
