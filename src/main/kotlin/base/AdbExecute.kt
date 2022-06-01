package base

import base.bean.AdbProcess
import base.bean.PullResultInfo

interface AdbExecute {
    /**
     * 查询可使用的设备列表
     */
    fun availableEquipmentList(): List<String>

    /**
     * 检查Adb的连接状态。
     */
    fun checkConnect(): Boolean

    /**
     * 从设备中拉去文件。
     * @param from 设备的文件路径。
     * @param to 主机的目标的路径。
     */
//    fun pullDeviceFile(from: String, to: String): PullResultInfo

    fun getProcessList(parentPid: Int): List<AdbProcess?>
}