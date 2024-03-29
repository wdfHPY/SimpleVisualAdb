import base.bean.DeviceFile
import base.getExecuteCommandProcess
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

    private val mPathsFlow: MutableStateFlow<List<DeviceFile>> = MutableStateFlow(emptyList())

    val pathsFlow: StateFlow<List<DeviceFile>> get() = mPathsFlow

    /**
     * 仅仅支持单选。
     */
    private val mSelectedFlow: MutableStateFlow<String> = MutableStateFlow("")

    val selectedFlow: StateFlow<String> get() = mSelectedFlow

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
        val source = getCurrentPath(newPath)
        println("是否为空" + source.isEmpty())
        source.onEach {
            println("元数据  $it")
        }

        mPathsFlow.emit(filterErrorFormatStr(source).doMapToFileList())
    }

    fun updateDeviceSelected(
        fileName: String
    ) {
        scope.launch {
            mSelectedFlow.emit(fileName)
        }
    }

    /**
     * 获取目录地址。
     */
    private fun getCurrentPath(
        path: String
    ): List<String> {
        return getExecuteCommandProcessNoBlock(
            "adb shell ls -al $path"
        ).getOrThrow().inputStream?.bufferedReader()?.readLines() ?: emptyList()
    }

    private fun filterErrorFormatStr(
        list: List<String>
    ): List<String> {
        return list.filter {
            it.split(" ").filter { array ->
                array.isNotEmpty() || array.contains("?")
            }.size > 3
        }
    }

    private fun List<String>.doMapToFileList(): List<DeviceFile> {
        return this.map {
            val array = it.split(" ").filter {
                it.isNotEmpty()
            }
            println(array)

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