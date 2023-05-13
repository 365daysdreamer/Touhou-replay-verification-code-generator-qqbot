package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.RandOperationHistory
import org.stg.verification.bot.storage.TRVGConfig

object ModifyTag : CommandHandler {
    override val name = "修改标签"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.NORMAL

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <旧标签> <新标签>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <旧标签> <新标签>
        修改随机操作记录的标签，只能修改自己的
        旧标签必须指定，新标签可以不填，会自动生成
        标签不能包含空格，且长度限制为${TRVGConfig.randOperation.tagLimit}字符
        修改为已存在的标签时会覆盖原有的记录
    """.trimIndent()

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        if (content.isEmpty()) return PlainText("参数不能为空！")
        val target = content.split(" ", limit=3)
        return PlainText(RandOperationHistory.modifyTag(
            event.sender.id,
            target[0],
            if (target.size == 1)
                ""
            else if (target[1].length > TRVGConfig.randOperation.tagLimit)
                target[1].substring(0, TRVGConfig.randOperation.tagLimit)
            else
                target[1]
        ))
    }
}
