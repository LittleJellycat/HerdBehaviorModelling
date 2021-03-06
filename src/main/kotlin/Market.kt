import java.util.*
import kotlin.collections.HashSet

object Market {
    val bids = PriorityQueue<Operation>()
    val asks = PriorityQueue<Operation>(Comparator.reverseOrder<Operation>())
    var averagePrice = 10.0 // default value
    val historyPrices = arrayListOf(10.0, 10.0)

    fun execute(type: BidAsk, price: Double, blocked: HashSet<Agent>, agent: Agent) {
        if (type == BidAsk.BID) {
            if (!asks.isEmpty() && asks.peek().price >= price) {
                val counterparty = asks.poll()
                blocked.remove(counterparty.agent)
                blocked.remove(agent)
                agent.amount--
                agent.money += price
                counterparty.agent.money -= price
                counterparty.agent.amount++
            } else {
                bids.offer(Operation(price, agent))
                blocked.add(agent)
            }
        } else {
            if (!bids.isEmpty() && bids.peek().price <= price) {
                val counterparty = bids.poll()
                blocked.remove(counterparty.agent)
                blocked.remove(agent)
                agent.amount++
                agent.money -= price
                counterparty.agent.money += price
                counterparty.agent.amount--
            } else {
                asks.offer(Operation(price, agent))
                blocked.add(agent)
            }
        }
    }
}

data class Operation(val price: Double, val agent: Agent) : Comparable<Operation> {
    override fun compareTo(other: Operation): Int = compareValues(this.price, other.price)
}