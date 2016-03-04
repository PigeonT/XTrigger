package de.darmstadt.tu.kom.XTrigger.datajavadao;

public interface IDAO<T> {
    void update(T t);
    void create(T t);
    T get();
    void delete(T t);
}
