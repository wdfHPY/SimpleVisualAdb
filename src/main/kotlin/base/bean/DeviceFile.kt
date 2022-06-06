package base.bean


/**
 * @param parentAbsPath 父绝对地址
 * @param subItem 子项列表。
 */
data class AdbPath(
    val parentAbsPath: String,
    val subItem: List<DeviceFile>
)

/**
 * @param isDirectory 是否是文件夹
 * @param isFile 是否是文件
 * @param name 文件的名称
 * @param createOrChangeTime 创建或者改变的时间
 * @param owner 文件/文件夹的拥有者
 * @param ownerGroup 文件/文件夹的拥有权限组
 * @param permission 文件的权限
 */
data class DeviceFile(
    val isDirectory: Boolean,
    val isFile: Boolean,
    val name: String,
    val createOrChangeTime: String,
    val owner: String,
    val ownerGroup: String,
    val permission: String
) {
    companion object {
        /**
         * 判断是否是目录。这里仅仅只是测试。
         * 目录的类别不仅仅是file和directory。暂时不区分那么多。
         */
        fun judgeIsDirectory(
            permission: String
        ): Boolean {
            return !permission.startsWith("-")
        }
    }
}