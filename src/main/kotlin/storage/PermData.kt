package org.stg.verification.bot.storage

import net.mamoe.mirai.console.data.*
import org.stg.verification.bot.CommandHandler

object PermData : AutoSavePluginData("PermData") {
    @ValueDescription("管理员")
    var admin: List<Long> by value(listOf())

    fun isAdmin(qq: Long) =
        TRVGConfig.isSuperAdmin(qq) || qq in admin

    fun getPermLevel(qq: Long) = when {
        TRVGConfig.isSuperAdmin(qq) -> CommandHandler.PermLevel.SUPER_ADMIN
        qq in admin -> CommandHandler.PermLevel.ADMIN
        else -> CommandHandler.PermLevel.NORMAL
    }

    fun addAdmin(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq in admin) return false
            admin += qq
            return true
        }
    }

    fun removeAdmin(qq: Long): Boolean {
        synchronized(PermData) {
            if (qq !in admin) return false
            admin -= qq
            return true
        }
    }

    fun listAdmin(): LongArray {
        synchronized(PermData) {
            return longArrayOf(TRVGConfig.qq.superAdminQQ, *admin.toLongArray())
        }
    }
}
