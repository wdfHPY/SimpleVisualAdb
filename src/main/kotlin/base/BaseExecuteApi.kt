package base

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import java.io.InputStream
import java.io.OutputStream

/**
 * 获取执行Command的进程。当然此方法会阻塞当前的线程直到 Process 执行完成。
 * @param cmd 待执行的cmd命令。
 * @return Result<Process> Process的结果。
 */
fun getExecuteCommandProcess(
    cmd: String
): Result<Process> = runCatching {
    Runtime.getRuntime().exec(cmd).also {
        it.waitFor()
    }
}

/**
 * 获取执行Command的进程。当然此方法会阻塞当前的线程直到 Process 执行完成。
 * @param cmd 待执行的cmd命令。
 * @return Result<Process> Process的结果。
 */
fun getExecuteCommandProcessNoBlock(
    cmd: String
) : Result<Process> = runCatching {
    Runtime.getRuntime().exec(cmd)
}

/**
 * 获取非阻塞的Process的输入流。
 * @return InputStream 输入流。
 * 获取输入流的过程中是会抛出异常。需要 catch exception。
 */
fun Result<Process>.getNoBlockProcessInputStream(): InputStream {
    return getOrThrow().inputStream
}

/**
 * 获取非阻塞的Process的执行错误流。
 * @return InputStream 输入流。
 * 获取错误流的过程中是会抛出异常。需要 catch exception。
 */
fun Result<Process>.getNoBlockProcessErrorStream(): InputStream {
    return getOrThrow().errorStream
}


private val scope: CoroutineScope = CoroutineScope(CoroutineName("Execute Process Cmd Coroutine") + Dispatchers.IO)

/**
 * 非阻塞执行命令会缓存最多 350 条数据。
 * 只供内部修改，不暴露给外部的Api。
 */
private val coreProcessResultFlow: MutableSharedFlow<String> = MutableSharedFlow(
    replay = 250,
    extraBufferCapacity = 100,
    onBufferOverflow = BufferOverflow.DROP_OLDEST
)

val processResultFlow: SharedFlow<String> get() = coreProcessResultFlow

fun executeCmdInProcess(
    cmd: String
) {
    scope.launch {
        launch {
            getExecuteCommandProcess(cmd).getNoBlockProcessInputStream().bufferedReader().lines().forEach {
                launch {
                    coreProcessResultFlow.emit(it)
                }
            }
        }

        launch {
            getExecuteCommandProcess(cmd).getNoBlockProcessErrorStream().bufferedReader().lines().forEach {
                launch {
                    coreProcessResultFlow.emit(it)
                }
            }
        }
    }
}


/**
 * 获取Process的输入流。
 * @param cmd 待执行命令的输入流。
 * @return InputStream? 可空的输入流。
 */
fun getProcessInputStream(
    cmd: String
): InputStream? {
    return getExecuteCommandProcess(cmd).getOrNull()?.inputStream
}

/**
 * 获取Process的错误流。
 * @param cmd 待执行命令的输入流。
 * @return InputStream? 可空的输入流。
 */
fun getProcessErrorStream(
    cmd: String
): InputStream? {
    return getExecuteCommandProcess(cmd).getOrNull()?.errorStream
}

/**
 * 获取Process的输出流。
 * @param cmd 待执行命令的输入流。
 * @return InputStream? 可空的输入流。
 */
fun getProcessOutputStream(
    cmd: String
): OutputStream? {
    return getExecuteCommandProcess(cmd).getOrNull()?.outputStream
}

fun main() {
    runBlocking {
        executeCmdInProcess("adb shell ls -al")
        coreProcessResultFlow.collect {
            println(it)
        }
    }
}