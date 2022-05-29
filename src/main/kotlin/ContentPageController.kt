import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

const val TASK_CONTENT_ID = 0
const val TASK_DESCRIPTION = "任务"

const val MULTITASK_CONTENT_ID = 1
const val MULTITASK_DESCRIPTION = "多任务"

const val TASK_LOGGER_ID = 2
const val TASK_LOGGER_DESCRIPTION = "任务日志"

const val TASK_LOGCAT_ID = 3
const val TASK_LOGCAT_DESCRIPTION = "Logcat"

const val TASK_SETTING_ID = 4
const val TASK_SETTING_DESCRIPTION = "设置"

/**
 * 抽象的UiContent类
 * @param pageIndex 界面的索引
 * @param pageDescription 界面的描述信息
 */
sealed class UiContentPage(
    open val pageIndex: Int,
    open val pageDescription: String
)

data class TaskPage(
    override val pageIndex: Int = TASK_CONTENT_ID,
    override val pageDescription: String = TASK_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

data class MultitaskPage(
    override val pageIndex: Int = MULTITASK_CONTENT_ID,
    override val pageDescription: String = MULTITASK_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

data class TaskLoggerPage(
    override val pageIndex: Int = TASK_LOGGER_ID,
    override val pageDescription: String = TASK_LOGGER_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

data class LogcatPage(
    override val pageIndex: Int = TASK_LOGCAT_ID,
    override val pageDescription: String = TASK_LOGCAT_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

data class SettingPage(
    override val pageIndex: Int = TASK_SETTING_ID,
    override val pageDescription: String = TASK_SETTING_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

/**
 * 主页内容的控制器。
 */
object ContentPageController {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mPageFlow: MutableStateFlow<UiContentPage> = MutableStateFlow(TaskPage())

    val pageFlow: StateFlow<UiContentPage> get() = mPageFlow

    /**
     * 导航页面通
     */
    fun navigateToPageByIndex(
        page: UiContentPage
    ) {
        scope.launch {
            mPageFlow.emit(page)
        }
    }
}