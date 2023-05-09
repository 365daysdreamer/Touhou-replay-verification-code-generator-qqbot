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

    override fun showTips(groupCode: Long, senderId: Long) = name

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
        RandOperationHistory.addRecord(event.sender.id, record.toString())
        return PlainText(text)
    }

    private val randOperations = arrayOf("↑", "↓", "←", "→")
}
