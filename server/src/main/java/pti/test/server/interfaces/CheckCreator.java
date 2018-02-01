package pti.test.server.interfaces;

import pti.test.model.StoredProduct;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Responsible for generating information and writing it to PDF document.
 */
public interface CheckCreator {

    /**
     * Creates a byte array representing a PDF document.
     *
     * @param name
     * @param surname
     * @param phone
     * @param address
     * @param street
     * @param city
     * @param postalCode
     * @param payment
     * @param delivery
     * @param order
     * @param date
     * @return a byte array representing a PDF document
     */
    byte[] createCheck(String name, String surname, String phone, String address, String street, String city,
                       String postalCode, String payment, String delivery, List<StoredProduct> order,
                       LocalDateTime date);

}
