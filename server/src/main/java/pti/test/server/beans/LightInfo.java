package pti.test.server.beans;

/**
 * Used for main product information collecting. Consists of
 * bought product name, external ID and count.
 * @author Syrotyuk R.
 */
public class LightInfo {

    private String name;
    private Long ipk;
    private int count;

    public LightInfo(String name, Long ipk, int count) {
        this.name = name;
        this.ipk = ipk;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
