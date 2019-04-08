import koma.*
import kotlin.random.Random

fun main() {
    val prices = start(17, 7, 77).drop(2).toDoubleArray()
    figure(1)
    plot(prices)
    xlabel("Time")
    ylabel("Price")
    title("????")

}

private fun start(d: Int, h: Int, r: Int, steps: Int = 100): List<Double> {
    val agents = ArrayList<Agent>()
    val blockedAgents = hashSetOf<Agent>()
    for (i in 0 until d) {
        agents.add(DefaultAgent((80..120).random().toDouble()))
    }
    for (i in 0 until h) {
        agents.add(HerdAgent((80..120).random().toDouble()))
    }
    for (i in 0 until r) {
        agents.add(SemiRationalAgent((80..120).random().toDouble()))
    }
    agents.shuffle()
    Market.averagePrice = 10.0
    for (i in 0 until steps) {
        for (agent in agents) {
            if (Random.nextInt(0, 3) == 0) {
                Market.bids.removeIf { o -> o.agent == agent }
                Market.asks.removeIf { o -> o.agent == agent }
                blockedAgents.remove(agent)
            }
            if (!blockedAgents.contains(agent)) {
                val order = agent.makeDeal()
                if (order != null) {
                    Market.execute(order.first, order.second, blockedAgents, agent)
                }
            }
        }
        Market.historyPrices.add(Market.averagePrice)
        Market.averagePrice = (Market.asks.map { it.price }.average() + Market.bids.map { it.price }.average()) / 2
    }
    println(Market.historyPrices)
    return Market.historyPrices
}