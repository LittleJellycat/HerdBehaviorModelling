import koma.*
import org.nield.kotlinstatistics.median
import org.nield.kotlinstatistics.variance
import kotlin.random.Random
 
fun main() {
    val prices = start(77, 16, 7).drop(2).toDoubleArray()
    figure(1)
    plot(prices)
    xlabel("Time")
    ylabel("Price")
    title("Average asset price")
    println(prices.median())
    println(prices.variance())
}

private fun start(
    stabilizingAgentCount: Int,
    herdAgentCount: Int,
    semiRationalAgentCount: Int,
    steps: Int = 100
): List<Double> {
    val agents: List<Agent> = mutableListOf<Agent>().apply {
        stabilizingAgentCount.repeat {
            add(StabilizingAgent(Random.nextDouble(80.0, 120.0)))
        }
        herdAgentCount.repeat {
            add(HerdAgent(Random.nextDouble(80.0, 120.0)))
        }
        semiRationalAgentCount.repeat {
            add(SemiRationalAgent(Random.nextDouble(80.0, 120.0)))
        }
        shuffle()
    }
    val blockedAgents = hashSetOf<Agent>()
    steps.repeat {
        for (agent in agents) {
            if (agent !in blockedAgents) {
                val order = agent.makeDeal()
                if (order != null) {
                    Market.execute(order.first, order.second, blockedAgents, agent)
                }
            }
        }
        Market.historyPrices.add(Market.averagePrice)
        Market.averagePrice = (Market.asks + Market.bids).map { it.price }.avgOr(Market.averagePrice)
    }
    println(Market.historyPrices)
    return Market.historyPrices
}

fun Iterable<Double>.avgOr(default: Double) = this.average().takeUnless(Double::isNaN) ?: default

inline fun Int.repeat(action: () -> Unit) {
    for (i in 0 until this) action()
}