package pti.test.model.authorization;

import pti.test.model.StoredProduct;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This is the reflection of <code>Users</code> entity in DB
 * on the java class. Holds user's data.
 *
 * @author Syrotyuk R.
 */
@Entity(name = "users")
public class Users implements Serializable {

    public static final long serialVersionUID = 100L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private String role;

    @Lob
    @Column(name = "products")
    private HashMap<Long, Integer> products;

    @Column(name = "favourites")
    private ArrayList<Long> favourites;

    @Lob
    @Column(name = "userhistory")
    private HashMap<LocalDateTime, List<StoredProduct>> userhistory;

    @Lob
    @Column(name = "usertemp")
    private HashMap<LocalDateTime, List<StoredProduct>> usertemp;

    public Users() {
    }

    public Users(String name, String surname, String mail, String password, String role) {
        this.name = name;
        this.surname = surname;
        this.mail = mail;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HashMap<Long, Integer> getProducts() {
        return products;
    }

    public void setProducts(HashMap<Long, Integer> products) {
        this.products = products;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<Long> getFavourites() {
        return favourites;
    }

    public void setFavourites(ArrayList<Long> favourites) {
        this.favourites = favourites;
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getUserhistory() {
        return userhistory;
    }

    public void setUserhistory(HashMap<LocalDateTime, List<StoredProduct>> userhistory) {
        this.userhistory = userhistory;
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getUsertemp() {
        return usertemp;
    }

    public void setUsertemp(HashMap<LocalDateTime, List<StoredProduct>> usertemp) {
        this.usertemp = usertemp;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", mail='" + mail + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(mail, users.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail);
    }

}
