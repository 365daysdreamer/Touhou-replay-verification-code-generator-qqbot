package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.TRVGConfig

object RemoveAdmin : CommandHandler {
    override val name = "删除管理员"

    override fun showTips(groupCode: Long, senderId: Long) = "删除管理员 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = TRVGConfig.isSuperAdmin(senderId)

    override suspend fun execute(msg: GroupMessageEvent, content: String): Message? {
        val qqNumbers = content.split(" ").map {
            runCatching { it.toLong() }.getOrNull() ?: return null
        }
        if (TRVGConfig.qq.superAdminQQ in qqNumbers)
            return PlainText("你不能删除自己")
        val (succeed, failed) = qqNumbers.partition { PermData.removeAdmin(it) }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已删除管理员：")
            else failed.joinToString(postfix = "并不是管理员")
        return PlainText(result)
    }
}