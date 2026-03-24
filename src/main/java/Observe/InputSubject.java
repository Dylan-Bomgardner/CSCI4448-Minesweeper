package Observe;

public interface InputSubject {
    void subscribe(InputObserver observer);
    void unsubscribe(InputObserver observer);
    void notifyObservers(String event, int x, int y);
}
