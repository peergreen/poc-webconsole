package com.peergreen.webconsole.it;

import com.gargoylesoftware.htmlunit.WebClient;
import org.apache.felix.ipojo.extender.queue.QueueService;

import static java.lang.String.format;

/**
 * User: guillaume
 * Date: 29/05/13
 * Time: 10:40
 */
public class StabilityHelper {
    public static final int INCREMENT_IN_MS = 100;
    public static final int THIRTY_SECONDS = 30000;
    public static final int DEFAULT_TIMEOUT = THIRTY_SECONDS;

    private final QueueService queueService;
    private final WebClient webClient;

    public StabilityHelper(final QueueService queueService) {
        this.queueService = queueService;
        webClient = new WebClient();
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
    }

    public void waitForIPOJOStability() throws Exception {
        waitForStability(DEFAULT_TIMEOUT, null);
    }

    public void waitForWebConsoleStability(String url) throws Exception {
        waitForStability(DEFAULT_TIMEOUT, url);
    }

    /**
     * This should be moved into chameleon osgi-helper module
     * @param timeout milliseconds
     * @throws Exception
     */
    public void waitForStability(long timeout, String url) throws Exception {

        long sleepTime = 0;
        long startupTime = System.currentTimeMillis();
        do {
            long elapsedTime = System.currentTimeMillis() - startupTime;
            if (isStable(url)) {
                //System.out.printf("Stability reached after %d ms%n", elapsedTime);
                return;
            }

            if (elapsedTime >= timeout) {
                throw new Exception(format("Stability not reached after %d ms%n", timeout));
            }

            // Not stable, re-compute sleep time
            long nextSleepTime = sleepTime + INCREMENT_IN_MS;
            if ((elapsedTime + nextSleepTime) > timeout) {
                // Last increment is too large, shrink it to fit in the timeout boundaries
                sleepTime = timeout - sleepTime;
            } else {
                sleepTime = nextSleepTime;
            }

            //System.out.printf("Waiting for %d ms (%d elapsed)%n", sleepTime, elapsedTime);
            Thread.sleep(sleepTime);
        } while (true);

    }

    private boolean isStable(String url) {
        if (url == null) {
            return queueService.getWaiters() == 0 && queueService.getFinished() > 0;
        } else {
            try {
                webClient.getPage(url);
                Thread.sleep(3000);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}
