interface Agent {
    fun makeDeal()
}

enum class BidAsk {
    BID,
    ASK
}

class DefaultAgent(var money: Double, var amount: Int) {

}

class HerdAgent(var money: Double, var amount: Int) : Agent {
    override fun makeDeal() {

    }

    private fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size < Market.bids.size) return BidAsk.BID
        if (Market.asks.size > Market.bids.size) return BidAsk.ASK
        return listOf(BidAsk.BID, BidAsk.ASK).random()
    }

    private fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.BID) {
            val maxBid = Market.bids.peek()
            maxBid * 1.01
        } else {
            val minAsk = Market.asks.peek()
            minAsk * 0.99
        }
    }
}

class SemiRationalAgent(var money: Double, var amount: Int) {}