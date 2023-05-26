package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.TRVGConfig

object DisableGroup : CommandHandler {
    override val name: String = "停用随机操作"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.SUPER_ADMIN

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name"

    override fun showInstruction(groupCode: Long, senderId: Long) = "$name"

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val groupId = event.group.id
        val (succeed, failed) = listOf(groupId).partition { TRVGConfig.isGroupEnabled(it) && TRVGConfig.disableGroup(it) }
        val result =
            if (succeed.isNotEmpty()) "OK"
            else failed.joinToString(postfix = "不是工作群")
        return PlainText(result)
    }
}