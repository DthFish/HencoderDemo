package com.dthfish.plugindemo

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils


class DemoTransform extends Transform {

    @Override
    String getName() {
        return "DthFishTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {


        def inputs = transformInvocation.inputs
        def outputProvider = transformInvocation.outputProvider
        inputs.each {
            it.directoryInputs.each {
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, dest)
            }
            it.jarInputs.each {
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes, it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, dest)
            }
        }
        /*inputs.each {
            // jarInputs:各个依赖所编译成的 jar ⽂文件
            it.jarInputs.each {
                // dest:
                //./app/build/intermediates/transforms/hencoderTransform/...
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes,
                        it.scopes, Format.JAR)
                FileUtils.copyFile(it.file, dest)
            }
            // directoryInputs:本地 project 编译成的多个 class ⽂文件存
            it.directoryInputs.each {
                // dest:
                //./app/ build / intermediates / transforms / hencoderTransform / ...
                File dest = outputProvider.getContentLocation(it.name, it.contentTypes,
                        it.scopes, Format.DIRECTORY)
                FileUtils.copyDirectory(it.file, dest)
            }
        }*/
    }
}