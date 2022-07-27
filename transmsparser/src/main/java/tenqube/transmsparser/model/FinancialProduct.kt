package tenqube.transmsparser.model

data class FinancialProduct(
    val productId: String,
    val finCode: String,
    val productType: Int,
    val productSubType: Int,
    val ruleId: Int,
    val finName: String?,
    val productName: String?,
    val amount: Double?,
    val date: String?,
    val option: String?,
    val sms: SMS
)
