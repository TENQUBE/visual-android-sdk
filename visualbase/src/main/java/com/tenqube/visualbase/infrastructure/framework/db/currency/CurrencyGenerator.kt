package com.tenqube.visualbase.infrastructure.framework.db.currency

import android.content.Context
import android.content.res.AssetManager
import com.tenqube.visualbase.infrastructure.adapter.currency.local.CurrencyModel
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object CurrencyGenerator {
    fun generate(context: Context) : List<CurrencyModel> {
        val items = mutableListOf<CurrencyModel>()
        val am: AssetManager = context.assets
        val inStream: InputStream
        var i = 0
        try {
            inStream = am.open("currency.tsv")
            val buffer = BufferedReader(InputStreamReader(inStream))
            var line: String
            while (buffer.readLine().also { line = it } != null) {
                if (i == 0) {
                    i++
                    continue
                }
                val colums = line.split("\t").toTypedArray()
                val currency = CurrencyModel(
                    colums[0],
                    colums[1],
                    colums[2].toFloat(),
                    System.currentTimeMillis()
                )
                items.add(currency)
                i++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return items
    }
}