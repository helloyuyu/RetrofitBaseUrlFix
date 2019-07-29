package com.helloyuyu.baseurlfix.plugin

import com.helloyuyu.baseurlfix.annotation.BaseUrl
import javassist.CtClass
import java.io.File

/**
 * @author xjs
 * @date 2019/7/9
 */
object Utils {


    fun findAllFile(file: File): MutableList<File> {
        return if (file.isFile) {
            mutableListOf(file)
        } else {
            val fileList = mutableListOf<File>()
            file.listFiles().forEach {
                fileList.addAll(findAllFile(it))
            }
            fileList
        }
    }


    /**
     * 过滤相关不处理的文件
     * 1. R文件相关
     * 2. 配置文件相关（BuildConfig）
     */
    fun filterInputFile(filePath: String): Boolean {
        return filePath.endsWith(".class") &&
                !filePath.contains("R$") &&
                !filePath.contains("R.class") &&
                !filePath.contains("BuildConfig.class")
    }

    /**
     * 筛选 retrofit Service
     */
    fun filterCtClass(ctClass: CtClass): Boolean {
        return ctClass.isInterface &&
                ctClass.interfaces.isEmpty() &&
                ctClass.hasAnnotation(BaseUrl::class.java)

    }

}