import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * 抽象的UiContent类
 * @param pageIndex 界面的索引
 * @param pageDescription 界面的描述信息
 */
sealed class UiContentPage(
    open val pageIndex: Int,
    open val pageDescription: String
)

const val TASK_LOG_CONTENT_ID = 5
const val TASK_LOG_DESCRIPTION = "任务的日志文件"

data class TaskLogPage(
    override val pageIndex: Int = TASK_LOG_CONTENT_ID,
    override val pageDescription: String = TASK_LOG_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

const val HOME_CONTENT_ID = 5
const val HOME_DESCRIPTION = "主页"

data class HomePage(
    override val pageIndex: Int = HOME_CONTENT_ID,
    override val pageDescription: String = HOME_DESCRIPTION
): UiContentPage(pageIndex = pageIndex, pageDescription = pageDescription)

/**
 * 主页内容的控制器。
 */
object ContentPageController {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val mPageFlow: MutableStateFlow<UiContentPage> = MutableStateFlow(HomePage())

    val pageFlow: StateFlow<UiContentPage> get() = mPageFlow

    /**
     * 导航页面通
     */
    fun navigateToPageByIndex(
        pageIndex: Int
    ) {
        scope.launch {
            mPageFlow.emit(TODO())
        }
    }
}