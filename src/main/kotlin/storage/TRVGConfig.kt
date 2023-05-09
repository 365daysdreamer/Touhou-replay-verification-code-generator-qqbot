package org.stg.verification.bot.storage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.console.data.*

object TRVGConfig : AutoSavePluginConfig("TRVGConfig") {
    @Serializable
    class QQConfig(
        @SerialName("super_admin_qq")
        /** 主管理员QQ号 */
        val superAdminQQ: Long,

        @SerialName("qq_group")
        /** 主要功能的QQ群 */
        val qqGroup: LongArray,
    )

    @ValueDescription("QQ相关配置")
    val qq: QQConfig by value(
        QQConfig(
            superAdminQQ = 12345678,
            qqGroup = longArrayOf(12345678)
        )
    )

    fun isSuperAdmin(qq: Long) =
        qq == this.qq.superAdminQQ

    @Serializable
    class RandOperationConfig(
        /** 一个验证码包含的随机操作的次数 */
        val number: Int,

        /** 查询时显示验证码个数的限制 */
        val limit: Int,
    )

    @ValueName("random_operation")
    @ValueDescription("随机操作的次数配置")
    val randOperation: RandOperationConfig by value(
        RandOperationConfig(
            number = 10,
            limit = 10
        )
    )
}
