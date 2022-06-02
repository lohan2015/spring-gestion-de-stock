/*
 * IFutureListener.java
 *
 * V1.1 mods replaced Callable with FutureTalker in this listener interface 
 * and added getCallable() to FutureTalker class  mpf 10/2/05
 *
 * Created on 14 December 2004, 11:56
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

import java.util.concurrent.CancellationException;

/**
 * Listener interface for {@link FutureTalker}
 * @author matthewf
 */
public interface IFutureListener<T> {
  
  /**
   * Called on normal completion
   * A call to this method implies the task's call()
   * method has returned without throwing an error.
   * @param result The result return by {@link java.util.concurrent.Callable#call}
   * @param talker the {@link FutureTalker} that was run.
   */
  public void futureResult(T result, FutureTalker<T> talker);
  
  /**
   * Called if cancelled.
   * <br>A call to this method does NOT imply the task's call()
   * method was ever started.
   * <p>Note: This method is <B>NOT</B> called if the task was
   * interrupted by some other means other than a 
   * call to {@link java.util.concurrent.Future#cancel}.
   * <BR>In particular a call to 
   * {@link java.util.concurrent.ThreadPoolExecutor#shutdownNow} does not call
   * {@link java.util.concurrent.Future#cancel} on any of the queued or running tasks.
   * <p>You need to provide a 
   * {@link java.util.concurrent.RejectedExecutionHandler} and
   * override {@link java.util.concurrent.ThreadPoolExecutor#shutdownNow}
   * to call {@link java.util.concurrent.Future#cancel}, if you want this
   * method to be called on rejection and shutdown.
   * @param talker the {@link FutureTalker} that was run.
   * @param cex the cancellation exception.
   */
  public void futureCancelled(CancellationException cex, FutureTalker<T> talker);
  
  /**
   * Called when the task's 
   * {@link java.util.concurrent.Callable#call} exits with an error.
   * <br>The throwable passed to this method is the one that
   * was wrapped in the {@link java.util.concurrent.ExecutionException}
   * return by a call to {@link java.util.concurrent.Future#get}
   * <p>A call to this method implies the task's call()
   * method has exited with an error or that {@link FutureTalker#rejected}
   * was called.
   * @param t the error that cased the task to terminate
   * @param talker the {@link FutureTalker} that was run.
   */
  public void futureError(Throwable t, FutureTalker<T> talker);
}
