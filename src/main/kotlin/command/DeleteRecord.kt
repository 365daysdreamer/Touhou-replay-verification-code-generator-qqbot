package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.RandOperationHistory

object DeleteRecord : CommandHandler {
    override val name = "删除记录"

    override fun showTips(groupCode: Long, senderId: Long) = "$name 对方QQ号"

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        return if (target.isEmpty()) {
            if (RandOperationHistory.deleteRecord(event.sender.id)) {
                PlainText("QQ${event.sender.id}的记录删除成功！")
            } else {
                PlainText("QQ${event.sender.id}没有随机操作记录！")
            }
        } else {
            val (succeed, failed) = target
                .filter { it == event.sender.id || PermData.isAdmin(event.sender.id) }
                .partition { RandOperationHistory.deleteRecord(it) }
            val result =
                if (succeed.isNotEmpty()) {
                    succeed.joinToString(separator = "\n", prefix = "已清除记录：\n")
                } else {
                    failed.joinToString(separator = "\n", postfix = "该记录为空或你不是管理员")
                }
            PlainText(result)
        }
    }
}