package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.TRVGConfig

object AddAdmin : CommandHandler {
    override val name: String = "增加管理员"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.SUPER_ADMIN

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun showInstruction(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        val (succeed, failed) = target.partition { !TRVGConfig.isSuperAdmin(it) && PermData.addAdmin(it) }
        val result =
            if (succeed.isNotEmpty()) succeed.joinToString(prefix = "已增加管理员：")
            else failed.joinToString(postfix = "已经是管理员了")
        return PlainText(result)
    }
}
