import CompareResult.*

interface Agent {
    var money: Double
    var amount: Int

    fun isValidDeal(type: BidAsk, price: Double) = if (type == BidAsk.BID) amount >= 1 else money >= price

    fun makeDeal(): Pair<BidAsk, Double>?
    fun setPrice(bidAsk: BidAsk): Double
    fun chooseBidOrAsk(): BidAsk
}

enum class BidAsk { BID, ASK }

class StabilizingAgent(override var money: Double, override var amount: Int = 10) : Agent {

    override fun makeDeal(): Pair<BidAsk, Double>? {
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return isValidDeal(type, price).then { type to price }
    }

    override fun chooseBidOrAsk() = when (Market.asks.size compare Market.bids.size) {
        GT -> BidAsk.BID
        LT -> BidAsk.ASK
        EQ -> BidAsk.values().random()
    }

    override fun setPrice(bidAsk: BidAsk) = Market.averagePrice * (if (bidAsk == BidAsk.ASK) 1.01 else 0.99)

}

class HerdAgent(override var money: Double, override var amount: Int = 10) : Agent {
    override fun makeDeal(): Pair<BidAsk, Double>? {
        if (Market.asks.isEmpty() && Market.bids.isEmpty()) return null
        val type = chooseBidOrAsk()
        val price = setPrice(type)
        return isValidDeal(type, price).then { type to price }
    }

    override fun chooseBidOrAsk() = when (Market.asks.size compare Market.bids.size) {
        LT -> BidAsk.BID
        GT -> BidAsk.ASK
        EQ -> BidAsk.values().random()
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
        return isValidDeal(type, price).then { type to price }
    }

    override fun chooseBidOrAsk() = when (Market.historyPrices.last() compare Market.averagePrice) {
        LT -> BidAsk.ASK
        GT -> BidAsk.BID
        EQ -> BidAsk.values().random()
    }

    override fun setPrice(bidAsk: BidAsk): Double {
        return if (bidAsk == BidAsk.ASK) {
            Market.asks.peek()?.let { it.price * 1.01 } ?: Market.averagePrice
        } else {
            Market.bids.peek()?.let { it.price * 0.99 } ?: Market.averagePrice
        }
    }
}

inline fun <T> Boolean.then(block: () -> T): T? = if (this) block() else null

enum class CompareResult { LT, GT, EQ }

infix fun <T : Comparable<T>> T.compare(other: T): CompareResult {
    return when {
        this > other -> GT
        this < other -> LT
        else -> EQ
    }
}