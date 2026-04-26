package Observe;

public interface GridSubject {
    void register(GridObserver observer);
    void unregister(GridObserver observer);
    void notifyObservers(String event, int x, int y);
}
