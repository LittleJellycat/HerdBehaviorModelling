import koma.*
import org.nield.kotlinstatistics.median
import org.nield.kotlinstatistics.variance
import kotlin.random.Random.Default.nextDouble

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
    val agents = (List(stabilizingAgentCount) { StabilizingAgent(nextDouble(80.0, 120.0)) }
            + List(herdAgentCount) { HerdAgent(nextDouble(80.0, 120.0)) }
            + List(semiRationalAgentCount) { SemiRationalAgent(nextDouble(80.0, 120.0)) }
            ).shuffled()
    val blockedAgents = hashSetOf<Agent>()
    repeat(steps) {
        for (agent in agents) {
            if (agent !in blockedAgents) {
                agent.makeDeal()?.let { (type, price) ->
                    Market.execute(type, price, blockedAgents, agent)
                }
            }
        }
        Market.historyPrices.add(Market.averagePrice)
        Market.averagePrice = (Market.asks + Market.bids).map { it.price }.avgOr(Market.averagePrice)
    }
    return Market.historyPrices
}

fun Iterable<Double>.avgOr(default: Double) = this.average().takeUnless(Double::isNaN) ?: default