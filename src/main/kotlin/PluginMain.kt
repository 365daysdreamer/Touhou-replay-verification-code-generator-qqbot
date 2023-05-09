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
        version = "1.0.0"
    )
) {
    override fun onEnable() {
        TRVGConfig.reload()
        PermData.reload()
        RandOperationHistory.reload()
        initHandler(GroupMessageEvent::class, CommandHandler::handle)
    }

    private fun <E : Event> initHandler(eventClass: KClass<out E>, handler: suspend (E) -> Unit) {
        globalEventChannel().subscribeAlways(
            eventClass,
            CoroutineExceptionHandler { _, throwable ->
                logger.error(throwable)
            },
            priority = EventPriority.MONITOR,
        ) {
            launch { handler(this@subscribeAlways) }
        }
    }
}
