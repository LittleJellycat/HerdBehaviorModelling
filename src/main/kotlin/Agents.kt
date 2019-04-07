interface Agent {
    fun addMoney(money: Double)
    fun addAmount(n: Int)
    fun makeDeal(): Pair<BidAsk, Double>?
    fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.BID) {
            if (Market.bids.isEmpty()) {
                Market.averagePrice
            } else {
                val maxBid = Market.bids.peek().price
                maxBid * 1.01
            }
        } else {
            if (Market.asks.isEmpty()) {
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

class DefaultAgent(var money: Double, var amount: Int = 10) : Agent {
    override fun addAmount(n: Int) {
        amount += n
    }

    override fun addMoney(money: Double) {
        this.money += money
    }

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return if (type == BidAsk.BID) {
            if (amount >= 1) {
                amount--
                type to price
            } else {
                null
            }
        } else {
            if (money >= price) {
                money -= price
                type to price
            } else {
                null
            }
        }
    }

    private fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size > Market.bids.size) return BidAsk.BID
        if (Market.asks.size < Market.bids.size) return BidAsk.ASK
        return BidAsk.values().random()
    }

}

class HerdAgent(var money: Double, var amount: Int = 10) : Agent {
    override fun addMoney(money: Double) {
        this.money += money
    }

    override fun addAmount(n: Int) {
        amount += n
    }

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return if (type == BidAsk.BID) {
            if (amount >= 1) {
                amount--
                type to price
            } else {
                null
            }
        } else {
            if (money >= price) {
                money -= price
                type to price
            } else {
                null
            }
        }
    }

    private fun chooseBidOrAsk(): BidAsk {
        if (Market.asks.size < Market.bids.size) return BidAsk.BID
        if (Market.asks.size > Market.bids.size) return BidAsk.ASK
        return BidAsk.values().random()
    }

}

class SemiRationalAgent(var money: Double, var amount: Int = 10) : Agent {
    override fun addMoney(money: Double) {
        this.money += money
    }

    override fun addAmount(n: Int) {
        amount += n
    }

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val (price1, price2) = Market.historyPrices.takeLast(2)
        val type = when {
            price1 < price2 -> BidAsk.ASK
            price1 > price2 -> BidAsk.BID
            else -> listOf(BidAsk.BID, BidAsk.ASK).random()
        }
        val price = setPrice(type)
        return if (type == BidAsk.BID) {
            if (amount >= 1) {
                amount--
                type to price
            } else {
                null
            }
        } else {
            if (money >= price) {
                money -= price
                type to price
            } else {
                null
            }
        }
    }
}