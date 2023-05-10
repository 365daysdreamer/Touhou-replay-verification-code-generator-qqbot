package org.stg.verification.bot

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import org.stg.verification.bot.command.*
import org.stg.verification.bot.storage.TRVGConfig

/**
 * 这是聊天指令处理器的接口，当你想要新增自己的聊天指令处理器时，实现这个接口即可。
 */
interface CommandHandler {
    /**
     * 群友输入聊天指令时，第一个空格前的内容，即指令
     */
    val name: String

    /**
     * 在【指令说明】中应该如何显示这个命令。null 表示不显示
     */
    fun showTips(groupCode: Long, senderId: Long): String?

    /**
     * 是否有权限执行这个指令
     */
    fun checkAuth(groupCode: Long, senderId: Long): Boolean

    /**
     * 提取指令中的QQ号
     * @param msg 触发指令的消息链
     * @return 如果指令中包含QQ号@target，就返回这个QQ号，否则返回null
     */
    fun extractQQ(msg: MessageChain): List<Long> {
        val target = mutableListOf<Long>()
        for (ele in msg) {
            if (ele is At && ele.target != msg.bot.id) {
                target.add(ele.target)
            } else if (ele is PlainText) {
                val qq = ele.content.split(" ").map {
                    runCatching { it.toLong() }.getOrNull()
                }
                target.addAll(qq.filterNotNull())
            }
        }
        return target.distinct()
    }

    /**
     * 执行指令
     * @param event 触发指令的事件
     * @param content 除开指令名（第一个空格前的部分）以外剩下的所有内容
     * @return 要发送的群聊消息，为空就是不发送消息
     */
    suspend fun execute(event: GroupMessageEvent, content: String): Message

    companion object {
        /**
         * 指令处理器列表
         */
        val handlers = arrayOf(
            ShowTips,
            AddAdmin, RemoveAdmin, ListAllAdmin,
            RandOperation, DeleteRecord, GetRecord, GetAllRecords
        )

        /**
         * 指令处理
         * @param e 群消息事件
         */
        suspend fun handle(e: GroupMessageEvent) {
            // 排除非工作QQ群
            if (e.group.id !in TRVGConfig.qq.qqGroup) return
            // 从群消息事件中获取消息链并忽略空消息
            val message = e.message
            if (message.size <= 1) return
            // 判断是否在@机器人
            val isAt = message.getOrNull(1)?.let { it is At && it.target == e.bot.id } ?: false
            // 获取文本消息，如果消息链中第一个元素是@机器人，则获取第二个元素
            val msg =
                if (isAt) (message.getOrNull(2) as? PlainText)?.content?.trim()
                else (message.getOrNull(1) as? PlainText)?.content?.trim()
            // 如存在文本消息，则获得之；如不存在文本消息且艾特了机器人，则视为输入了“ShowTips”指令；否则返回
            val msgContent = if (!msg.isNullOrEmpty()) msg else if (isAt) ShowTips.name else return
            // 文本消息中存在换行时，视为指令无效
            if (msgContent.contains("\n") || msgContent.contains("\r")) return
            // 将文本消息分为指令和参数两部分
            val msgSlices = msgContent.split(" ", limit = 2)
            val cmd = msgSlices[0]
            val content = msgSlices.getOrElse(1) { "" }
            // 遍历指令处理器并执行之
            handlers.forEach {
                if (it.name == cmd && it.checkAuth(e.group.id, e.sender.id)) {
                    val groupMsg = it.execute(e, content)
                    e.group.sendMessage(groupMsg)
                }
            }
        }
    }
}
