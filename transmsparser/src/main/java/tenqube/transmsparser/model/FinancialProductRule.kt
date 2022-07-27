package tenqube.transmsparser.model

data class FinancialProductRule(
    val ruleId: Int,
    val sender: String,
    val repSender: String,
    val productType: Int,
    val productSubType: Int,
    val finCode: String,
    val regex: String,
    val finNameRule: String,
    val productNameRule: String?,
    val userNameRule: String?,
    val amountRule: String?,
    val yearRule: String?,
    val monthRule: String?,
    val dayRule: String?,
    val hourRule: String?,
    val minRule: String?,
    val secRule: String?,
    val optionRule: String?,
    val smsType: Int,
    val isDelete: Int,
    val priority: Int
)
