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
        var qqGroup: MutableList<Long>,
    )

    @ValueDescription("QQ相关配置")
    val qq: QQConfig by value(
        QQConfig(
            superAdminQQ = 12345678L,
            qqGroup = mutableListOf(12345678L)
        )
    )

    fun isSuperAdmin(qq: Long): Boolean =
        qq == this.qq.superAdminQQ

    fun enableGroup(groupId: Long): Boolean {
        synchronized(TRVGConfig) {
            if (groupId in qq.qqGroup) return false
            qq.qqGroup += groupId
            return true
        }
    }

    fun disableGroup(groupId: Long): Boolean {
        synchronized(TRVGConfig) {
            if (groupId !in qq.qqGroup) return false
            qq.qqGroup -= groupId
            return true
        }
    }

    fun isGroupEnabled(groupId: Long): Boolean =
        groupId in qq.qqGroup

    @Serializable
    class RandOperationConfig(
        /** 一个验证码包含的随机操作的次数 */
        val number: Int,

        @SerialName("query_limit")
        /** 查询时显示验证码个数的限制 */
        val queryLimit: Int,

        @SerialName("tag_limit")
        /** 标签的长度限制 */
        val tagLimit: Int,
    )

    @ValueName("random_operation")
    @ValueDescription("随机操作的次数配置")
    val randOperation: RandOperationConfig by value(
        RandOperationConfig(
            number = 10,
            queryLimit = 10,
            tagLimit = 20
        )
    )

    @Serializable
    class CooldownConfig(
        /** 指令冷却时间 */
        val time: Long,

        @SerialName("command_with_cd")
        /** 需要冷却的指令 */
        val commandWithCd: Array<String>,
    )

    @ValueDescription("指令冷却配置")
    val cooldown: CooldownConfig by value(
        CooldownConfig(
            time = 30L,
            commandWithCd = arrayOf(
                "查看管理员",
                "随机操作",
                "查询记录",
                "查询全部记录"
            )
        )
    )

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
fun String.replace(replaceMap: Map<String, SingleMessage>): MessageChain {
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
