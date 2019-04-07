import java.util.*
import kotlin.collections.HashSet

object Market {
    var averagePrice = 10.0 // default value
    val bids = PriorityQueue<Operation>(Comparator.reverseOrder<Operation>())
    val asks = PriorityQueue<Operation>()
    val historyPrices = arrayListOf(10.0, 10.0)

    fun execute(type: BidAsk, price: Double, blocked: HashSet<Agent>, agent: Agent) {
        if (type == BidAsk.BID) {
            if (!asks.isEmpty() && asks.peek().price >= price) {
                val counterparty = asks.poll()
                blocked.remove(counterparty.agent)
                agent.addAmount(-1)
                agent.addMoney(price)
                counterparty.agent.addMoney(-price)
                counterparty.agent.addAmount(1)
            } else {
                bids.offer(Operation(price, agent))
                blocked.add(agent)
            }
        } else {
            if (!bids.isEmpty() && bids.peek().price <= price) {
                val counterparty = bids.poll()
                blocked.remove(bids.poll().agent)
                blocked.remove(counterparty.agent)
                agent.addAmount(1)
                agent.addMoney(-price)
                counterparty.agent.addMoney(price)
                counterparty.agent.addAmount(-1)
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