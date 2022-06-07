package base.bean


sealed class FileCategory

/**
 * 普通文件（regular file：-）
 */
object RegularFile: FileCategory()

/**
 * 块设备文件（block special file：b）
 */
object BlockFile: FileCategory()

/**
 * 字符设备文件（character special file：c）
 */
object CharacterFile: FileCategory()

/**
 * 目录文件（director file：d）
 */
object DirectorFile: FileCategory()

/**
 * 套接字文件（socket：s）
 */
object SocketFile: FileCategory()

/**
 * 管道文件（fifo：p）
 */
object FifoFile: FileCategory()

/**
 * 符号连接文件（symbolic link：l）
 */
object SymbolicFile: FileCategory()


/**
 * @param name 文件的名称
 * @param createOrChangeTime 创建或者改变的时间
 * @param owner 文件/文件夹的拥有者
 * @param ownerGroup 文件/文件夹的拥有权限组
 * @param permission 文件的权限
 */
data class DeviceFile(
    val category: FileCategory,
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
        fun judgeFileCategory(
            permission: String
        ): FileCategory {
            return  when(permission[0]) {
                '-' -> RegularFile
                'b' -> BlockFile
                'c' -> CharacterFile
                'd' -> DirectorFile
                's' -> SocketFile
                'p' -> FifoFile
                'l' -> SymbolicFile
                else -> RegularFile
            }
        }
    }
}