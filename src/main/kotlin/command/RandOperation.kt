package org.stg.verification.bot.command

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import org.stg.verification.bot.CommandHandler
import org.stg.verification.bot.storage.RandOperationHistory
import org.stg.verification.bot.storage.TRVGConfig
import java.text.DateFormat
import java.util.*
import kotlin.random.Random

object RandOperation : CommandHandler {
    override val name = "随机操作"

    override val permLevel: CommandHandler.PermLevel = CommandHandler.PermLevel.NORMAL

    override val cooldown: MutableMap<Long, Long> = mutableMapOf()

    override fun showTips(groupCode: Long, senderId: Long) = "$name <标签>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <标签>
        获得一串由[↑, ↓, ←, →]组成的随机方向，玩家需要在直播+实录的同时，在关底boss对话过程中通过方向键输入进游戏里
        在配置文件里可以设置验证码的长度，当前为${TRVGConfig.randOperation.number}位验证码
        标签可以用来描述验证码的用途，也可以不填
        标签不能包含空格，且长度限制为${TRVGConfig.randOperation.tagLimit}字符
        生成相同标签的随机操作时会覆盖原有的记录
    """.trimIndent()

    override suspend fun execute(event: GroupMessageEvent, content: String): Message {
        val record = StringBuilder()
        for (i in 1..TRVGConfig.randOperation.number)
            record.append(randOperations[Random.nextInt(randOperations.size)])
        val text = record.toString()
        val now = Calendar.getInstance()
        val time = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM, Locale.CHINA)
        time.timeZone = TimeZone.getTimeZone("GMT+8:00")
        record.append("\n${time.format(now.time)}")
        RandOperationHistory.addRecord(
            event.sender.id,
            if (content.split(" ", limit=2)[0].length > TRVGConfig.randOperation.tagLimit)
                content.split(" ", limit=2)[0].substring(0, TRVGConfig.randOperation.tagLimit)
            else
                content.split(" ", limit=2)[0],
            record.toString()
        )
        return PlainText(text)
    }

    private val randOperations = arrayOf("↑", "↓", "←", "→")
}
