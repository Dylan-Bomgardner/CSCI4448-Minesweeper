package Observe;

public interface InputSubject {
    void register(InputObserver observer);
    void unregister(InputObserver observer);
    void notifyObservers(String event, int x, int y);
}
