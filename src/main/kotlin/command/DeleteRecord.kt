package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.RandOperationHistory

object DeleteRecord : CommandHandler {
    override val name = "删除记录"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.NORMAL

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <标签>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <标签>
        删除自己申请的随机操作记录，不能删除别人的
        支持同时删除多个记录，用空格隔开
        参数不能为空
    """.trimIndent()

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        if (content.isEmpty()) return PlainText("参数不能为空！")
        val target = content.split(" ")
        val (succeed, _) = target.partition {
            RandOperationHistory.deleteRecord(event.sender.id, it)
        }
        val result =
            if (succeed.isNotEmpty()) {
                succeed.joinToString(separator = "\n", prefix = "已删除记录：\n")
            } else {
                "该记录不存在或标签不正确"
            }
        return PlainText(result)
    }
}
