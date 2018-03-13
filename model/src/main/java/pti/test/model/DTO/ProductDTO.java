package pti.test.model.DTO;

import pti.test.model.Comment;
import pti.test.model.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Reflects <code>Product</code> entity class into working copy.
 *
 * @author Syrotyuk R.
 */
public class ProductDTO {

    private long id; //?
    private String name;
    private Type type;
    private double price;
    private int count;
    private HashMap<String, String> info;
    private long ipk;
    private String pic;
    private ArrayList<String> pics;
    private String country;
    private String supplier;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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

    public Comment getComments() {
        return comments;
    }

    public void setComments(Comment comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ipk=" + ipk +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDTO that = (ProductDTO) o;
        return ipk == that.ipk;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ipk);
    }

}
