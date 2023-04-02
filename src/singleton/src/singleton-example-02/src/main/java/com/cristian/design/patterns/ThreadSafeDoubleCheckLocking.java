package com.cristian.design.patterns;

/**
 * <p>Double check locking.</p>
 *
 * <p><a href="http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html">DoubleCheckedLocking</a></p>
 *
 * <p>Broken under Java 1.4.</p>
 *
 * @author mortezaadi@gmail.com
 */
public class ThreadSafeDoubleCheckLocking {

  private static volatile ThreadSafeDoubleCheckLocking instance;

  private ThreadSafeDoubleCheckLocking() {
    // to prevent instantiating by Reflection call
    if (ThreadSafeDoubleCheckLocking.instance != null) {
      throw new IllegalStateException("Already initialized.");
    }
  }

  public static ThreadSafeDoubleCheckLocking getInstance() {
    // local variable increases performance by 25 percent
    // Joshua Bloch "Effective Java, Second Edition", p. 283-284

    ThreadSafeDoubleCheckLocking result = instance;
    // Check if singleton instance is initialized.
    // If it is initialized then we can return the instance.
    if (result == null) {
      // It is not initialized but we cannot be sure because some other thread might have
      // initialized it in the meanwhile.
      // So to make sure we need to lock on an object to get mutual exclusion.
      synchronized (ThreadSafeDoubleCheckLocking.class) {
        // Again assign the instance to local variable to check if it was initialized by some
        // other thread while current thread was blocked to enter the locked zone.
        // If it was initialized then we can return the previously created instance
        // just like the previous null check.
        result = instance;
        if (result == null) {
          // The instance is still not initialized so we can safely
          // (no other thread can enter this zone)
          // create an instance and make it our singleton instance.
          instance = result = new ThreadSafeDoubleCheckLocking();
        }
      }
    }
    return result;
  }
}
