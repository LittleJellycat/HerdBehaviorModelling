interface Agent {
    fun makeDeal()

    fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.BID) {
            if (Market.bids.isEmpty()) {
                Market.averagePrice
            } else {
                val maxBid = Market.bids.peek().price
                maxBid * 1.01
            }
        } else {
            if (Market.bids.isEmpty()) {
                Market.averagePrice
            } else {
                val minAsk = Market.asks.peek().price
                minAsk * 0.99
            }
        }
    }
}

enum class BidAsk {
    BID,
    ASK
}

class DefaultAgent(var money: Double, var amount: Int):Agent {
    override fun makeDeal() {

    }

    private fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size > Market.bids.size) return BidAsk.BID
        if (Market.asks.size < Market.bids.size) return BidAsk.ASK
        return listOf(BidAsk.BID, BidAsk.ASK).random()

    }

}

class HerdAgent(var money: Double, var amount: Int) : Agent {
    override fun makeDeal() {
        val type = chooseBidOrAsk()
        if (type==BidAsk.BID && amount > 0){

        } else if (type==BidAsk.ASK && money > 0){

        }
    }

    private fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size < Market.bids.size) return BidAsk.BID
        if (Market.asks.size > Market.bids.size) return BidAsk.ASK
        return listOf(BidAsk.BID, BidAsk.ASK).random()
    }

}

class SemiRationalAgent(var money: Double, var amount: Int) : Agent {
    override fun makeDeal() {
        val (price1, price2) = Market.historyPrices.takeLast(2)
        val orderType = when {
            price1 < price2 -> BidAsk.ASK
            price1 > price2 -> BidAsk.BID
            else -> listOf(BidAsk.BID, BidAsk.ASK).random()
        }

    }

}