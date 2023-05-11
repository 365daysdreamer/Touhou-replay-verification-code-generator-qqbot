package org.stg.verification.bot.storage

import net.mamoe.mirai.console.data.*

object RandOperationHistory : AutoSavePluginData("RandOperationHistory") {
    @ValueName("history")
    @ValueDescription("随机操作历史记录")
    var history: MutableMap<Long, MutableList<String>> by value(hashMapOf())

    fun addRecord(qq: Long, record: String) {
        synchronized(RandOperationHistory) {
            history =
                if (!history.containsKey(qq)) {
                    history[qq] = mutableListOf(record)
                    history
                } else {
                    history[qq]!!.add(record)
                    history
                }
        }
    }

    fun deleteRecord(qq: Long): Boolean {
        synchronized(RandOperationHistory) {
            if (!history.containsKey(qq))
                return false
            history -= qq
            return true
        }
    }

    fun getRecord(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            val result = history[qq] ?: return null
            if (result.size <= TRVGConfig.randOperation.limit) return result
            return result.subList(result.size - TRVGConfig.randOperation.limit, result.size)
        }
    }

    fun getAllRecords(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            return history[qq]
        }
    }
}
