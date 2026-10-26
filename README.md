# MouseJiggler

A tiny console mouse jiggler that keeps the PC (and Microsoft Teams) from going idle.
It nudges the cursor 1px and back every N seconds. No install or build needed.

## Run

Requires Java 21 (runs the source file directly, no compile step):

```
java Main.java        # nudge every 30 seconds (default)
java Main.java 10     # nudge every 10 seconds
```

Run from the folder containing `Main.java` (`src/main/java/com/github/kyanbrix`),
or pass the full path. Press `Ctrl+C` to stop.
