package tenqube.parser.core;

import android.text.TextUtils;

import tenqube.parser.model.Transaction;

import static tenqube.parser.util.Validator.getValidCurrency;
import static tenqube.parser.util.Validator.getValidFullSMS;
import static tenqube.parser.util.Validator.getValidString;

class ParserMapper {

    ParserMapper() {
    }

    Transaction transform(TransactionTableData transactionTableData){

        Transaction transaction = new Transaction();

        transaction.identifier = transactionTableData.identifier;
        transaction.cardName = getValidString(transactionTableData.cardTableData.cardName);

        if(TextUtils.isEmpty(transaction.cardName))
            return null;

        transaction.cardNum = getValidString(transactionTableData.cardTableData.cardNum);
        transaction.cardType = transactionTableData.cardTableData.cardType;

        transaction.cardSubType = transactionTableData.cardTableData.cardSubType;
        transaction.parsedBalance = transactionTableData.parsedBalance;//파싱된 잔액
        transaction.balance = transactionTableData.cardTableData.balance;// 계산된 잔액

        transaction.smsId = transactionTableData.smsId;
        transaction.regId = transactionTableData.regId;


        transaction.sender = getValidString(transactionTableData.sender);
        if(TextUtils.isEmpty(transaction.sender))
            return null;

        transaction.fullSms = getValidFullSMS(transactionTableData.fullSms);

        transaction.smsDate = transactionTableData.smsDate;
        if(TextUtils.isEmpty(transaction.smsDate))
            return null;

        transaction.smsType = transactionTableData.smsType;

        transaction.spentMoney = transactionTableData.spentMoney;
        if(transaction.spentMoney == 0)
            return null;

        transaction.spentDate = transactionTableData.spentDate;
        if(TextUtils.isEmpty(transaction.spentDate))
            return null;

        transaction.finishDate = transactionTableData.finishDate;
        if(TextUtils.isEmpty(transaction.finishDate))
            return null;

        transaction.keyword = transactionTableData.keyword;
        transaction.installmentCount = transactionTableData.installmentCount;
        transaction.dwType = transactionTableData.dwType;
        transaction.currency = getValidCurrency(transactionTableData.currency);
        transaction.isOffset = transactionTableData.isOffset;
        transaction.isDuplicate = transactionTableData.isDuplicate;
        transaction.memo = transactionTableData.memo;

        transaction.spentLatitude = transactionTableData.spentLatitude;
        transaction.spentLongitude = transactionTableData.spentLongitude;
        transaction.categoryCode = transactionTableData.categoryCode;
        transaction.isSuccess = transactionTableData.isSuccess;
        transaction.isCurrentTran = transactionTableData.isCurrentTran;
        return transaction;
    }


}
