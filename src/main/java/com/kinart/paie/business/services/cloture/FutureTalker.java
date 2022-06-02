/*
 * FutureTalker.java
 *
 * V1.3 mods  made listenerList protected and changed addListener test from
 * conatins to  == 
 * mpf 11/04/05
 *
 * V1.2 mods in     public void addListener(IFutureListener<V> listener)
 * moved   synchronized(listenerList) end }  to cover fireListeners to prevent concurrent modification exception
 * mpf 08/04/05
 *
 * V1.1 mods replaced Callable with FutureTalker in listener interface and added
 * getCallable() to this class  mpf 10/2/05
 *
 * Created on December 16, 2004, 7:30 AM
 * by Matthew Ford
 *
 <H1>Licence Agreement</H1>
<PRE>FutureTalker package Copyright (c) 2004 Forward Computing and Control Pty. Ltd.
NSW. Australia,  www.forward.com.au
All rights reserved.
 
Redistribution of the source and/or compiled .class of this package, with or without
modification, is permitted subject to the limitation of libability below.
 
THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.</PRE>
 */

package com.kinart.paie.business.services.cloture;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * This class allows you to add listeners to your
 * {@link FutureTask }.
 * <br>These listeners are called when task completes, throws an error
 * or is cancelled.
 * <p>Your listeners need to implement {@link IFutureListener }. Typicall
 * usage is
 * <PRE>
 *      FutureTalker talker = new FutureTalker<Integer>(<i>callable</i>));
 *      talker.addListener(listener);
 *      try {
 *        threadPool.execute(talker);
 *      } catch (RejectedExecutionException rex) {
 *        talker.rejected(rex);  // failed to execute set task to error and notifies listeners
 *      }
 * </PRE>
 * where <i>callable</i> implements Callable<Integer> and
 * <br>threadPool implements {@link java.util.concurrent.Executor}
 * @author Matthew Ford
 */
public class FutureTalker<V> extends FutureTask<V> {
  
  /**
   * Checks in the interrupt flag is set for the thread running this task
   * and throws an {@link InterruptedException} if it is.
   * <p>In order to stop the task, this {@link InterruptedException}
   * must propergate out of the {@link Callable#call} method.
   * <p>If this check is done in an internal method which is not defined to
   * throw exceptions you will have to catch the InterruptedException and wrap
   * it in a RuntimeException and re-throw it.
   * @throws InterruptedException Thrown if the interrupt flag is set for the thread running this method.
   */
  public static void ifInterruptedStop() throws InterruptedException {
    Thread.yield(); // let another thread have some time perhaps to stop this one.
    if (Thread.currentThread().isInterrupted()) {
      throw new InterruptedException("Stopped by ifInterruptedStop()");
    }
  }
  
  /**
   * Shut down a {@link ThreadPoolExecutor} and reject or cancel unstarted pending tasks.
   * <br>Note: Tasks that have already been started will be interrupted
   * by the shutdown and will fire any {@link IFutureListener#futureError futureError}
   * listeners registered.
   * @return a list of Runnables that were waiting for execution and had not yet started.
   * <br>{@link FutureTalker}'s in this list will have been rejected and {@link Callable}'s will have been cancelled.
   * @param threadPool the {@link ThreadPoolExecutor} to
   * be shutdown.
   */
  public static List<Runnable> shutdownNow(ThreadPoolExecutor threadPool) {
    if (threadPool == null) {
      return new ArrayList<Runnable>(); 
    }
    List<Runnable> 	waitingTasks = threadPool.shutdownNow();
    RejectedExecutionException rex = new RejectedExecutionException("shutdown");
    for( Runnable r : waitingTasks) {
      if(r instanceof FutureTalker) {
        ((FutureTalker)r).rejected(rex);
      } else if (r instanceof Future) {
        ((Future)r).cancel(true);
      }
    }
    return waitingTasks;
  }
  
  /**
   * The Callable being run by this task
   */
  private Callable<V> calling;
  
  /**
   * Returns the <tt>Callable</tt> that this task executes
   *
   * @return  callable the callable task
   */
  public Callable<V> getCallable() {
    return calling;
  }
  
  /**
   * Creates a <tt>FutureTalker</tt> that will upon running, execute the
   * given <tt>Callable</tt>.
   * use<br>
   * FutureTalker(Executors.callable(runnable, result))
   * for runnable
   *
   * @param  callable the callable task
   * @throws NullPointerException if callable is null
   */
  public FutureTalker(Callable<V> callable) {
    super(callable);
    calling = callable;
  }
  
  /**
   * fire the listeners when task terminates or is cancelled
   */
  protected void done() {
    fireListeners();
  }
  
  /**
   * Causes this future to report an <tt>ExecutionException</tt>
   * with the given throwable as its cause, unless this Future has
   * already been set or has been cancelled.
   * @param r the {@link RejectedExecutionException}
   * to be set as the error for this task.
   */
  public void rejected(RejectedExecutionException r) {
    super.setException(r);
  }
  
  /**
   * The list of listeners for this task
   */
  protected List<IFutureListener<V>> listenerList = new ArrayList<IFutureListener<V>>();
  
  /**
   * Adds a listener to this task.
   * <br>If the task has already terminated then this listener
   * is called immediately. Otherwise it is call on the termination
   * of the task.
   * <p>Each listener removed once it is called.
   * <p>Any errors thrown by listeners are caught and ignored.
   * @param listener listener to add. Duplicates quietly are ignored.
   * @throws IllegalArgumentException thrown if listener is null.
   */
  public void addListener(IFutureListener<V> listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    synchronized(listenerList) {
			for (Object obj:listenerList) {
				if (listener == obj) {
					// use exact object equality as equals (contains) may give erroneous matches
					return;
				}
      }
      listenerList.add(listener);
      if (isDone()) {
        fireListeners(); // fire this one
      }
    }   // mod V1.2 move     synchronized(listenerList) { to cover fireListeners to prevent concurrent modification exception
  }
  
  /**
   * Remove a listener from this task, if it is found.
   * <br>Listeners not found in the listener list are ignored.
   * @param listener the listener to remove.
   * @throws IllegalArgumentException thrown if listener is null.
   */
  public void removeListener(IFutureListener<V> listener) {
    if (listener == null) {
      throw new IllegalArgumentException("Listener cannot be null.");
    }
    synchronized(listenerList) {
      listenerList.remove(listener);
    }
  }
  
  
  /**
   * Fires currently registered listeners and removes them.
   * <br>This should only be called from
   * {@link FutureTask#done }
   * <p>Try a {@link Future#get }
   * the result and then handle any exceptions that occure.
   * <br>{@link ExecutionException} are
   * un-wrapped and the underlying exception passed to
   * {@link IFutureListener#futureError futureError}
   */
  protected void fireListeners() {
    synchronized(listenerList) {
      V result = null;
      Iterator<IFutureListener<V>> it = listenerList.iterator();
      try {
        result = get();
        while (it.hasNext()) {
          try {
            it.next().futureResult(result,this);
          } catch (Throwable err) {
            // ignore user should handle this in listener
          }
          it.remove();
        }
      } catch (CancellationException cex) {
        while (it.hasNext()) {
          try {
            it.next().futureCancelled(cex,this);
          } catch (Throwable err) {
            // ignore user should handle this in listener
          }
          it.remove();
        }
      } catch (Throwable t) {
        if (t instanceof ExecutionException) {
          t = t.getCause();
        }
        while (it.hasNext()) {
          try {
            it.next().futureError(t,this);
          } catch (Throwable err) {
            // ignore user should handle this in listener
          }
          it.remove();
        }
      }
    }
  }
  
}
