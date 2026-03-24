package Observe;

public interface ObserveSubject {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void notifyObservers(String event, Object data);
}