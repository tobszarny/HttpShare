package pl.biltech.httpshare.httpd.runner.impl;

import pl.biltech.httpshare.httpd.runner.AsyncRunner;
import pl.biltech.httpshare.httpd.runner.ClientHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * BoundRunner
 * <p>
 * The default threading strategy for NanoHTTPD launches a new thread every time. We override that here so we can put an
 * upper limit on the number of active threads using a thread pool.
 */
public class BoundRunner implements AsyncRunner {
    private ExecutorService executorService;
    private final List<ClientHandler> running =
            Collections.synchronizedList(new ArrayList<ClientHandler>());

    public BoundRunner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void closeAll() {
        // copy of the list for concurrency
        for (ClientHandler clientHandler : new ArrayList<>(this.running)) {
            clientHandler.close();
        }
    }

    @Override
    public void closed(ClientHandler clientHandler) {
        this.running.remove(clientHandler);
    }

    @Override
    public void exec(ClientHandler clientHandler) {
        executorService.submit(clientHandler);
        this.running.add(clientHandler);
    }

}