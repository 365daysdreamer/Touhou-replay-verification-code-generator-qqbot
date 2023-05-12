package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.TRVGConfig

object RemoveAdmin : CommandHandler {
    override val name = "删除管理员"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.SUPER_ADMIN

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun showInstruction(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        if (target.isEmpty())
            return PlainText("请指定要删除的管理员")
        if (TRVGConfig.qq.superAdminQQ in target)
            return PlainText("你不能删除自己")
        val (succeed, failed) = target.partition { PermData.removeAdmin(it) }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已删除管理员：")
            else failed.joinToString(postfix = "并不是管理员")
        return PlainText(result)
    }
}
