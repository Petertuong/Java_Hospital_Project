package dao;

import java.util.ArrayList;

public interface DAOInterface<T, K> {

    //CRUD Operations
    public T create(T t);

    public T update(T t);

    public T delete(K k);

    //Other Operations
    public ArrayList<T> selectAll();

    public T selectById(K k);

    public ArrayList<T> selectByCondition(String condition);

}


