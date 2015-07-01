package server.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;

import server.game.player.update.ForkJoinPlayerUpdater;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * The main game sequencer that executes game logic every {@code CYCLE_RATE}ms.
 * This also gives access to a single threaded {@link ScheduledExecutorService}
 * which allows for the execution of low priority asynchronous services.
 * 
 * @author lare96 <http://github.com/lare96>
 */
public final class GameService implements Runnable {

    /**
     * The logger that will print important information.
     */
    //private final Logger logger = Logger.getLogger(GameService.class.getName());

    /**
     * The executor that will execute various low priority services. This
     * executor implementation will allocate a maximum of {@code 1} thread that
     * will timeout after {@code 45}s of inactivity.
     */
    private final ScheduledExecutorService logicService = GameService.createLogicService();

    @Override
    public void run() {
        try {
          new ForkJoinPlayerUpdater().execute();
        } catch (Throwable t) {
           // XXX: save all players
        }
    }

    /**
     * Submits {@code t} to the underlying logic service to be executed
     * asynchronously. Please note that the task may not be executed for some
     * time after this method returns, depending on how many tasks are currently
     * in the queue of the logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     */
    public void submit(Runnable t) {
        logicService.execute(t);
    }

    /**
     * Submits {@code t} to the underlying logic service to be repeatedly
     * executed asynchronously with an initial delay of {@code initialDelay} and
     * subsequent delays of {@code delay} in the time unit {@code unit}. Please
     * note that the task may not be executed exactly on the requested
     * intervals, depending on how many tasks are currently in the queue of the
     * logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     * @return the result of the execution of this task, primarily used to
     *         cancel execution.
     */
    public ScheduledFuture<?> submit(Runnable t, long initialDelay, long delay, TimeUnit unit) {
        return logicService.scheduleAtFixedRate(t, initialDelay, delay, unit);
    }

    /**
     * Submits {@code t} to the underlying logic service to be repeatedly
     * executed asynchronously with initial and subsequent delays of
     * {@code delay} in the time unit {@code unit}. Please note that the task
     * may not be executed for some time after this method returns, depending on
     * how many tasks are currently in the queue of the logic service.
     * 
     * @param t
     *            the task to submit to the executor.
     * @return the result of the execution of this task, primarily used to
     *         cancel execution.
     */
    public ScheduledFuture<?> submit(Runnable t, long delay, TimeUnit unit) {
        return submit(t, delay, delay, unit);
    }


    /**
     * Creates and configures a new {@link ScheduledExecutorService} aimed at
     * executing low priority services. The returned executor is
     * <b>unconfigurable</b> meaning it's configuration can no longer be
     * modified.
     *
     * @return the newly created and configured executor service.
     */
    private static ScheduledExecutorService createLogicService() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        executor.setRejectedExecutionHandler(new CallerRunsPolicy());
        executor.setThreadFactory(new ThreadFactoryBuilder().setNameFormat("LogicServiceThread").build());
        executor.setKeepAliveTime(45, TimeUnit.SECONDS);
        executor.allowCoreThreadTimeOut(true);
        return Executors.unconfigurableScheduledExecutorService(executor);
    }
}