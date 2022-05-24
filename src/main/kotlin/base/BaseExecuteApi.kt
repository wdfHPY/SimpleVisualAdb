package base

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

fun getExecuteCommandProcessNoBlock(
    cmd: String
) : Result<Process> = runCatching {
    Runtime.getRuntime().exec(cmd)
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