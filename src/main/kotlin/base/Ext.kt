package base

import base.bean.PullResultInfo
import mu.KotlinLogging

//扩展方法文件

private val logger = KotlinLogging.logger {}

fun String.convertPullResult(): PullResultInfo? {
    logger.info { "convertPullResult result is : $this" }
    return null
}