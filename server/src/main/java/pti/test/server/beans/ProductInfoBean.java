package pti.test.server.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import javax.faces.bean.ManagedBean;
import java.util.HashMap;

/**
 * This is a container of additional product information. Consists of
 * <code>HashMap</code> collection in which this information holds.
 */

@ManagedBean
@RestController
@Scope("view")
public class ProductInfoBean {

    private HashMap<String, String> info = new HashMap<>();
    private String date;
    private int warranty;
    private int discount;
    private String description;

    /**
     * Getter of additional product information collection.
     * @return <code>HashMap</code> collection
     */
    public HashMap<String, String> getInfo() {
        info.put("Date", date);
        info.put("Warranty", String.valueOf(warranty));
        info.put("Discount", String.valueOf(discount));
        info.put("Description", description);
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
