package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.RandOperationHistory

object ClearRecord : CommandHandler {
    override val name = "清除记录"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.ADMIN

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <@某人|QQ号>
        清除该用户申请的全部随机操作记录，参数为空则是清除自己的，只有管理员才能清除别人的操作记录
        支持同时清除多位用户的记录，用空格隔开
    """.trimIndent()

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        return if (target.isEmpty()) {
            if (RandOperationHistory.clearRecord(event.sender.id)) {
                PlainText("QQ${event.sender.id}的记录清除成功！")
            } else {
                PlainText("QQ${event.sender.id}没有随机操作记录！")
            }
        } else {
            val (succeed, _) = target.partition {
                if (it == event.sender.id || PermData.isAdmin(event.sender.id))
                    RandOperationHistory.clearRecord(it)
                else
                    false
            }
            val result =
                if (succeed.isNotEmpty()) {
                    succeed.joinToString(separator = "\n", prefix = "已清除记录：\n")
                } else {
                    "该记录为空或你不是管理员"
                }
            PlainText(result)
        }
    }
}
