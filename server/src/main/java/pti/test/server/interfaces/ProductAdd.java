package pti.test.server.interfaces;

import java.io.IOException;

/**
 * This interface corresponds for adding new ProductDTO to DB in case of success verifying process.
 * @author Syrotyuk R.
 */
public interface ProductAdd {

    /**
     * Creates and adds product object to DB.
     * @throws IOException
     */
    void createProduct() throws IOException;

}
