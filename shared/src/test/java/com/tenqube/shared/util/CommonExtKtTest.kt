package com.tenqube.shared.util

import com.tenqube.shared.util.Utils.getCurrentWeekMonday
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.util.*

class CommonExtKtTest {

    @Test
    fun toInt_true_returnOne() {
        // Given
        val value = true

        // When
        val result = value.toInt()

        // Then
        assertThat(result, CoreMatchers.`is`(1))
    }

    @Test
    fun toInt_false_returnZero() {
        // Given
        val value = false

        // When
        val result = value.toInt()

        // Then
        assertThat(result, CoreMatchers.`is`(0))
    }

    @Test
    fun toCal_todayDate_returnTodayCalendar() {
        // Given
        val value = Date()

        // When
        val result = value.toCal()

        // Then
        assertThat(result.timeInMillis, CoreMatchers.`is`(result.timeInMillis))
    }

    @Test
    fun toLcode_categoryCode_returnLcode() {
        // Given
        val value = 101010

        // When
        val result = value.toLcode()

        // Then
        assertThat(result, CoreMatchers.`is`(10))
    }

    @Test
    fun toFullCode_lCode_returnCategoryCode() {
        // Given
        val value = 10

        // When
        val result = value.toFullCode()

        // Then
        assertThat(result, CoreMatchers.`is`(101010))
    }

    @Test
    fun toFullCode_mCode_returnCategoryCode() {
        // Given
        val value = 1010

        // When
        val result = value.toFullCode()

        // Then
        assertThat(result, CoreMatchers.`is`(101010))
    }

    @Test
    fun toMcode_categoryCode_returnMcode() {
        // Given
        val value = 101010

        // When
        val result = value.toMcode()

        // Then
        assertThat(result, CoreMatchers.`is`(1010))
    }

    @Test
    fun dayOfWeek_monday_returnOne() {
        // Given
        val dayOfWeek = 2 // monday

        // When
        val result = dayOfWeek.dayOfWeek()

        // Then
        assertThat(result, CoreMatchers.`is`(1))
    }

    @Test
    fun dayOWeek_sunday_returnSeven() {
        // Given
        val dayOfWeek = 1 // sunday

        // When
        val result = dayOfWeek.dayOfWeek()

        // Then
        assertThat(result, CoreMatchers.`is`(7))
    }

    @Test
    fun toGroupByMonth_date_returnYearMonth() {
        // Given
        val value = "2020-10-10 10:10:10"

        // When
        val result = value.toGroupByMonth()

        // Then
        assertThat(result, CoreMatchers.`is`("2020-10"))
    }

    @Test
    fun toGroupByDate_date_returnYearMonthDate() {
        // Given
        val value = "2020-10-10 10:10:10"

        // When
        val result = value.toGroupByDate()

        // Then
        assertThat(result, CoreMatchers.`is`("2020-10-10"))
    }

    @Test
    fun toHour_date_returnHour() {
        // Given
        val value = "2020-10-10 10:10:10"

        // When
        val result = value.toHour()

        // Then
        assertThat(result, CoreMatchers.`is`(10))
    }

    @Test
    fun toGroupByWeek_currentDate_returnWeek0() {
        // Given
        val value = Utils.convertCalendarToDateTimeStr(Calendar.getInstance())

        val calendar = getCurrentWeekMonday()
        // When
        val result = value.toGroupByWeek(calendar.timeInMillis)

        // Then
        assertThat(result, CoreMatchers.`is`("0"))
    }

    @Test
    fun toGroupByWeek_lastWeekDate_returnWeek1() {
        // Given
        val value = Utils.convertCalendarToDateTimeStr(
            Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_YEAR, -1)
            }
        )

        val calendar = getCurrentWeekMonday()
        // When
        val result = value.toGroupByWeek(calendar.timeInMillis)

        // Then
        assertThat(result, CoreMatchers.`is`("1"))
    }

    @Test
    fun toGroupByWeek_lastWeekDate_returnWeek2() {
        // Given
        val value = Utils.convertCalendarToDateTimeStr(
            Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_YEAR, -2)
            }
        )

        val calendar = getCurrentWeekMonday()
        // When
        val result = value.toGroupByWeek(calendar.timeInMillis)

        // Then
        assertThat(result, CoreMatchers.`is`("2"))
    }

    @Test
    fun toGroupByWeek_afterDate_returnWeekMinus1() {
        val value = Utils.convertCalendarToDateTimeStr(
            Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_YEAR, 1)
            }
        )

        val calendar = getCurrentWeekMonday()
        // When
        val result = value.toGroupByWeek(calendar.timeInMillis)

        // Then
        assertThat(result, CoreMatchers.`is`("-1"))
    }

    @Test
    fun toGroupByWeek_afterDate_returnWeekMinus2() {
        val value = Utils.convertCalendarToDateTimeStr(
            Calendar.getInstance().apply {
                add(Calendar.WEEK_OF_YEAR, 2)
            }
        )

        val calendar = getCurrentWeekMonday()
        // When
        val result = value.toGroupByWeek(calendar.timeInMillis)

        // Then
        assertThat(result, CoreMatchers.`is`("-2"))
    }
}
