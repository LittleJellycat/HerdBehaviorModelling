import java.util.*
import kotlin.collections.HashSet

object Market {
    var averagePrice = 100.0 // default value
    val bids = PriorityQueue<Operation>(Comparator.reverseOrder<Operation>())
    val asks = PriorityQueue<Operation>()
    val historyPrices = arrayListOf(100.0, 100.0)

    fun execute(type: BidAsk, price: Double, blocked: HashSet<Agent>, agent: Agent) {
        if (type == BidAsk.BID) {
            if (asks.peek().price >= price) {
                blocked.remove(asks.poll().agent)
            } else {
                bids.offer(Operation(price, agent))
                blocked.add(agent)
            }
        } else {
            if (bids.peek().price <= price) {
                blocked.remove(bids.poll().agent)
            } else {
                asks.offer(Operation(price, agent))
                blocked.add(agent)
            }
        }
    }
}

data class Operation(val price: Double, val agent: Agent) : Comparable<Operation> {
    override fun compareTo(other: Operation): Int {
        if (this.price > other.price) return 1
        if (this.price < other.price) return -1
        return 0
    }
}