interface Agent {
    var money: Double
    var amount: Int
    fun addMoney(money: Double) {
        this.money += money
    }

    fun addAmount(n: Int) {
        amount += n
    }

    fun isValidDeal(type: BidAsk, price: Double) = if (type == BidAsk.BID) amount >= 1 else money >= price

    fun makeDeal(): Pair<BidAsk, Double>?
    fun setPrice(bidAsk: BidAsk): Double
    fun chooseBidOrAsk(): BidAsk
}

enum class BidAsk {
    BID,
    ASK
}

class StabilizingAgent(override var money: Double, override var amount: Int = 10) : Agent {

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return (type to price).takeIf { isValidDeal(type, price) }
    }

    override fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size > Market.bids.size) return BidAsk.BID
        if (Market.asks.size < Market.bids.size) return BidAsk.ASK
        return BidAsk.values().random()
    }

    override fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.ASK) {
            Market.averagePrice * 1.01
        } else {
            Market.averagePrice * 0.99
        }
    }

}

class HerdAgent(override var money: Double, override var amount: Int = 10) : Agent {
    override fun makeDeal(): Pair<BidAsk, Double>? {
        if (Market.asks.isEmpty() && Market.bids.isEmpty()) return null
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return (type to price).takeIf { isValidDeal(type, price) }
    }

    override fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size < Market.bids.size) return BidAsk.BID
        if (Market.asks.size > Market.bids.size) return BidAsk.ASK
        return BidAsk.values().random()
    }

    override fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.ASK) {
            require(Market.asks.isNotEmpty())
            Market.asks.peek().price * 1.01
        } else {
            require(Market.bids.isNotEmpty())
            Market.bids.peek().price * 0.99
        }
    }
}

class SemiRationalAgent(override var money: Double, override var amount: Int = 10) : Agent {

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return (type to price).takeIf { isValidDeal(type, price) }
    }

    override fun chooseBidOrAsk(): BidAsk {
        val previousPrice = Market.historyPrices.last()
        return when {
            previousPrice < Market.averagePrice -> BidAsk.ASK
            previousPrice > Market.averagePrice -> BidAsk.BID
            else -> BidAsk.values().random()
        }
    }

    override fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.ASK) {
            if (Market.asks.isEmpty()) {
                Market.averagePrice
            } else {
                Market.asks.peek().price * 1.01
            }
        } else {
            if (Market.bids.isEmpty()) {
                Market.averagePrice
            } else {
                Market.bids.peek().price * 0.99
            }
        }
    }
}