package com.cristian.design.patterns.framework;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This represents the <i>Handle</i> of Reactor pattern. These are resources managed by OS which can be submitted to {@link NioReactor}.
 *
 * <p>This class serves has the responsibility of reading the data when a read event occurs and
 * writing the data back when the channel is writable. It leaves the reading and writing of data on the concrete implementation. It provides
 * a block writing mechanism wherein when any {@link ChannelHandler} wants to write data back, it queues the data in pending write queue and
 * clears it in block manner. This provides better throughput.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public abstract class AbstractNioChannel {

  @Getter
  private final SelectableChannel channel;

  @Getter
  private final ChannelHandler handler;

  private final Map<SelectableChannel, Queue<Object>> channelToPendingWrites = new ConcurrentHashMap<>();

  private NioReactor reactor;

  /**
   * The operation in which the channel is interested, this operation is provided to {@link Selector}.
   *
   * @return interested operation.
   * @see SelectionKey
   */
  public abstract int getInterestedOps();

  /**
   * Binds the channel on provided port.
   *
   * @throws IOException if any I/O error occurs.
   */
  public abstract void bind() throws IOException;

  /**
   * Reads the data using the key and returns the read data. The underlying channel should be fetched using {@link SelectionKey#channel()}.
   *
   * @param key the key on which read event occurred.
   * @return data read.
   * @throws IOException if any I/O error occurs.
   */
  public abstract Object read(SelectionKey key) throws IOException;

  void flush(final SelectionKey key) throws IOException {
    final Queue<Object> pendingWrites = this.channelToPendingWrites.get(key.channel());
    while (!pendingWrites.isEmpty()) {
      // ask the concrete channel to make sense of data and write it to java channel
      doWrite(pendingWrites.poll(), key);
    }
    // Wo don't have anything more to write so channel is interested in reading more data.
    this.reactor.changeOps(key, SelectionKey.OP_READ);
  }

  /**
   * Writes the data to the channel.
   *
   * @param pendingWrite the data to be written on channel.
   * @param key the key which is writable.
   * @throws IOException if any I/O error occurs.
   */
  protected abstract void doWrite(Object pendingWrite, SelectionKey key) throws IOException;

  /**
   * Queues the data for writing. The data is not guaranteed to be written on underlying channel when this method returns. It will be
   * written when the channel is flushed.
   *
   * <p>This method is used by the {@link ChannelHandler} to send reply back to the client. <br>
   * Example:
   *
   * <pre>
   * <code>
   * {@literal @}Override
   * public void handleChannelRead(AbstractNioChannel channel, Object readObj, SelectionKey key) {
   *   byte[] data = ((ByteBuffer)readObj).array();
   *   ByteBuffer buffer = ByteBuffer.wrap("Server reply".getBytes());
   *   channel.write(buffer, key);
   * }
   * </code>
   * </pre>
   *
   * @param data the data to be written on underlying channel.
   * @param key the key which is writable.
   */
  public void write(final Object data, final SelectionKey key) {
    Queue<Object> pendingWrites = this.channelToPendingWrites.get(key.channel());
    if (pendingWrites != null) {
      pendingWrites.add(data);
      this.reactor.changeOps(key, SelectionKey.OP_WRITE);
      return;
    }
    synchronized (this.channelToPendingWrites) {
      pendingWrites = this.channelToPendingWrites.get(key.channel());
      if (pendingWrites == null) {
        pendingWrites = new ConcurrentLinkedDeque<>();
        this.channelToPendingWrites.put(key.channel(), pendingWrites);
      }
    }
  }
}
