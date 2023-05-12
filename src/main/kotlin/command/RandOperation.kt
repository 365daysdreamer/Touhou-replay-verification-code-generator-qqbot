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

    override fun showTips(groupCode: Long, senderId: Long) = "$name <标签>"

    override fun showInstruction(groupCode: Long, senderId: Long) = """
        $name <标签>
        获得一串由[↑, ↓, ←, →]组成的随机方向，玩家需要在直播+实录的同时，在关底boss对话过程中通过方向键输入进游戏里
        在配置文件里可以设置验证码的长度，默认为10位验证码
        标签可以用来描述验证码的用途，也可以不填
        标签不能包含空格
    """.trimIndent()

    override fun checkAuth(groupCode: Long, senderId: Long) = true

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
            event.sender.id, content.split(" ", limit=2)[0], record.toString()
        )
        return PlainText(text)
    }

    private val randOperations = arrayOf("↑", "↓", "←", "→")
}
