import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

class MessageQueue<Line> implements BlockingQueue<Line> {
    private ArrayDeque<Line> ar;
    MessageQueue() {
        ar = new ArrayDeque<>();
    }

    @Override
    public Line take() {
        return ar.removeFirst();
    }

    @Override
    public Line poll(long timeout, TimeUnit unit) throws InterruptedException {
        return null;
    }

    @Override
    public int remainingCapacity() {
        return 0;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Line> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public int size() {
        return ar.size();
    }

    @Override
    public boolean isEmpty() {
        return ar.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Line> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return ar.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public int drainTo(Collection<? super Line> c) {
        return 0;
    }

    @Override
    public int drainTo(Collection<? super Line> c, int maxElements) {
        return 0;
    }

    @Override
    public boolean add(Line line) {
        return false;
    }

    @Override
    public boolean offer(Line line) {
        return false;
    }

    @Override
    public Line remove() {
        return null;
    }

    @Override
    public Line poll() {
        return null;
    }

    @Override
    public Line element() {
        return null;
    }

    @Override
    public Line peek() {
        return null;
    }

    @Override
    public void put(Line m) {
        ar.add(m);
    }

    @Override
    public boolean offer(Line line, long timeout, TimeUnit unit) throws InterruptedException {
        return false;
    }
}
