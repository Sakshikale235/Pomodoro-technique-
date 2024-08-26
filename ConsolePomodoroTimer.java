import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class ConsolePomodoroTimer {
    private static final int WORK_DURATION = 25 * 60 * 1000; // 25 minutes
    private static final int SHORT_BREAK_DURATION = 5 * 60 * 1000; // 5 minutes
    private static final int LONG_BREAK_DURATION = 15 * 60 * 1000; // 15 minutes
    private static final int CYCLES_BEFORE_LONG_BREAK = 4; // Cycles before long break

    private static int cycleCount = 0;
    private static Timer timer = new Timer();
    private static boolean focusInterrupted = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Pomodoro Timer Console Application");
        System.out.println("Press Enter to start the Pomodoro timer.");
        scanner.nextLine(); // Wait for user input

        // Start a thread to handle user commands
        new Thread(() -> {
            while (true) {
                System.out.println("Type 'end' to interrupt the current focus period.");
                String command = scanner.nextLine();
                if ("end".equalsIgnoreCase(command)) {
                    focusInterrupted = true;
                    System.out.println("Focus period interrupted. Ending current session.");
                    timer.cancel(); // Cancel the current timer tasks
                    return;
                }
            }
        }).start();

        startPomodoroCycle();
    }

    private static void startPomodoroCycle() {
        if (focusInterrupted) {
            return; // Exit if the focus was interrupted
        }

        if (cycleCount < CYCLES_BEFORE_LONG_BREAK) {
            startWorkPeriod();
        } else {
            startLongBreak();
        }
    }

    private static void startWorkPeriod() {
        System.out.println("Work period started. Focus for 25 minutes!");
        TimerTask workTask = new TimerTask() {
            @Override
            public void run() {
                if (!focusInterrupted) {
                    System.out.println("Work period ended. Take a short break.");
                    startShortBreak();
                }
            }
        };
        timer.schedule(workTask, WORK_DURATION);
    }

    private static void startShortBreak() {
        TimerTask breakTask = new TimerTask() {
            @Override
            public void run() {
                if (!focusInterrupted) {
                    System.out.println("Short break ended. Get ready for the next work period.");
                    cycleCount++;
                    startPomodoroCycle();
                }
            }
        };
        timer.schedule(breakTask, SHORT_BREAK_DURATION);
    }

    private static void startLongBreak() {
        System.out.println("Long break started. Relax for 15 minutes!");
        TimerTask longBreakTask = new TimerTask() {
            @Override
            public void run() {
                if (!focusInterrupted) {
                    System.out.println("Long break ended. Starting a new cycle.");
                    cycleCount = 0; // Reset cycle count
                    startPomodoroCycle();
                }
            }
        };
        timer.schedule(longBreakTask, LONG_BREAK_DURATION);
    }
}

