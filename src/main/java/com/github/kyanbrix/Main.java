package com.github.kyanbrix;

import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Console mouse jiggler: nudges the cursor 1px and back every N seconds so the
 * OS idle timer (which Teams uses for "Away") keeps resetting.
 *
 * Usage: java -jar jiggler.jar [intervalSeconds]
 */
public class Main {

    private static final int DEFAULT_INTERVAL_SECONDS = 30;

    public static void main(String[] args) throws InterruptedException {
        int intervalSeconds = parseInterval(args);

        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("No display available; the jiggler needs a desktop session.");
            System.exit(1);
        }

        Robot robot;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("Could not control the mouse: " + e.getMessage());
            System.exit(1);
            return;
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Jiggler stopped.")));

        System.out.println("Mouse jiggler running, nudging every " + intervalSeconds + "s.");
        System.out.println("Press Ctrl+C to stop.");

        long count = 0;
        while (true) {
            Thread.sleep(intervalSeconds * 1000L);

            // Null while the screen is locked or on the secure desktop
            PointerInfo pointer = MouseInfo.getPointerInfo();
            if (pointer == null) {
                log("pointer unavailable (screen locked?), skipped");
                continue;
            }

            Point p = pointer.getLocation();
            robot.mouseMove(p.x + 1, p.y);
            robot.delay(50);
            robot.mouseMove(p.x, p.y);
            log("nudged (#" + (++count) + ")");
        }
    }

    private static int parseInterval(String[] args) {
        if (args.length == 0) {
            return DEFAULT_INTERVAL_SECONDS;
        }
        try {
            int seconds = Integer.parseInt(args[0]);
            if (seconds >= 1) {
                return seconds;
            }
        } catch (NumberFormatException ignored) {
            // fall through to usage
        }
        System.err.println("Usage: java -jar jiggler.jar [intervalSeconds]  (default "
                + DEFAULT_INTERVAL_SECONDS + ", must be >= 1)");
        System.exit(1);
        return -1;
    }

    private static void log(String message) {
        System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] " + message);
    }
}
