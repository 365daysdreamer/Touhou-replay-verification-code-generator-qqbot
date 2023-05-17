package org.stg.verification.bot

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.globalEventChannel
import org.stg.verification.bot.storage.PermData
import org.stg.verification.bot.storage.RandOperationHistory
import org.stg.verification.bot.storage.TRVGConfig
import kotlin.reflect.KClass

internal object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "org.stg.verification.bot",
        name = "Touhou Replay Verification Code Generator Bot",
        version = "1.2.4"
    )
) {
    /**
     * 插件启动时执行初始化
     */
    override fun onEnable() {
        // 重载配置和数据存储
        TRVGConfig.reload()
        PermData.reload()
        RandOperationHistory.reload()
        // 初始化处理器
        initHandler(GroupMessageEvent::class, CommandHandler::handle) // 群消息指令处理器
    }

    /**
     * 启动处理器监听
     * @param eventClass 处理器要处理的事件类型
     * @param handler 处理器
     */
    private fun <E : Event> initHandler(eventClass: KClass<out E>, handler: suspend (E) -> Unit) {
        globalEventChannel().subscribeAlways(
            eventClass,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) {
            launch { handler(this@subscribeAlways) } // 将监听获取的事件作为参数传入处理器
        }
    }
}
