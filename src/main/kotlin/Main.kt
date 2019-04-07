import koma.*


fun main() {
    val prices = start(16, 7, 77).toDoubleArray()
    figure(1)
    plot(prices)
    xlabel("Time")
    ylabel("Price")
    title("????")

}

private fun start(d: Int, h: Int, r: Int, steps: Int = 1000): List<Double> {
    val agents = ArrayList<Agent>()
    val blockedAgents = hashSetOf<Agent>()
    for (i in 0 until d) {
        agents.add(DefaultAgent((1000..2000).random().toDouble(), (10..20).random()))
    }
    for (i in 0 until h) {
        agents.add(HerdAgent((1000..2000).random().toDouble(), (10..20).random()))
    }
    for (i in 0 until r) {
        agents.add(SemiRationalAgent((1000..2000).random().toDouble(), (10..20).random()))
    }
    agents.shuffle()
    Market.averagePrice = 100.0
    for (i in 0 until steps) {
        for (agent in agents) {
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
    return Market.historyPrices
}