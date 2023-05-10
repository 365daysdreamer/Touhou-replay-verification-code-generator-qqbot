package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.RandOperationHistory

object GetRecord : CommandHandler {
    override val name = "查询记录"

    override fun showTips(groupCode: Long, senderId: Long) = "$name <@某人|QQ号>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <@某人|QQ号>
        查询该用户申请的随机操作记录，参数为空则是查询自己的
        为了防止刷屏，可以设置查询时显示验证码个数的限制，默认显示最后10条验证码
        支持同时查询多位用户的记录，用空格隔开
    """.trimIndent()

    override fun checkAuth(groupCode: Long, senderId: Long) = true

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val target = extractQQ(event.message)
        return if (target.isEmpty()) {
            val result = RandOperationHistory.getRecord(event.sender.id)
            if (result.isNullOrEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        } else {
            val result = target.mapNotNull {
                qqNumber ->
                RandOperationHistory.getRecord(qqNumber)?.let { listOf("$qqNumber:", *it.toTypedArray()) }
            }.flatten()
            if (result.isEmpty()) {
                PlainText("未查询到记录")
            } else {
                PlainText(result.joinToString(separator = "\n", prefix = "随机操作记录：\n"))
            }
        }
    }
}
