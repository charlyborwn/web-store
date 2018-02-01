package pti.test.server.beans;

import com.itextpdf.text.DocumentException;
import org.apache.log4j.Logger;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.controller.Controller;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.StoredProduct;
import pti.test.model.authorization.Users;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.CheckCreator;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.UserActions;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.format.DateTimeFormatter.*;

/**
 * This class processes all users's storing actions
 * in cart and favourites pages. Secured by <code>SpringSecurity</code>.
 */
@ManagedBean
@RestController
@Scope("view")
public class SystemUserActionsBean implements UserActions {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private CheckCreator checkCreator;

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private Logger logger;

    private Users user;
    private ArrayList<ProductDTO> favourites = new ArrayList<>();
    private List<ProductDTO> droppedProducts = new ArrayList<>();
    private HashMap<Long, Integer> cart;

    /**
     * For buying dialog and check information generation
     */
    private String name;
    private String surname;
    private String mail;
    private String address;
    private String street;
    private String city;
    private String postalCode;
    private String phone;
    private String comment;
    private Date date = Date.from(Instant.now().plusSeconds(86400L));
    private String payment = "Cash";
    private String delivery = "self-removal";

    private byte[] document;

    /**
     * Adds product to cart. If product is not available then error message will be shown.
     */
    @Override
    public void addToCart() {

        FacesContext context = FacesContext.getCurrentInstance();
        long id = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("id"));
        ProductDTO product = productEngine.findByIpk(id);
        int count = product.getCount();
        if (count > 0) {
            cart = userEngine.getUser().getProducts();
            if (cart == null) {
                cart = new HashMap<>();
            }
            if (cart.containsKey(id)) {
                cart.put(id, cart.get(id) + 1);
            } else {
                cart.put(id, 1);
            }
            userEngine.setUserCart(cart);
            userEngine.saveUser();
            JSFMessages.info(product.getName() + " added to your cart!",
                    "Count: " + cart.get(id));
        } else {
            JSFMessages.warn("This product is not available now.");
        }

    }

    /**
     * Is responsible for user's logging out. If user logs out then
     * his cart will be cleared.
     *
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public String doLogout() throws ServletException, IOException {

        for (CartProducts p : getCart()) {
            ProductDTO productDTO = productEngine.findByIpk(p.getProductDTO().getIpk());
            int count = productDTO.getCount();
            productDTO.setCount(count + p.getCount());
            productEngine.save(productDTO);
        }
        clearCart();
        logger.info("User " + userEngine.getUser().getMail() + " logged out.");

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) externalContext.getRequest()).getRequestDispatcher("/logout");
        dispatcher.forward((ServletRequest) externalContext.getRequest(), (ServletResponse) externalContext.getResponse());
        facesContext.responseComplete();
        return null;

    }

    /**
     * Sets current user's cart products list to <code>null</code>.
     */
    private void clearCart() {
        userEngine.setUserCart(new HashMap<>());
        userEngine.saveUser();
    }

    /**
     * Gets the information of current user's cart.
     *
     * @return information of current user's cart
     */
    public List<CartProducts> getCart() {

        cart = userEngine.getUser().getProducts();
        List<CartProducts> products = new LinkedList<>();
        if (cart != null) {
            products.addAll(cart.keySet().stream().map(id -> new CartProducts(
                    productEngine.findByIpk(id), cart.get(id)))
                    .collect(Collectors.toList()));
        }
        return products;

    }

    /**
     * Is responsible for user's product's count reducing actions.
     *
     * @param p current product in cart.
     */
    public void reduceProductCount(CartProducts p) {

        HashMap<Long, Integer> products = userEngine.getUser().getProducts();
        Long id = p.getProductDTO().getIpk();
        if (products.get(id) == 1) {
            products.remove(id);
        } else {
            products.put(id, products.get(id) - 1);
        }
        userEngine.setUserCart(products);
        userEngine.saveUser();

    }

    /**
     * Is responsible for user's product's count inducing actions.
     *
     * @param p current product in cart.
     */
    public void induceProductCount(CartProducts p) {

        int count = productEngine.findByIpk(p.getProductDTO().getIpk()).getCount();
        if (count > 0) {
            HashMap<Long, Integer> products = userEngine.getUser().getProducts();
            Long id = p.getProductDTO().getIpk();
            products.put(id, products.get(id) + 1);
            userEngine.setUserCart(products);
            userEngine.saveUser();
        } else {
            JSFMessages.error("This product is not available now.");
        }

    }

    /**
     * Is responsible for user's deleting actions.
     *
     * @param p current product in cart.
     */
    public void deleteFromCart(CartProducts p) {
        HashMap<Long, Integer> products = userEngine.getUser().getProducts();
        Long id = p.getProductDTO().getIpk();
        products.remove(id);
        userEngine.setUserCart(products);
        userEngine.saveUser();
    }

    /**
     * Forms information about a price of products in the cart.
     *
     * @return price of products in the cart
     */
    public double allCost() {
        NumberFormat nf = new DecimalFormat("0.00");
        double sum = getCart().stream().mapToDouble(x -> x.getProductDTO().getPrice() * x.getCount()).sum();
        return Double.valueOf(nf.format(sum).replace(",", "."));
    }

    /**
     * Forms information about a count of products in the cart.
     *
     * @return count of products in the cart
     */
    public int allCount() {
        return getCart().stream().mapToInt(x -> x.getCount()).sum();
    }

    /**
     * Forms information about a price of current product in the cart.
     *
     * @return price of products in the cart
     */
    public double productCost(int count, double price) {
        NumberFormat nf = new DecimalFormat("0.00");
        return Double.valueOf(nf.format(count * price).replace(",", "."));
    }

    /**
     * Is responsible for user's buying actions. Adds all information
     * about purchasing to user's history. Reduces products's count
     * in accordance with stored product's counts.
     */
    @Override
    public void buy() {
        Users user = userEngine.getUser();
        if (user.getProducts() == null || user.getProducts().size() == 0) {
            logger.warn("Buying event. User: " + user.getMail() + ", cart: empty.");
            JSFMessages.warn("There is nothing to buy yet.");
            return;
        }
        HashMap<LocalDateTime, List<StoredProduct>> temp;
        List<StoredProduct> products = new ArrayList<>();
        if (user.getUsertemp() == null) {
            temp = new HashMap<>();
        } else {
            temp = user.getUsertemp();
        }
        getCart().stream().forEach(x -> products.add(new StoredProduct(x.getProductDTO().getIpk(), x.getCount())));
        temp.put(LocalDateTime.now(), products);
        userEngine.setUserTemp(temp);
        userEngine.saveUser();
        for (CartProducts p : getCart()) {
            ProductDTO productDTO = p.getProductDTO();
            int count = productDTO.getCount();
            if (count > p.getCount()) {
                productEngine.updateCount(count - p.getCount(), p.getProductDTO().getIpk());
            } else {
                productEngine.updateCount(0, p.getProductDTO().getIpk());
            }
        }
        clearCart();
        createCheck(products);
        JSFMessages.info("Operation successful!", "We waiting for you again!");
        logger.info("Buying event successful. User: " + user.getMail() + ".");

    }

    /**
     * Creates shopping check as byte array of PDF document.
     *
     * @param order ordered products info
     */
    private void createCheck(List<StoredProduct> order) {
        document = checkCreator.createCheck(userEngine.getUser().getName(), userEngine.getUser().getSurname(),
                getPhone(), getAddress(), getStreet(), getCity(), getPostalCode(), getPayment(), getDelivery(),
                order, LocalDateTime.now());
    }

    /**
     * Adds chosen product to user's favourites list if this product is not
     * already present there. Max favourites list size is 6.
     *
     * @param ipk an external product's ID obtained as the method parameter
     *            or from faces context.
     */
    public void addToFav(long ipk) {
        FacesContext context = FacesContext.getCurrentInstance();
        Long id = null;
        try {
            id = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("id"));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if (id != null) {
            ipk = id;
        }
        ArrayList<Long> favourites = userEngine.getUser().getFavourites();
        if (favourites == null) {
            favourites = new ArrayList<>();
        } else if (favourites.contains(ipk)) {
            JSFMessages.warn(productEngine.findByIpk(ipk).getName() + " already in your favourites!", null);
            return;
        } else if (favourites.size() > 5) {
            JSFMessages.warn("Only 6 favourites allowed.", "Manage its in your personal room.");
            return;
        }
        favourites.add(ipk);
        userEngine.setUserFavourites(favourites);
        userEngine.saveUser();
        JSFMessages.info(productEngine.findByIpk(ipk).getName() + " added to your favourites!", null);//+++++++
    }

    /**
     * Processes product dropping event.
     *
     * @param ddEvent
     */
    public void onProductDrop(DragDropEvent ddEvent) {
        ProductDTO productDTO = ((ProductDTO) ddEvent.getData());
        droppedProducts.add(productDTO);
        favourites.remove(productDTO);
        JSFMessages.info(productDTO.getName() + " moved into cart temporal!");
    }

    /**
     * Processes moving products from user's favourites list to cart.
     */
    public synchronized void fromFavouritesToCart() {
        ArrayList<ProductDTO> favourites = getFavourites();
        if (droppedProducts.size() < 1) {
            JSFMessages.error("There are nothing to buy.");
            return;
        }
        for (ProductDTO p : droppedProducts) {
            ProductDTO product = productEngine.findByIpk(p.getIpk());
            int count = product.getCount();
            if (count < 1) {
                JSFMessages.error("There are no available products in store: " + p.getName() + ".");
                break;
            }
            if (userEngine.getUser().getProducts() == null) {
                cart = new LinkedHashMap<>();
                cart.put(p.getIpk(), 1);
            } else {
                cart = userEngine.getUser().getProducts();
                if (cart.containsKey(p.getIpk())) {
                    cart.put(p.getIpk(), cart.get(p.getIpk()) + 1);
                } else {
                    cart.put(p.getIpk(), 1);
                }
            }
            userEngine.setUserCart(cart);
            userEngine.saveUser();
            favourites.remove(p);
            setFavourites(favourites);
        }
        ArrayList<Long> newFavourites = new ArrayList<>();
        getFavourites().forEach(x -> newFavourites.add(x.getIpk()));
        userEngine.setUserFavourites(newFavourites);
        userEngine.saveUser();
        clearDr();
        JSFMessages.info("All selected favourites are placed in your cart!");
    }

    /**
     * Clears drop area.
     */
    public void clearDr() {
        droppedProducts.clear();
    }

    /**
     * Processes deleting products from dropping area.
     *
     * @param id external product ID
     */
    public void deleteFromDropped(Long id) {
        favourites.add(productEngine.findByIpk(id));
        List<ProductDTO> dropped = droppedProducts.stream().filter(x -> x.getIpk() != id).collect(Collectors.toList());
        setDroppedProducts(dropped);
    }

    /**
     * Processes deleting products from user'sfavourites list.
     *
     * @param id external product ID
     */
    public void deleteFromFavs(Long id) {
        ArrayList<Long> favourites = userEngine.getUser().getFavourites();
        favourites.remove(id);
        userEngine.setUserFavourites(favourites);
        userEngine.saveUser();
        JSFMessages.info("Toy was removed from your favourites.");
    }

    public Users getUser() {
        return userEngine.getUser();
    }

    public ArrayList<ProductDTO> getFavourites() {
        if (droppedProducts.size() != 0) {
            return favourites;
        }
        ArrayList<Long> favourites = userEngine.getUser().getFavourites();
        if (favourites == null) {
            favourites = new ArrayList<>();
        }
        ArrayList<ProductDTO> favProds = new ArrayList<>();
        favourites.stream().forEach(x -> favProds.add(productEngine.findByIpk(x)));
        this.favourites = favProds;
        return favProds;
    }

    public void setFavourites(ArrayList<ProductDTO> favourites) {
        ArrayList<Long> favs = new ArrayList<>();
        favourites.forEach(x -> favs.add(x.getIpk()));
        userEngine.setUserFavourites(favs);
    }

    public List<ProductDTO> getDroppedProducts() {
        return droppedProducts;
    }

    public void setDroppedProducts(List<ProductDTO> droppedProducts) {
        this.droppedProducts = droppedProducts;
    }

    public String onFlowProcess(FlowEvent event) {
        return event.getNewStep();
    }

    public String getName() {
        if (getUser() != null & name == null) {
            return getUser().getName();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        if (getUser() != null & surname == null) {
            return getUser().getSurname();
        }
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        if (getUser() != null & mail == null) {
            return getUser().getMail();
        }
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getSimpleDate() {
        LocalDateTime instant = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return instant.format(ofPattern("yyyy-MM-dd"));
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public StreamedContent getDocument() throws DocumentException {
        return new DefaultStreamedContent(new ByteArrayInputStream(document),
                "application/pdf", "check.pdf");
    }

    /**
     * This class includes product in cart and accordant product's count.
     */
    public class CartProducts {

        private ProductDTO productDTO;
        private int count;

        public CartProducts(ProductDTO productDTO, int count) {
            this.productDTO = productDTO;
            this.count = count;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public ProductDTO getProductDTO() {
            return productDTO;
        }

        public void setProductDTO(ProductDTO productDTO) {
            this.productDTO = productDTO;
        }

    }

}
