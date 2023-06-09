package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData

object ListAllAdmin : CommandHandler {
    override val name = "查看管理员"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.NORMAL

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = name

    override fun showInstruction(groupCode: Long, senderId: Long) = name

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val result = PermData.listAdmin().joinToString(separator = "\n", prefix = "管理员列表：\n")
        return PlainText(result)
    }
}
