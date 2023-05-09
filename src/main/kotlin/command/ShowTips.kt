package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.CommandHandler.Companion.handlers

object ShowTips : CommandHandler {
    override val name = "随机操作帮助"

    override fun showTips(groupCode: Long, senderId: Long) = null

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val tips = handlers.filter { it.checkAuth(event.group.id, event.sender.id) }
            .mapNotNull { it.showTips(event.group.id, event.sender.id) }
        val result = tips.joinToString(separator = "\n", prefix = "你可以使用以下功能：\n")
        return PlainText(result)
    }
}