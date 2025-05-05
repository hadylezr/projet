package Services;

import java.util.List;

public interface iService<T>  {
    List<T> display() throws Exception ;
    void create(T t) throws Exception;
    void update(T t) throws Exception;
    void delete(int id) throws Exception;

}
