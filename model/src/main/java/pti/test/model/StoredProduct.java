package pti.test.model;

import java.io.Serializable;

/**
 * This class used for stored products placing into user's <code>History</code>
 * and user's <code>Cart</code>.
 */
public class StoredProduct implements Serializable{

    private Long ipk;
    private int count;

    public StoredProduct(Long ipk, int count) {
        this.ipk = ipk;
        this.count = count;
    }

    public Long getIpk() {
        return ipk;
    }

    public void setIpk(Long ipk) {
        this.ipk = ipk;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
