package pti.test.server.interfaces;

import java.io.IOException;

/**
 * This interface is responsible for updating information about entity.
 * @author Syrotyuk R.
 */
public interface Update {

    /**
     * Updates entity and saves it into DB.
     *
     * @throws IOException when any reading/writing error happens
     */
    void update() throws IOException;

}
