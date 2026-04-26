package Observe;

public interface ObserveSubject {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers(String event, Object data);
}
