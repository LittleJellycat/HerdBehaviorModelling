import java.util.*

object Market {
    var averagePrice = 100.0 // default value
    val bids = PriorityQueue<Int>(Comparator.reverseOrder<Int>())
    val asks = PriorityQueue<Int>()
    val historyPrices = ArrayList<Int>()
}