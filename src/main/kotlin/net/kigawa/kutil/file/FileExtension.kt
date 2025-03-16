package net.kigawa.kutil.file

/**
 * 拡張子の管理のためのクラス
 * to manage file extension
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
enum class FileExtension(
    private val extension: String,
) {
    LOG("log"),
    ;


    /**
     * 拡張子の取得
     * get extension
     *
     * @return extension
     */
    fun getExtension(): String {
        return ".$extension"
    }

    /**
     * 拡張子を追加して返す
     * add extension and return
     *
     * @param name file name
     * @return added file name
     */
    fun addExtension(name: String): String {
        return name + getExtension()
    }

    /**
     * 拡張子を追加する
     * add extension
     *
     * @param sb file name
     * @return added string buffer
     */
    fun addExtension(sb: StringBuilder): StringBuilder {
        return sb.append(".").append(extension);
    }
}
