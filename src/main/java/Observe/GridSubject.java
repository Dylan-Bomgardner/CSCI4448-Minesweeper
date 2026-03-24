package Observe;

public interface GridSubject {
    void subscribe(GridObserver observer);
    void unsubscribe(GridObserver observer);
    void notifyObservers(String event, int x, int y);
}
