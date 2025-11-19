package service;

public abstract class AbstractService<EntityDAO> {

    @SuppressWarnings("unused")
    private EntityDAO entityDAO;

    public AbstractService(){
        entityDAO = createEntityDAO();
    }

    public abstract EntityDAO createEntityDAO();

}
