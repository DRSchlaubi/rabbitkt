/*
 *    Copyright 2021 NyCode
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package de.nycode.rabbitkt.receiver

import com.rabbitmq.client.Delivery
import kotlinx.coroutines.flow.Flow
import reactor.rabbitmq.AcknowledgableDelivery
import reactor.rabbitmq.Receiver
import java.io.Closeable

public interface CoroutineReceiver : Closeable {
    /**
     * Closes the underlying [Receiver].
     */
    override fun close()

    /**
     * Consume messages of the given [queue] with a [Flow].
     * Every message gets acknowledged automatically.
     * @param queue the targeted queue.
     * @return A [Flow] of deliveries.
     */
    public fun consumeAutoAckFlow(queue: String): Flow<Delivery>

    /**
     * Consume messages of the given [queue] with your given [handler].
     * Every message gets acknowledged automatically.
     * @param queue the targeted queue.
     * @param handler the handler which gets called for every message.
     */
    public suspend fun consumeAutoAck(queue: String, handler: suspend (Delivery) -> Unit)

    /**
     * Consume messages of the given [queue] with a [Flow].
     * You have to acknowledge every message manually with either
     * [AcknowledgeHandler.ack] or [AcknowledgeHandler.reject].
     * @param queue the targeted queue.
     * @return A [Flow] of deliveries which must be acknowledged or rejected.
     */
    public fun consume(queue: String): Flow<AcknowledgableDelivery>

    /**
     * Consume message of the given [queue] with your given [handler].
     * You have to acknowledge every message manually with either
     * [AcknowledgeHandler.ack] or [AcknowledgeHandler.reject].
     * @param queue the targeted queue.
     * @param handler the handler which gets called for every message.
     */
    public suspend fun consume(queue: String, handler: suspend AcknowledgeHandler.() -> Unit)
}
