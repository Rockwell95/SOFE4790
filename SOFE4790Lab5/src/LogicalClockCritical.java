public class LogicalClockCritical {

    private static int uniquePid = -1;
    private static int localClock = 0;

    public static void main(String[] args) {
        uniquePid = Integer.parseInt(args[0]);
        localClock++;
    }

    public void criticalSection(){

    }
}
