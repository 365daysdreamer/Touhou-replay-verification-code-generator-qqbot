package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.CommandHandler.Companion.handlers

object ShowTips : CommandHandler {
    override val name = "指令说明"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.NORMAL

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <指令名>"

    override fun showInstruction(groupCode: Long, senderId: Long) = "$name <指令名>"

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        if (content.isEmpty()) {
            val tips = handlers.filter { it.checkAuth(event.sender.id) }
                .mapNotNull { it.showTips(event.group.id, event.sender.id) }
            val result = tips.joinToString(separator = "\n", prefix = "你可以使用以下功能：\n")
            return PlainText(result)
        } else {
            for (handler in handlers) {
                if (content == handler.name) {
                    return PlainText(handler.showInstruction(event.group.id, event.sender.id)?:"")
                }
            }
            return PlainText("")
        }
    }
}
