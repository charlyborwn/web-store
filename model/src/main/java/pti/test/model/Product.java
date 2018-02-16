package pti.test.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is the reflection of <code>Product</code> entity in DB
 * on the java class.
 * @author Syrotyuk R.
 */
@Entity(name = "product")
public class Product implements Serializable {

    public static final long serialVersionUID = 100L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "count", nullable = false)
    private int count;

    //make columns
    @Column(name = "info")
    private HashMap<String, String> info;

    @Column(name = "ipk", nullable = false)
    private long ipk;

    @Column(name = "PIC", nullable = false)
    private String pic;

    //make separate table
    @Lob
    @Column(name = "PICS", nullable = false)
    private ArrayList<String> pics;

    @ManyToOne
    private Type type;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "SUPPLIER")
    private String supplier;

    //make separate table
    @Lob
    @Column(name = "COMMENTS")
    private Comment comments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public void setInfo(HashMap<String, String> info) {
        this.info = info;
    }

    public long getIpk() {
        return ipk;
    }

    public void setIpk(long ipk) {
        this.ipk = ipk;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<String> getPics() {
        return pics;
    }

    public void setPics(ArrayList<String> pics) {
        this.pics = pics;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

    public Comment getComments() {
        return comments;
    }

}
