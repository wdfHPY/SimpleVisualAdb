import base.bean.DeviceFile
import base.getExecuteCommandProcessNoBlock
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object AdbShellManager {

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mAdbPathFlow: MutableStateFlow<String> = MutableStateFlow("/")

    val adbPath: StateFlow<String> get() = mAdbPathFlow

    private val mAdbDisplayPathFlow: MutableStateFlow<String> = MutableStateFlow("/")

    val adbDisplayPath: StateFlow<String> get() = mAdbDisplayPathFlow

    private var updateAdbPathFileJob: Job? = null

    private val mPathsFlow: MutableStateFlow<List<DeviceFile?>> = MutableStateFlow(emptyList())

    val pathsFlow: StateFlow<List<DeviceFile?>> get() = mPathsFlow

    /**
     * 更新任务信息。
     */
    fun updateDisplayAdbPathInfo(
        newAdbPath: String
    ) {
        scope.launch {
            mAdbDisplayPathFlow.emit(newAdbPath)
        }
    }

    /**
     * 更新任务信息。
     */
    fun updateAdbPathInfo(
        newAdbPath: String
    ) {
        scope.launch {
            mAdbPathFlow.emit(newAdbPath)
        }
    }

    suspend fun updateDevicePath(
        newPath: String
    ) {
        mPathsFlow.emit(getCurrentPath(newPath).doMapToFileList())
    }

    /**
     * 获取目录地址。
     */
    private fun getCurrentPath(
        path: String
    ): List<String> {
        return getExecuteCommandProcessNoBlock(
            "adb shell ls -al $path"
        ).getOrNull()?.inputStream?.bufferedReader()?.readLines() ?: emptyList()
    }

    private fun List<String>.doMapToFileList(): List<DeviceFile?> {
        return this.map {
            val array = it.split(" ").filter {
                it.isNotEmpty()
            }
            if (array.size <= 3)  null else {
                DeviceFile(
                    category = DeviceFile.judgeFileCategory(array.first(), array[7]),
                    name = array[7],
                    createOrChangeTime = array[5] + array[6],
                    owner = array[2],
                    ownerGroup = array[3],
                    permission = array.first()
                )
            }
        }
    }

}