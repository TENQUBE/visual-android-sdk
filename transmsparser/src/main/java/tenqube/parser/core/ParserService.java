package tenqube.parser.core;

/**
 * Created by tenqube on 2017. 4. 18..
 */

import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.LinkedList;

import tenqube.parser.BuildConfig;
import tenqube.parser.BulkSmsAdapter;
import tenqube.parser.OnNetworkResultListener;
import tenqube.parser.Parser;
import tenqube.parser.constants.Constants;
import tenqube.parser.model.FinancialProduct;
import tenqube.parser.model.ParserResult;
import tenqube.parser.model.ParsingRule;
import tenqube.parser.model.ResultCode;
import tenqube.parser.model.SMS;
import tenqube.parser.model.Transaction;

import static tenqube.parser.util.LogUtil.LOGI;
import static tenqube.parser.util.LogUtil.makeLogTag;

public class ParserService implements Parser {

    public static final String TAG = makeLogTag(ParserService.class);

    private Context mContext;
    private boolean mIsBulk = false;
    public static boolean mIsDebug = false;
    private int mTranCnt;

    private ParserPresenter mParserPresenter;

    private static ParserService mInstance;

    public static ParserService getInstance(Context context) {
        synchronized (ParserService.class) {
            if (mInstance == null) {
                mInstance = new ParserService(context);
            }
            return mInstance;
        }
    }

    private ParserService(Context context) throws SQLException {
        this.mContext = context;

        this.mParserPresenter = new ParserPresenter(context);
        this.mIsBulk = false;
        this.mTranCnt = PrefUtils.getInstance(this.mContext).loadIntValue(PrefUtils.TRAN_COUNT, 50);
    }

    @Override
    public int getRuleVersion() {
        return PrefUtils.getInstance(this.mContext).loadIntValue(PrefUtils.RULE_VERSION, BuildConfig.RULE_VERSION);
    }

    @Override
    public void syncParsingRule(ParsingRule parsingRule) throws SQLException {

        if (parsingRule != null) {
            SecretKeyManager.getInstance(this.mContext).saveKey(parsingRule.securityKey);
            PrefUtils.getInstance(this.mContext).saveIntValue(PrefUtils.RULE_VERSION, parsingRule.ruleVersion);
            PrefUtils.getInstance(this.mContext).saveIntValue(PrefUtils.TRAN_COUNT, parsingRule.tranCount);
            this.mTranCnt = parsingRule.tranCount;
            this.mParserPresenter.syncParsingRule(parsingRule, this.mTranCnt);
        }
    }

    @Override
    public ParserResult parse(SMS sms) throws SQLException {

        if(PrefUtils.getInstance(this.mContext).loadIntValue(PrefUtils.RULE_VERSION, BuildConfig.RULE_VERSION) == BuildConfig.RULE_VERSION)
            return new ParserResult(ResultCode.NEED_TO_SYNC_PARSING_RULE, null, null);

        if (sms != null && sms.isValid()) {
            if (!this.mIsBulk) {
                mParserPresenter.deleteOldTransaction(sms.getSmsDate());
            }

            return mParserPresenter.parse(sms);
        } else {
            return new ParserResult(ResultCode.PARAMETER_ERROR, null, null);
        }
    }

    @Override
    public void onNetworkResult(ArrayList<Transaction> transactions, boolean isSuccess) {
        this.mParserPresenter.updateIsSuccess(transactions, isSuccess ? Constants.YES : Constants.NO);
    }

    @Override
    public void destroy() {
        if (mInstance != null) {

            if (mParseBulkTask != null) {
                mParseBulkTask.cancel(true);
                mParseBulkTask = null;
            }
        }
        mInstance = null;
    }

    @Override
    public void setDebugMode(boolean isDebug) {
        mIsDebug = isDebug;
    }

    @Override
    public void initDb() throws SQLException {

        this.mParserPresenter.initDb();
    }

    @Override
    public void addTestOriginNumber(String number) throws SQLException {
        this.mParserPresenter.addTestOriginNumber(number);
    }


    private ParserBulkTask mParseBulkTask = null;

    @Override
    public void cancelParseBulk() {
        if (mParseBulkTask != null) {
            mParseBulkTask.cancelTask();
            mParseBulkTask = null;
        }
    }

    @Override
    public String getRepSender(SMS sms) {
        return mParserPresenter.getRepSender(sms);
    }


    @Override
    public void parseBulk(BulkSmsAdapter bulkSmsAdapter) throws SQLException {

        LOGI(TAG, "Start FUNC : parseBulk");

        if(bulkSmsAdapter != null && !mIsBulk) {

            mIsBulk = true;

            if(PrefUtils.getInstance(this.mContext).loadIntValue(PrefUtils.RULE_VERSION, BuildConfig.RULE_VERSION) == BuildConfig.RULE_VERSION) {
                bulkSmsAdapter.onError(ResultCode.NEED_TO_SYNC_PARSING_RULE);
                mIsBulk = false;

            } else {
                this.cancelParseBulk();
                mParseBulkTask = new ParserBulkTask(this, bulkSmsAdapter);
                mParseBulkTask.execute();
            }

        }
        LOGI(TAG, "End FUNC : parseBulk");

    }


    private static class ParserBulkTask extends AsyncTask<Void, Void, Void> {

        private ParserService mParser;
        private BulkSmsAdapter mAdapter;
        private SendToServerThread mSendToServerThread;
        private boolean mIsCanceled;

        ParserBulkTask(ParserService parserService, BulkSmsAdapter smsAdapter) {
            super();
            this.mParser = parserService;
            this.mAdapter = smsAdapter;
            this.mIsCanceled = false;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.mParser.mParserPresenter.initTransactions();
            this.mParser.mParserPresenter.initMap();
            this.mParser.mParserPresenter.setSendersWithBulk();
            this.mParser.mParserPresenter.initFinancialProductRules();

            //벌크 시작 알린후
        }

        void cancelTask() {
            if (this.mSendToServerThread != null) {
                this.mSendToServerThread.cancelThread();
            }
            this.mIsCanceled = true;
        }


        @Override
        protected Void doInBackground(Void... params) {
            LOGI(TAG, "doInBackground");
            this.mSendToServerThread = new SendToServerThread(this.mAdapter, this.mParser.mParserPresenter);
            this.mSendToServerThread.start();

            int totalCnt = this.mAdapter.getSmsCount();
            ArrayList<Transaction> transactionBuffer = null;
            ArrayList<FinancialProduct> financialProductBuffer = null;

            for (int i = 0; i < totalCnt && !this.mIsCanceled; i++) {
                SMS sms = this.mAdapter.getSmsAt(i);
                if(sms!=null && sms.isValid()){
                    ArrayList<Transaction> transactions = this.mParser.mParserPresenter.parseWithBulk(sms);
                    if (transactions != null && !transactions.isEmpty()) {
                        if (transactionBuffer == null) {
                            transactionBuffer = new ArrayList<>();
                        }
                        transactionBuffer.addAll(transactions);
                    } else { // transaction 파싱 안된경우

                        ParserResult finResult = mParser.mParserPresenter.parseFinancialProduct(sms, mParser.mParserPresenter.getRepSender(sms));
                        if(finResult.resultCode == ResultCode.SEND_TO_SERVER) {
                            if (financialProductBuffer == null) {
                                financialProductBuffer = new ArrayList<>();
                            }
                            financialProductBuffer.add(finResult.financialProduct);
                        }
                    }
                }


                if (transactionBuffer != null && transactionBuffer.size() >= this.mParser.mTranCnt) {
                    mSendToServerThread.pushTransactions(transactionBuffer);
                    transactionBuffer = null;
                }

                if(financialProductBuffer != null && financialProductBuffer.size() >= this.mParser.mTranCnt) {
                    financialProductBuffer = null;
                    mSendToServerThread.pushFinancialProducts(financialProductBuffer);
                }


                mAdapter.onProgress(i, totalCnt);
            }

            if (!this.mIsCanceled) {
                if (transactionBuffer != null && !transactionBuffer.isEmpty()) {
                    mSendToServerThread.pushTransactions(transactionBuffer);
                }

                if (financialProductBuffer != null && !financialProductBuffer.isEmpty()) {
                    mSendToServerThread.pushFinancialProducts(financialProductBuffer);
                }
            }

            mSendToServerThread.setLastTransactionPushed();

            try {
                this.mSendToServerThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {

            try {
                LOGI(TAG, "onPostExecute");
                if (mSendToServerThread != null) {

                    if (mAdapter!=null) {
                        if (!mSendToServerThread.mIsCanceled) {

                            if (mSendToServerThread.mIsError ) {
                                mAdapter.onError(ResultCode.SERVER_ERROR);   // network error
                            } else {
                                mAdapter.onCompleted(); // success
                            }

                        } else {
                            mAdapter.onError(ResultCode.FORCE_STOP); // cancel... ResultCode 에 취소 에러 상태추가해주세요.
                        }
                    }

                    this.mSendToServerThread.releaseAll();
                    this.mSendToServerThread = null;
                }

                if(this.mParser!=null) {

                    this.mParser.mIsBulk = false;

                    if(mParser.mParserPresenter !=null)
                        this.mParser.mParserPresenter.onBulkComplete();
                }

            }catch (Exception e) {
               e.printStackTrace();
            }

            this.mAdapter = null;
            this.mParser = null;
            super.onPostExecute(aVoid);
        }
    }


    private static class SendToServerThread extends Thread {

        private boolean mIsError;
        private boolean mIsCompleted;
        private boolean mIsCanceled;

        private boolean mIsLastTransactionPushed;
        private boolean mIsLastFinancialProductPushed;

        private BulkSmsAdapter mAdapter;
        private LinkedList<ArrayList<Transaction>> mTransactionPool;
        private ArrayList<ArrayList<Transaction>> mSendingTransactionPool;

        private LinkedList<ArrayList<FinancialProduct>> mFinancialProductPool;
        private ArrayList<ArrayList<FinancialProduct>> mSendingFinancialProductPool;

        private Object mWaitObject;
        private ParserPresenter mParserPresenter;

        SendToServerThread(BulkSmsAdapter adapter, ParserPresenter parserPresenter) {
            super();
            this.mTransactionPool = new LinkedList<>();
            this.mSendingTransactionPool = new ArrayList<>();

            this.mFinancialProductPool = new LinkedList<>();
            this.mSendingFinancialProductPool = new ArrayList<>();

            this.mIsLastTransactionPushed = false;

            this.mAdapter = adapter;
            this.mWaitObject = new Object();
            this.mIsError = false;
            this.mIsCompleted = false;
            this.mIsCanceled = false;
            this.mParserPresenter = parserPresenter;
        }

        void cancelThread() {

            this.mIsCanceled = true;
            this.mTransactionPool.clear();
            this.mSendingTransactionPool.clear();
            this.mFinancialProductPool.clear();
            this.mSendingFinancialProductPool.clear();

            this.wakeupThread();

            LOGI(TAG, "Thread Canceled.. ");

        }

        void setLastTransactionPushed() {
            this.mIsLastTransactionPushed = true;
            this.wakeupThread();
        }

        void pushTransactions(ArrayList<Transaction> transactions) {
            if (transactions != null) {
                if (!this.mIsError && !this.mIsCanceled) {
                    this.mTransactionPool.push(transactions);
                }
            }
            this.wakeupThread();
        }

        void pushFinancialProducts(ArrayList<FinancialProduct> products) {
            if (products != null) {
                if (!this.mIsError && !this.mIsCanceled) {
                    this.mFinancialProductPool.push(products);
                }
            }
            this.wakeupThread();
        }

        @Override
        public void run() {

            LOGI(TAG, "Start Sender Thread ");
            while (!isFinishedLoop()) {

                final boolean hasTransaction = mTransactionPool !=null && !mTransactionPool.isEmpty();
                final boolean hasFinancialProduct = mFinancialProductPool !=null && !mFinancialProductPool.isEmpty();


                if (hasTransaction || hasFinancialProduct) {
                    ArrayList<Transaction> transactions = new ArrayList<>();
                    ArrayList<FinancialProduct> financialProducts = new ArrayList<>();

                    if(hasTransaction) {
                        transactions = Utils.distinctTransactions(mTransactionPool.pop());
                        this.mSendingTransactionPool.add(transactions);
                    }

                    if(hasFinancialProduct) {
                        financialProducts = mFinancialProductPool.pop();
                        this.mSendingFinancialProductPool.add(financialProducts);
                    }

                    LOGI(TAG, "Start Network ... ");
                    final ArrayList<Transaction> finalTransactions = transactions;
                    final ArrayList<FinancialProduct> finalFinancialProducts = financialProducts;

                    mAdapter.sendToServerTransactions(transactions, financialProducts, new OnNetworkResultListener() {
                        @Override
                        public void onResult(final boolean bSuccess) {

                            try {
                                if(mSendingTransactionPool != null && hasTransaction) {

                                    mSendingTransactionPool.remove(finalTransactions);

                                    if (bSuccess) {
                                        mParserPresenter.insertTransactionAndMaxSmsId(finalTransactions);
                                    }

                                    LOGI(TAG, "End Network ... ");
                                }

                                if(mSendingFinancialProductPool != null && hasFinancialProduct) {
                                    mSendingFinancialProductPool.remove(finalFinancialProducts);
                                }

                                if (!bSuccess && !mIsError) {
                                    mIsError = true;
                                }

                                if (mIsLastTransactionPushed && mTransactionPool.isEmpty() && (mSendingTransactionPool == null || mSendingTransactionPool.isEmpty()) &&
                                mFinancialProductPool.isEmpty() && (mSendingFinancialProductPool == null || mSendingFinancialProductPool.isEmpty())) {

                                    mIsCompleted = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            wakeupThread();


                        }
                    });
                } else if (mIsLastTransactionPushed && mSendingTransactionPool.isEmpty() && mSendingFinancialProductPool.isEmpty()) {

                    mIsCompleted = true;
                }

                if (this.shouldWaitThread()) {
                    this.waitThread();
                }

            }

            LOGI(TAG, "End Sender Thread");
        }


        void releaseAll() {
            this.mAdapter = null;
            if (this.mTransactionPool != null) {
                this.mTransactionPool.clear();
                this.mTransactionPool = null;
            }

            if (this.mSendingTransactionPool != null) {
                this.mSendingTransactionPool.clear();
                this.mSendingTransactionPool = null;
            }

            this.mWaitObject = null;
        }

        private synchronized boolean isFinishedLoop() {
            return (this.mIsCompleted || this.mIsCanceled || this.mIsError);
        }

        private synchronized boolean shouldWaitThread() {
            if (this.isFinishedLoop() || !this.mTransactionPool.isEmpty()) {
                return false;
            } else {
                return true;
            }
        }

        private void wakeupThread() {
            try {
                if (this.mWaitObject != null) {
                    synchronized (this.mWaitObject) {
                        this.mWaitObject.notifyAll();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private synchronized void waitThread() {
            try {
                if (this.mWaitObject != null) {
                    synchronized (this.mWaitObject) {
                        this.mWaitObject.wait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

