class LineProducer implements Runnable {
    private final MessageQueue<Line> q1;
    private FileIterator input;

    LineProducer(FileIterator input, MessageQueue<Line> q1) {
        this.q1 = q1;
        this.input = input;
    }

    @Override
    public void run() {
        synchronized (q1){
            FileIterator.MyIterator iterator = (FileIterator.MyIterator) input.iterator();
            while(iterator.hasNext()){
                Line l = iterator.next();
                q1.put(l);

            }
        }
    }
}

