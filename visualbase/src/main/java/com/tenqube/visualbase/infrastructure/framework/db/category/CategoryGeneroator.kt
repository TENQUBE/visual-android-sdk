package com.tenqube.visualbase.infrastructure.framework.db.category

import android.content.Context
import android.content.res.AssetManager
import com.tenqube.visualbase.domain.category.Category
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

object CategoryGeneroator {
    fun generate(context: Context) : List<Category> {
        val categories = mutableListOf<Category>()
        val am: AssetManager = context.assets
        val inStream: InputStream
        var i = 0
        try {
            inStream = am.open("categories.tsv")
            val buffer = BufferedReader(InputStreamReader(inStream))
            var line: String
            while (buffer.readLine().also { line = it } != null) {
                if (i == 0) {
                    i++
                    continue
                }
                val colums = line.split("\t").toTypedArray()
                val category = Category(
                    colums[0],
                    colums[1],
                    colums[2],
                    colums[3],
                    colums[4]
                )
                categories.add(category)
                i++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categories
    }
}
