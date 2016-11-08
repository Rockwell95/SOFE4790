import javax.xml.transform.Result;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

class ResultConsumer implements Runnable {
    private final MessageQueue<Line> q2;
    private String outputFileName;

    ResultConsumer(MessageQueue<Line> q2, String outputFileName){
        this.q2 = q2;
        this.outputFileName = outputFileName;
    }

    @Override
    public void run() {
        synchronized (q2) {
            try {
                PrintWriter out = new PrintWriter(outputFileName);;
                while (!q2.isEmpty()) {
                    String line = q2.take().content;
                    out.println(line);
                    System.out.println(line);
                }
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
