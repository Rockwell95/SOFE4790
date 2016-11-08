import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

class LineConsumer implements Runnable {
    private final MessageQueue<Line> q1;
    private final MessageQueue<Line> q2;
    private String outputFileName;

    LineConsumer(MessageQueue<Line> q1, String outputFileName) {
        this.q1 = q1;
        this.q2 = null;
        this.outputFileName = outputFileName;
    }

    LineConsumer(MessageQueue<Line> q1, MessageQueue<Line> q2){
        this.q1 = q1;
        this.q2 = q2;
        this.outputFileName = null;
    }

//    @Override
//    public void run() {
//        synchronized (q1) {
//            try {
//                PrintWriter out = new PrintWriter(outputFileName);;
//                while (!q1.isEmpty()) {
//                    String line = q1.take().content;
//                    if (line != null && Util.editDistance(Program.query, line) <= Program.kDistance) {
//                        out.println(line);
//                        System.out.println(line);
//                    }
//                }
//                out.close();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public void run() {
        assert q2 != null;
        synchronized (q2) {
            synchronized (q1){
                while (!q1.isEmpty()) {
                    Line line = q1.take();
                    if (line.content != null && Util.editDistance(Program.query, line.content) <= Program.kDistance) {
                        q2.put(line);
                    }
                }
            }
        }
    }
}