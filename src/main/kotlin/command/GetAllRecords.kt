package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.RandOperationHistory

object GetAllRecords : CommandHandler {
    override val name = "查询全部记录"

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun checkAuth(groupCode: Long, senderId: Long) = PermData.isAdmin(senderId)

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        return if (target.isEmpty()) {
            val result = RandOperationHistory.getAllRecords(event.sender.id)
            if (result.isNullOrEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        } else {
            val result = target.mapNotNull {
                qqNumber ->
                RandOperationHistory.getAllRecords(qqNumber)?.let { listOf("$qqNumber:", *it.toTypedArray()) }
            }.flatten()
            if (result.isEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        }
    }
}
