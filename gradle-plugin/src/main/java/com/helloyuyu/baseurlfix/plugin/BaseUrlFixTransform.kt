package com.helloyuyu.baseurlfix.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.utils.FileUtils
import com.helloyuyu.baseurlfix.annotation.BaseUrl
import javassist.ClassPool
import javassist.CtMethod
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.annotation.Annotation
import javassist.bytecode.annotation.StringMemberValue
import java.io.File
import java.io.FileInputStream

/**
 * @author xjs
 * @date 2019/7/9
 */
class BaseUrlFixTransform : Transform() {

    override fun getName(): String = "RetrofitBaseUrlFix"

    override fun getInputTypes() =
        mutableSetOf(QualifiedContent.DefaultContentType.CLASSES)

    override fun isIncremental(): Boolean = false

    override fun getScopes() = mutableSetOf(QualifiedContent.Scope.PROJECT,
        QualifiedContent.Scope.SUB_PROJECTS)

    override fun transform(transformInvocation: TransformInvocation) {
        super.transform(transformInvocation)

        log("begin >>>>")

        val outputProvider = transformInvocation.outputProvider

        val filesMap = hashMapOf<String, String>()

        transformInvocation.inputs.forEach {

            it.directoryInputs.forEach { dirInput ->
                val destFile = outputProvider.getContentLocation(
                    dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY
                )

                filesMap[dirInput.file.absolutePath] = destFile.absolutePath
            }

//            it.jarInputs.forEach { jarInput ->
//                val outputFile = outputProvider.getContentLocation(
//                    jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR
//                )
//                outputFile.mkdirs()
//                log("jarInput:" + jarInput.file.absolutePath)
//                log("outputFile:" + outputFile.absolutePath)
//
//                val zipInputStream = ZipInputStream(FileInputStream(jarInput.file))
//                val zipOutputStream = ZipOutputStream(FileOutputStream(outputFile))
//
//                zipInputStream.use { zis ->
//                    var entry: ZipEntry? = zis.nextEntry
//                    while (entry != null) {
//                        zipOutputStream.putNextEntry(entry)
//                        ByteStreams.copy(zis,zipOutputStream)
//                        entry = zis.nextEntry
//                    }
//                    zipOutputStream.close()
//                }
//            }
        }

        val classPool = ClassPool(ClassPool.getDefault())
        filesMap.forEach { (key, value) ->
            try {
                inject(key, value, classPool)
            } catch (e: Exception) {
                log("transform:error:msg=${e.message},cause=${e.cause?.message}")
            }
        }

        log("end <<<<")
    }

    private fun inject(inputPath: String, outputPath: String, classPool: ClassPool) {
        val inputFileDir = File(inputPath)
        val fileList = Utils.findAllFile(inputFileDir)

        fileList.forEach { inputFile ->
            if (!Utils.filterInputFile(inputFile.absolutePath) ||
                !onTransform(inputFile, outputPath, classPool)
            ) {
                //不是需要处理的文件直接拷贝
                val outputFile = File(outputPath + inputFile.absolutePath.substring(inputPath.length))
                outputFile.mkdirs()
                FileUtils.copyFile(inputFile, outputFile)
            }
        }
    }

    private fun onTransform(inputFile: File, outputDir: String, classPool: ClassPool): Boolean {
        val inputStream = FileInputStream(inputFile)
        try {
            val ctClass = classPool.makeClass(inputStream)
            if (!Utils.filterCtClass(ctClass)) return false
            log("transform:${ctClass.name}")
            val baseUrl = ctClass.getAnnotation(BaseUrl::class.java) as BaseUrl
            ctClass.methods.forEach { ctMethod ->
                changeAnnotationPath(ctMethod, baseUrl.value)
            }
            ctClass.writeFile(outputDir)
            ctClass.detach()
            return true
        } catch (e: Exception) {
            log("transform:error:$e")
            e.printStackTrace()
            return false
        } finally {
            inputStream.close()
        }
    }

    /**
     * 修改 GET/HTTP... 注解的path的值
     */
    @Suppress("FoldInitializerAndIfToElvis")
    private fun changeAnnotationPath(method: CtMethod, baseUrl: String) {
        //获取运行时保留的注解信息
        val annotationsAttribute =
            method.methodInfo.getAttribute(AnnotationsAttribute.visibleTag) as? AnnotationsAttribute
        if (annotationsAttribute == null) return

        //判断是否含有 GET/POST 等注解
        val annotation: Annotation? = run findAnnotation@{
            Constants.sAnnotations.forEach { typeName ->
                val a = annotationsAttribute.getAnnotation(typeName)
                if (a != null) return@findAnnotation a
            }
            return@findAnnotation null
        }
        if (annotation == null) return

        //修改为完整的Url
        val name = if (annotation.typeName == Constants.ANNOTATION_HTTP) "path" else "value"
        val memberValue = annotation.getMemberValue(name) as StringMemberValue
        val path = memberValue.value
        val url =
            if (baseUrl.endsWith("/") && path.startsWith("/")) {
                baseUrl + path.substring(1)
            } else {
                baseUrl + path
            }
        memberValue.value = url
        log("changeAnnotation:method=${method.name} path=$path")
        log("changeAnnotation:method=${method.name} url=$url")

        //更新修改信息
        annotationsAttribute.addAnnotation(annotation)
    }

    private fun log(msg: String) {
        println("BURT:----$msg")
    }
}