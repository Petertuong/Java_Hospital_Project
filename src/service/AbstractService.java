package service;

public abstract class AbstractService<EntityDAO> {

    private EntityDAO entityDAO;

    public AbstractService(){
        entityDAO = createEntityDAO();
    }

    public abstract EntityDAO createEntityDAO();
}
