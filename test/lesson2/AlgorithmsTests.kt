package lesson2

import java.io.BufferedWriter
import java.io.File
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class AlgorithmsTests {

    private val minPrice = 42

    private val maxPrice = 99999

    private fun generatePrices(size: Int): Pair<Int, Int> {
        val random = Random()
        val prices = mutableListOf<Int>()
        for (index in 1..size) {
            val price = minPrice + 1 + random.nextInt(maxPrice - 1 - minPrice)
            prices += price
        }
        val firstIndex = random.nextInt(size)
        val secondIndex = random.nextInt(size).let {
            when (it) {
                firstIndex -> if (firstIndex == size - 1) firstIndex - 1 else firstIndex + 1
                else -> it
            }
        }
        val (minIndex, maxIndex) =
                if (firstIndex < secondIndex) firstIndex to secondIndex else secondIndex to firstIndex
        prices[minIndex] = minPrice
        prices[maxIndex] = maxPrice

        fun BufferedWriter.writePrices() {
            for (price in prices) {
                write(price.toString())
                newLine()
            }
            close()
        }

        File("temp_prices.txt").bufferedWriter().writePrices()
        return minIndex + 1 to maxIndex + 1
    }

    @Test
    fun optimizeBuyAndSell() {
        assertEquals(3 to 4, optimizeBuyAndSell("input/buysell_in1.txt"))
        assertEquals(8 to 12, optimizeBuyAndSell("input/buysell_in2.txt"))
        assertEquals(3 to 4, optimizeBuyAndSell("input/buysell_in3.txt"))
        try {
            val expectedAnswer = generatePrices(1000)
            assertEquals(expectedAnswer, optimizeBuyAndSell("temp_prices.txt"))
        } finally {
            File("temp_prices.txt").delete()
        }
        try {
            val expectedAnswer = generatePrices(100000)
            assertEquals(expectedAnswer, optimizeBuyAndSell("temp_prices.txt"))
        } finally {
            File("temp_prices.txt").delete()
        }
    }

    @Test
    fun josephTask() {
        assertEquals(1, josephTask(menNumber = 1, choiceInterval = 1))
        assertEquals(2, josephTask(menNumber = 2, choiceInterval = 1))
        assertEquals(100000000, josephTask(menNumber = 100000000, choiceInterval = 1))
        assertEquals(3, josephTask(menNumber = 8, choiceInterval = 5))
    }

    @Test
    fun longestCommonSubstring() {
        assertEquals("", longestCommonSubstring("мой мир", "я"))
        assertEquals("зд", longestCommonSubstring("здравствуй мир", "мы здесь"))
        assertEquals("СЕРВАТОР", longestCommonSubstring("ОБСЕРВАТОРИЯ", "КОНСЕРВАТОРЫ"))
    }

    @Test
    fun calcPrimesNumber() {
        assertEquals(0, calcPrimesNumber(-1))
        assertEquals(0, calcPrimesNumber(1))
        assertEquals(1, calcPrimesNumber(2))
        assertEquals(2, calcPrimesNumber(4))
        assertEquals(4, calcPrimesNumber(10))
        assertEquals(8, calcPrimesNumber(20))
        assertEquals(1000, calcPrimesNumber(7920))
        assertEquals(79498, calcPrimesNumber(1000000))
    }

    @Test
    fun baldaSearcher() {
        assertEquals(setOf("ТРАВА", "КРАН", "АКВА", "НАРТЫ"),
                baldaSearcher("input/balda_in1.txt", setOf("ТРАВА", "КРАН", "АКВА", "НАРТЫ", "РАК")))
    }
}