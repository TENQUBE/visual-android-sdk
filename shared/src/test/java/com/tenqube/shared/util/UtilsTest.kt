package com.tenqube.shared.util

import com.tenqube.shared.util.Utils.convertCalendarToDateStr
import com.tenqube.shared.util.Utils.convertCalendarToDateTimeStr
import com.tenqube.shared.util.Utils.convertCalendarToSyncDateStr
import com.tenqube.shared.util.Utils.convertCalendarToTimeStr
import com.tenqube.shared.util.Utils.convertDateStrToCalendar
import com.tenqube.shared.util.Utils.convertDateStrToDate
import com.tenqube.shared.util.Utils.convertDateTimeStrToCalendar
import com.tenqube.shared.util.Utils.convertDateTimeStrToDate
import com.tenqube.shared.util.Utils.convertDateToDateStr
import com.tenqube.shared.util.Utils.convertDateToDateTimeStr
import com.tenqube.shared.util.Utils.convertTimeStrToCalendar
import com.tenqube.shared.util.Utils.getCurrentWeekMonday
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.util.*

class UtilsTest {

    @Test
    fun convertDateToDateTimeStr_date_returnDateTimeStr() {
        // Given
        val value = "2020-10-10 10:10:10"
        val date = convertDateTimeStrToDate(value)
        // When
        val result = convertDateToDateTimeStr(date)

        // Then
        MatcherAssert.assertThat(value, CoreMatchers.`is`(result))
    }

    @Test
    fun convertDateStrToDate_date_returnDate() {
        // Given
        val value = "2020-10-10"
        // When
        val result = convertDateStrToDate(value)
        val str = convertDateToDateStr(result)

        // Then
        MatcherAssert.assertThat(value, CoreMatchers.`is`(str))
    }

    @Test
    fun convertDateTimeStrToCalendar_str_returnCalendar() {
        // Given
        val value = "2020-10-10 10:10:10"
        // When
        val result = convertDateTimeStrToCalendar(value)
        val str = convertCalendarToDateTimeStr(result)

        // Then
        MatcherAssert.assertThat(value, CoreMatchers.`is`(str))
    }

    @Test
    fun convertDateStrToCalendar_date_returnCalendar() {
        // Given
        val value = "2020-10-10"
        // When
        val result = convertDateStrToCalendar(value)
        val str = convertCalendarToDateStr(result)

        // Then
        MatcherAssert.assertThat(value, CoreMatchers.`is`(str))
    }

    @Test
    fun convertTimeStrToCalendar_str_returnCalendar() {
        // Given
        val value = "10:10:10"
        // When
        val result = convertTimeStrToCalendar(value)
        val str = convertCalendarToTimeStr(result)

        // Then
        MatcherAssert.assertThat(value, CoreMatchers.`is`(str))
    }

    @Test
    fun convertCalendarToSyncDateStr_calendar_returnStr() {
        // Given
        val value = "2020-10-10 10:10:10"
        // When
        val result = convertDateTimeStrToCalendar(value)
        val str = convertCalendarToSyncDateStr(result)

        // Then
        MatcherAssert.assertThat(str, CoreMatchers.`is`("20201010101010"))
    }

    @Test
    fun getCurrentWeekMonday_current_returnMondayDate() {

        // Given

        // When
        val result = getCurrentWeekMonday()
        val monday = result.get(Calendar.DAY_OF_WEEK)

        // Then
        MatcherAssert.assertThat(monday, CoreMatchers.`is`(Calendar.MONDAY))
    }
}
