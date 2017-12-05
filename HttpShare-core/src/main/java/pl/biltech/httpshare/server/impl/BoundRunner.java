package pl.biltech.httpshare.server.impl;

import fi.iki.elonen.NanoHTTPD;

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
public class BoundRunner implements NanoHTTPD.AsyncRunner {
    private ExecutorService executorService;
    private final List<NanoHTTPD.ClientHandler> running =
            Collections.synchronizedList(new ArrayList<NanoHTTPD.ClientHandler>());

    public BoundRunner(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public void closeAll() {
        // copy of the list for concurrency
        for (NanoHTTPD.ClientHandler clientHandler : new ArrayList<>(this.running)) {
            clientHandler.close();
        }
    }

    @Override
    public void closed(NanoHTTPD.ClientHandler clientHandler) {
        this.running.remove(clientHandler);
    }

    @Override
    public void exec(NanoHTTPD.ClientHandler clientHandler) {
        executorService.submit(clientHandler);
        this.running.add(clientHandler);
    }
}