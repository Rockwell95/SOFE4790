import java.util.ArrayList;
import java.util.List;

// --------------------- FOR TASK 1 ------------------------
//class Program {
//    private static String query;
//
//    public static String getQuery() {
//        return query;
//    }
//
//    public static int getkDistance() {
//        return kDistance;
//    }
//
//    private static int kDistance;
//    public static void main(String[] args){
//        if(args.length < 4){
//            System.out.println("Usage: java Program [input file] [output file] [query string] [k-edit-distance]");
//            System.exit(0);
//        }
//        String inputFileName = args[0];
//        int repeat = 1;
//        String outputFileName = args[1];
//        query = args[2];
//        kDistance = Integer.parseInt(args[3]);
//        // setup the data flow
//        MessageQueue<Line> q1 = new MessageQueue<>();
//        FileIterator lines = new FileIterator(inputFileName,repeat);
//        LineProducer p1 = new LineProducer(lines, q1);
//        LineConsumer c1 = new LineConsumer(q1, outputFileName);
//        // place the producer and consumer in thread containers
//        List<Thread> threads = new ArrayList<>();
//        threads.add(new Thread(p1));
//        threads.add(new Thread(c1));
//        long start = System.currentTimeMillis();
//        // start the threads
//        threads.forEach(Thread::start);
//        // wait for the threads to complete
//        for(Thread t : threads) {
//            try {
//                t.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        long duration = System.currentTimeMillis() - start;
//        System.out.println("Total Duration: " + duration + " ms.");
//    }
//}

// --------------------- FOR TASK 2 ------------------------
class Program {
    private static String query;
    private static int kDistance;

    public static String getQuery() {
        return query;
    }

    public static int getkDistance() {
        return kDistance;
    }

    public static void main(String[] args) {
        if(args.length < 5){
            System.out.println("Usage: java Program [input file] [output file] [number of workers] [query string] [k-edit-distance]");
            System.exit(0);
        }
        String inputFileName = args[0];
        int repeat = 1;
        String outputFileName = args[1];
        int numWorkers = Integer.parseInt(args[2]);
        query = args[3];
        kDistance = Integer.parseInt(args[4]);
        FileIterator lines = new FileIterator(inputFileName, repeat); ;
        MessageQueue<Line> q1 = new MessageQueue<>();
        MessageQueue<Line> q2 = new MessageQueue<>() ;
        List<Thread> threads = new ArrayList<Thread>();
// create the producer thread
        threads.add(new Thread(new LineProducer(lines, q1)));
// create the LineConsumer threads
        for (int i = 0; i < numWorkers; i++)
            threads.add(new Thread(new LineConsumer(q1, q2)));
// create the ResultConsumer threads
        threads.add(new Thread(new ResultConsumer(q2,
                outputFileName)));
        // ...
        long start = System.currentTimeMillis();
        // start the threads
        threads.forEach(Thread::start);
        // wait for the threads to complete
        for(Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long duration = System.currentTimeMillis() - start;
        System.out.println("Total Duration: " + duration + " ms.");

    }
}