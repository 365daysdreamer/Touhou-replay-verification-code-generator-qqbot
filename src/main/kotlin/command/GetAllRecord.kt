package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.RandOperationHistory

object GetAllRecord : CommandHandler {
    override val name = "查询全部记录"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.ADMIN

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <@某人|QQ号>
        查询该用户申请的全部随机操作记录，参数为空则是查询自己的，只有管理员才能进行此操作
        支持同时查询多位用户的记录，用空格隔开
    """.trimIndent()

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        return if (target.isEmpty()) {
            val result = RandOperationHistory.getAllRecord(event.sender.id)
            if (result.isNullOrEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        } else {
            val result = target.mapNotNull {
                qqNumber ->
                RandOperationHistory.getAllRecord(qqNumber)?.let { listOf("$qqNumber:", *it.toTypedArray()) }
            }.flatten()
            if (result.isEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        }
    }
}
