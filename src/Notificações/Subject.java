package Notificações;

import java.util.List;
import java.util.ArrayList;
import GerenciamentoUsuarios.Usuario;

public class Subject {
    protected List<Observer> observers = new ArrayList<>();

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    // Notificação modelo PULL (passa o próprio objeto)
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
    }

    // Notificação modelo PUSH (passa os dados diretamente)
    public void notifyObserversPush(String mensagem, Usuario envolvido) {
        for (Observer o : observers) {
            o.update(mensagem, envolvido);
        }
    }
}
