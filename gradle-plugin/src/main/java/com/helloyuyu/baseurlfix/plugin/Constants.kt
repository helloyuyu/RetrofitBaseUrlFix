package com.helloyuyu.baseurlfix.plugin

/**
 * @author xjs
 * @date 2019/7/26
 */
object Constants {

    const val ANNOTATION_GET = "retrofit2.http.GET"
    const val ANNOTATION_DELETE = "retrofit2.http.DELETE"
    const val ANNOTATION_POST = "retrofit2.http.POST"
    const val ANNOTATION_PUT = "retrofit2.http.PUT"
    const val ANNOTATION_HEAD = "retrofit2.http.HEAD"
    const val ANNOTATION_HTTP = "retrofit2.http.HTTP"
    const val ANNOTATION_OPTIONS = "retrofit2.http.OPTIONS"

    val sAnnotations = arrayOf(
        ANNOTATION_GET, ANNOTATION_DELETE, ANNOTATION_POST, ANNOTATION_PUT,
        ANNOTATION_HEAD, ANNOTATION_HTTP, ANNOTATION_OPTIONS
    )
}