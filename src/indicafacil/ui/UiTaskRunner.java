package indicafacil.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Window;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.swing.SwingUtilities;

/*
 * Essa classe centraliza as tarefas em segundo plano.
 * Assim a tela continua respondendo enquanto o banco ou outras rotinas trabalham.
 */
public final class UiTaskRunner {
    private static final AtomicInteger THREAD_SEQUENCE = new AtomicInteger();
    private static final ExecutorService BACKGROUND_POOL = Executors.newFixedThreadPool(
        4,
        runnable -> {
            Thread thread = new Thread(runnable, "indicafacil-worker-" + THREAD_SEQUENCE.incrementAndGet());
            thread.setDaemon(true);
            return thread;
        }
    );

    private UiTaskRunner() {
    }

    public static ExecutorService getExecutor() {
        return BACKGROUND_POOL;
    }

    public static <T> void run(
        Component owner,
        String actionName,
        Supplier<T> backgroundAction,
        Consumer<T> onSuccess
    ) {
        run(owner, actionName, backgroundAction, () -> true, onSuccess, null);
    }

    public static <T> void run(
        Component owner,
        String actionName,
        Supplier<T> backgroundAction,
        Consumer<T> onSuccess,
        Runnable onFinally
    ) {
        run(owner, actionName, backgroundAction, () -> true, onSuccess, onFinally);
    }

    public static <T> void run(
        Component owner,
        String actionName,
        Supplier<T> backgroundAction,
        BooleanSupplier canApplyResult,
        Consumer<T> onSuccess
    ) {
        run(owner, actionName, backgroundAction, canApplyResult, onSuccess, null);
    }

    public static <T> void run(
        Component owner,
        String actionName,
        Supplier<T> backgroundAction,
        BooleanSupplier canApplyResult,
        Consumer<T> onSuccess,
        Runnable onFinally
    ) {
        setBusy(owner, true);

        BACKGROUND_POOL.submit(() -> {
            try {
                T result = backgroundAction.get();
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (canApplyResult.getAsBoolean()) {
                            onSuccess.accept(result);
                        }
                    } finally {
                        finish(owner, onFinally);
                    }
                });
            } catch (Exception exception) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (owner == null || owner.isDisplayable()) {
                            AppAlerts.showError(owner, buildErrorMessage(actionName, exception));
                        }
                    } finally {
                        finish(owner, onFinally);
                    }
                });
            }
        });
    }

    public static void runVoid(
        Component owner,
        String actionName,
        Runnable backgroundAction,
        Runnable onSuccess
    ) {
        run(owner, actionName, () -> {
            backgroundAction.run();
            return null;
        }, ignored -> {
            if (onSuccess != null) {
                onSuccess.run();
            }
        });
    }

    private static void finish(Component owner, Runnable onFinally) {
        setBusy(owner, false);
        if (onFinally != null) {
            onFinally.run();
        }
    }

    private static void setBusy(Component owner, boolean busy) {
        if (owner == null) {
            return;
        }

        Window window = SwingUtilities.getWindowAncestor(owner);
        if (window != null) {
            window.setCursor(Cursor.getPredefinedCursor(busy ? Cursor.WAIT_CURSOR : Cursor.DEFAULT_CURSOR));
        }
    }

    private static String buildErrorMessage(String actionName, Exception exception) {
        String message = exception.getMessage();
        if (message != null && !message.isBlank()) {
            return message;
        }

        return "Nao foi possivel concluir a acao: " + actionName + ".";
    }
}
