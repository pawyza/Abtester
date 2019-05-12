package ib.edu.Abtester.interfaces;

public interface Observable {

    public void addObserver(Observer observer); //dodaje obiekt boserver
    public void deleteObserver(Observer observer); //usuwa obiekt observer
    public void deleteObservers(); //czysci liste
    public void setChanged(); //zmiana informacji o zmianie stanu
    public boolean checkIfChanged(); //zmiana informacji o zmianie stanu
    public void notifyObservers(); //przekazanie parametrow
}
