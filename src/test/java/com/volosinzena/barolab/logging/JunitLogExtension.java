package com.volosinzena.barolab.logging;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JunitLogExtension implements BeforeAllCallback, AfterAllCallback, TestWatcher,
        BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(JunitLogExtension.class);
    private static final String PASSED = "passed";
    private static final String FAILED = "failed";
    private static final String ABORTED = "aborted";
    private static final String DISABLED = "disabled";

    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    @Override
    public void beforeAll(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(NAMESPACE);
        store.put(PASSED, new AtomicInteger());
        store.put(FAILED, new AtomicInteger());
        store.put(ABORTED, new AtomicInteger());
        store.put(DISABLED, new AtomicInteger());
        log.info("{}=== TESTS START: {} ==={}", CYAN, context.getDisplayName(), RESET);
    }

    @Override
    public void afterAll(ExtensionContext context) {
        ExtensionContext.Store store = context.getStore(NAMESPACE);
        int passed = store.get(PASSED, AtomicInteger.class).get();
        int failed = store.get(FAILED, AtomicInteger.class).get();
        int aborted = store.get(ABORTED, AtomicInteger.class).get();
        int disabled = store.get(DISABLED, AtomicInteger.class).get();
        int total = passed + failed + aborted + disabled;
        String color = failed > 0 ? RED : GREEN;
        log.info("{}=== TESTS END: {} | total={} passed={} failed={} aborted={} disabled={} ==={}",
                color, context.getDisplayName(), total, passed, failed, aborted, disabled, RESET);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        context.getStore(NAMESPACE).get(PASSED, AtomicInteger.class).incrementAndGet();
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        context.getStore(NAMESPACE).get(FAILED, AtomicInteger.class).incrementAndGet();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        context.getStore(NAMESPACE).get(ABORTED, AtomicInteger.class).incrementAndGet();
    }

    @Override
    public void testDisabled(ExtensionContext context, java.util.Optional<String> reason) {
        context.getStore(NAMESPACE).get(DISABLED, AtomicInteger.class).incrementAndGet();
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        log.info("{}--- TEST START: {} ---{}", YELLOW, context.getDisplayName(), RESET);
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        log.info("{}--- TEST END: {} ---{}", YELLOW, context.getDisplayName(), RESET);
    }
}
