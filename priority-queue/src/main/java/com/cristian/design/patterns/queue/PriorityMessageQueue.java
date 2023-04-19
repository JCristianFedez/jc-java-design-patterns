package com.cristian.design.patterns.queue;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PriorityMessageQueue<T extends Comparable> {

  private int size = 0;

  private int capacity;

  private T[] queue;

  public PriorityMessageQueue(T[] queue) {
    this.queue = queue;
    this.capacity = queue.length;
  }

  /**
   * Remove top message from queue.
   */
  public T remove() {
    if (isEmpty()) {
      return null;
    }

    final var root = queue[0];
    queue[0] = queue[size - 1];
    size--;
    maxHeapifyDown();
    return root;
  }

  /**
   * Add message to queue.
   */
  public void add(T t) {
    ensureCapacity();
    queue[size] = t;
    size++;
    maxHeapifyUp();
  }

  /**
   * Check queue size.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  private void maxHeapifyDown() {
    var index = 0;
    while (hasLeftChild(index)) {

      var smallerIndex = leftChildIndex(index);

      if (hasRightChild(index) && right(index).compareTo(left(index)) > 0) {
        smallerIndex = rightChildIndex(index);
      }

      if (queue[index].compareTo(queue[smallerIndex]) > 0) {
        break;
      } else {
        swap(index, smallerIndex);
      }

      index = smallerIndex;


    }

  }

  private void maxHeapifyUp() {
    var index = size - 1;
    while (hasParent(index) && parent(index).compareTo(queue[index]) < 0) {
      swap(parentIndex(index), index);
      index = parentIndex(index);
    }
  }

  // index
  private int parentIndex(int pos) {
    return (pos - 1) / 2;
  }

  private int leftChildIndex(int parentPos) {
    return 2 * parentPos + 1;
  }

  private int rightChildIndex(int parentPos) {
    return 2 * parentPos + 2;
  }

  // value
  private T parent(int childIndex) {
    return queue[parentIndex(childIndex)];
  }

  private T left(int parentIndex) {
    return queue[leftChildIndex(parentIndex)];
  }

  private T right(int parentIndex) {
    return queue[rightChildIndex(parentIndex)];
  }

  // check
  private boolean hasLeftChild(int index) {
    return leftChildIndex(index) < size;
  }

  private boolean hasRightChild(int index) {
    return rightChildIndex(index) < size;
  }

  private boolean hasParent(int index) {
    return parentIndex(index) >= 0;
  }

  private void swap(int fpos, int tpos) {
    var tmp = queue[fpos];
    queue[fpos] = queue[tpos];
    queue[tpos] = tmp;
  }

  private void ensureCapacity() {
    if (size == capacity) {
      capacity = capacity * 2;
      queue = Arrays.copyOf(queue, capacity);
    }
  }

  /**
   * For debug .. print current state of queue
   */
  public void print() {
    for (var i = 0; i <= size / 2; i++) {
      LOGGER.info(" PARENT : " + queue[i] + " LEFT CHILD : "
          + left(i) + " RIGHT CHILD :" + right(i));
    }
  }

}