package cn.cokebook.lotus.redis

import cn.cokebook.lotus.core.DelayMsgQueue
import cn.cokebook.lotus.core.DelayMsgQueue.DEFAULT_LOCK_TIME
import cn.cokebook.lotus.core.Msg
import cn.cokebook.lotus.core.QueueProperties
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.script.DefaultRedisScript
import java.math.BigDecimal
import java.math.RoundingMode
import java.nio.charset.StandardCharsets
import java.time.Duration
import kotlin.math.min

open class RedisDelayQueue(
    private val name: String,
    private val redisTemplate: RedisTemplate<String, String>,
    private val queueProperties: QueueProperties
) :
    DelayMsgQueue {

    override fun offer(msg: String, ts: Long) {
        val score = BigDecimal.valueOf(ts + 0.00).toString()
        redisTemplate.execute<Any>(
            DefaultRedisScript<Any>("redis.call('zadd', KEYS[1], ARGV[2], ARGV[1])", null),
            listOf<String>(name),
            msg, score
        )
    }

    override fun offer(msgList: MutableList<out Msg>) {

        redisTemplate.execute { connection ->
            connection.execute(
                "ZADD",
                name.toByteArray(StandardCharsets.UTF_8),
                *msgList.flatMap {
                    listOf(
                        it.ts.toString().toByteArray(StandardCharsets.UTF_8),
                        it.content.toByteArray(StandardCharsets.UTF_8)
                    )
                }.toTypedArray()
            )
        }

    }


    override fun poll(size: Int, lockTime: Duration?): List<Msg> {

        val lockMs = (lockTime ?: DEFAULT_LOCK_TIME).toMillis()
        val targetMaxSize = min(size, queueProperties.maxFetchSize)
        val results = redisTemplate.execute(
            DefaultRedisScript(
                """
                        local ct = redis.call('TIME')
                        local nowMs = tonumber(ct[1]) * 1000 + math.floor(tonumber(ct[2])/1000)
                        local lockMs = ARGV[2]
                        local nextMs = nowMs + tonumber(lockMs)
                        local params = {}
                        local size = ARGV[1]
                        local result =  redis.call('zrangebyscore', KEYS[1], '-inf' , nowMs, 'WITHSCORES', 'LIMIT' , 0 , size) 
                        for i = 1, #result,  2 do 
                            local member = result[i] 
                            local score =  result[i+1] 
                            local ip, fp = math.modf(score) 
                            local nextScore = nextMs + fp + 0.01 
                            table.insert(params , nextScore)
                            table.insert(params , member)
                        end 
                        if #params > 0 then
                            redis.call('zadd', KEYS[1], unpack(params)) 
                        end
                        return  result;
                     """,
                MutableList::class.java
            ),
            listOf(name),
            targetMaxSize.toString(),
            lockMs.toString()
        )

        val msgList: MutableList<Msg> = ArrayList()
        var i = 0
        while (i < results.size) {
            val member = results[i] as String
            val score = results[i + 1] as String
            val bigDecimal = BigDecimal(score).setScale(2, RoundingMode.HALF_UP)
            val retries = (bigDecimal.multiply(BigDecimal.valueOf(100)).toLong() % 100).toInt()
            msgList.add(Msg.of(member, bigDecimal.toLong(), retries))
            i += 2
        }
        return msgList


    }

    override fun ack(msg: String) {
        redisTemplate.execute { connection: RedisConnection ->
            connection.execute(
                "ZREM", name.toByteArray(
                    StandardCharsets.UTF_8
                ), msg.toByteArray(StandardCharsets.UTF_8)
            )
        }
    }

    override fun ack(msg: MutableList<String>) {

        redisTemplate.execute { connection: RedisConnection ->
            connection.execute(
                "ZREM",
                name.toByteArray(StandardCharsets.UTF_8),
                *msg.map { it.toByteArray(StandardCharsets.UTF_8) }.toTypedArray()
            )
        }
    }

}