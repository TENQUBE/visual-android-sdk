package tenqube.parser.util

import tenqube.parser.finsmsparser.presentation.model.FinancialProduct
import tenqube.parser.finsmsparser.presentation.model.FinancialSMS
import tenqube.parser.model.FinancialProductRule
import tenqube.parser.model.SMS

object ParserMapper {

    fun toFinItems(financialProductRules: List<FinancialProductRule>) : List<tenqube.parser.finsmsparser.domain.entity.FinancialProductRule> {

        return financialProductRules.map {
            tenqube.parser.finsmsparser.domain.entity.FinancialProductRule(it.ruleId,
                    it.sender,
                    it.repSender,
                    it.productType,
                    it.productSubType,
                    it.finCode,
                    it.regex,
                    it.finNameRule,
                    it.productNameRule ?: "",
                    it.userNameRule ?: "",
                    it.amountRule ?: "",
                    it.yearRule ?: "",
                    it.monthRule ?: "",
                    it.dayRule ?: "",
                    it.hourRule ?: "",
                    it.minRule ?: "",
                    it.secRule ?: "",
                    it.optionRule ?: "",
                    it.smsType,
                    it.isDelete,
                    it.priority,
                    0)
        }

    }

    fun toFinancialSms(sms: SMS, repSender: String?): FinancialSMS {
        return FinancialSMS(sms.smsId,
                sms.fullSms,
                sms.sender,
                repSender ?: "",
                sms.smsDate,
                sms.smsType)
    }

    fun fromFinParser(financialProduct: FinancialProduct, sms: SMS): tenqube.parser.model.FinancialProduct {
        return  tenqube.parser.model.FinancialProduct(
                financialProduct.productId,
                financialProduct.finCode,
                financialProduct.productType,
                financialProduct.productSubType,
                financialProduct.ruleId,
                financialProduct.finName,
                financialProduct.productName,
                financialProduct.amount,
                financialProduct.date,
                financialProduct.option,
                sms
        )
    }

}