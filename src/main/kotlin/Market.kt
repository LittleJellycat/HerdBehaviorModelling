import java.util.*

object Market {
    var averagePrice = 100.0 // default value
    val bids = PriorityQueue<Operation>(Comparator.reverseOrder<Operation>())
    val asks = PriorityQueue<Operation>()
    val historyPrices = arrayListOf(100.0, 100.0)
}

data class Operation(val price: Double, val agent: Int) : Comparable<Operation> {
    override fun compareTo(other: Operation): Int {
        if (this.price > other.price) return 1
        if (this.price < other.price) return -1
        return 0
    }
}