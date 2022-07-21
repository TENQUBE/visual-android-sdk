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
            var line: String?
            while (buffer.readLine().also { line = it } != null) {
                if (i == 0) {
                    i++
                    continue
                }
                line?.split("\t")?.toTypedArray()?.let {
                    val category = Category(
                        it[0],
                        it[1],
                        it[2],
                        it[3],
                        it[4]
                    )
                    categories.add(category)

                }
                i++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categories
    }
}
