package org.stg.verification.bot.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.*
import net.mamoe.mirai.message.data.*

object TRVGConfig : AutoSavePluginConfig("TRVGConfig") {
    @Serializable
    class QQConfig(
        @SerialName("super_admin_qq")
        /** 主管理员QQ号 */
        val superAdminQQ: Long,

        @SerialName("qq_group")
        /** 主要功能的QQ群 */
        val qqGroup: LongArray,
    )

    @ValueDescription("QQ相关配置")
    val qq: QQConfig by value(
        QQConfig(
            superAdminQQ = 12345678,
            qqGroup = longArrayOf(12345678)
        )
    )

    fun isSuperAdmin(qq: Long) =
        qq == this.qq.superAdminQQ

    @Serializable
    class RandOperationConfig(
        /** 一个验证码包含的随机操作的次数 */
        val number: Int,

        /** 查询时显示验证码个数的限制 */
        val limit: Int,
    )

    @ValueName("random_operation")
    @ValueDescription("随机操作的次数配置")
    val randOperation: RandOperationConfig by value(
        RandOperationConfig(
            number = 10,
            limit = 10
        )
    )

    @ValueName("cooldown")
    @ValueDescription("指令冷却时间")
    val cooldown: Long by value(30L)

    @ValueName("cooldown_msg")
    @ValueDescription(
        "指令冷却提示\n" +
                "\$quote: 引用消息\n" +
                "\$cmd: 指令名\n" +
                "\$cd: 冷却时间"
    )
    val replyCooldown: String by value("\$quote \$cmd 太快了，请 \$cd 秒后再试")
}

/**
 * 重载 String.replace, 用于替换字符串中的变量
 * @param replaceMap 变量名到变量值的映射
 * @return 替换后的消息链
 */
suspend fun String.replace(replaceMap: Map<String, SingleMessage>): MessageChain {
    if (!this.contains("\$")) return PlainText(this).toMessageChain()

    val keys = replaceMap.keys.toMutableList()
    val message = MessageChainBuilder()
    val s = this.split("\$").toMutableList()
    message.add(PlainText(s.removeAt(0)))
    s.forEach { text ->
        var isOriginal = true
        for (k in keys) {
            if (!text.startsWith(k)) continue
            replaceMap[k]?.let { message.add(it) }
            message.add(PlainText(text.removePrefix(k)))
            isOriginal = false
            break
        }
        if (isOriginal) message.add(PlainText("\$$text"))
    }
    return message.build()
}