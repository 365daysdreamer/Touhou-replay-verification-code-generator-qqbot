package org.stg.verification.bot.storage

import net.mamoe.mirai.console.data.*

object RandOperationHistory : AutoSavePluginData("RandOperationHistory") {
    @ValueName("history")
    @ValueDescription("随机操作历史记录")
    var history: MutableMap<Long, MutableMap<String, String>> by value(hashMapOf())

    fun addRecord(qq: Long, index: String?, record: String) {
        synchronized(RandOperationHistory) {
            if (!history.containsKey(qq)) {
                if (index.isNullOrEmpty())
                    history[qq] = mutableMapOf("1" to record)
                else
                    history[qq] = mutableMapOf(index to record)
            } else {
                if (index.isNullOrEmpty()) {
                    for (i in 1..Int.MAX_VALUE) {
                        if (!history[qq]!!.containsKey((history[qq]!!.size + i).toString())) {
                            history[qq]!![(history[qq]!!.size + i).toString()] = record
                            break
                        }
                    }
                } else {
                    history[qq]!![index] = record
                }
            }
            history
        }
    }

    fun deleteRecord(qq: Long, key: String): Boolean {
        synchronized(RandOperationHistory) {
            if (!history.containsKey(qq))
                return false
            if (!history[qq]!!.containsKey(key))
                return false
            history[qq]!!.remove(key)
            history
            return true
        }
    }

    fun clearRecord(qq: Long): Boolean {
        synchronized(RandOperationHistory) {
            if (!history.containsKey(qq))
                return false
            history -= qq
            return true
        }
    }

    fun getRecord(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            val result = history[qq]?.flatMap {
                (key, value) -> listOf("$key：$value")
            } ?: return null
            if (result.size <= TRVGConfig.randOperation.queryLimit) return result
            return result.subList(result.size - TRVGConfig.randOperation.queryLimit, result.size)
        }
    }

    fun getAllRecord(qq: Long): List<String>? {
        synchronized(RandOperationHistory) {
            return history[qq]?.flatMap {
                (key, value) -> listOf("$key：$value")
            }
        }
    }
}
