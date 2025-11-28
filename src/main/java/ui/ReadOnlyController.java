package ui;

public interface ReadOnlyController {
    
    /**
     * Set read-only mode for this controller
     * @param readOnly true if controller should be in read-only mode (no editing/adding/deleting)
     */
    void setReadOnlyMode(boolean readOnly);
}