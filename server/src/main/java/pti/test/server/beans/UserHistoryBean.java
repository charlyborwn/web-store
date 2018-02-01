package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.StoredProduct;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class is responsible for direct user's information
 * showing including user's shopping history. Secured by <code>SpringSecurity</code>.
 */
@ManagedBean
@RestController
@Scope("view")
public class UserHistoryBean {

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private Logger logger;

    //    private Users user;
    private String mail;
    private HashMap<LocalDateTime, List<StoredProduct>> history;
    private HashMap<LocalDateTime, List<StoredProduct>> temp;
    private List<LocalDateTime> dates = new ArrayList<>();
    private List<LocalDateTime> tempDates = new ArrayList<>();
    private List<StoredProduct> products = new ArrayList<>();
    private List<StoredProduct> tempProducts = new ArrayList<>();

    /**
     * Forms a collection of bought products in certain chosen date.
     *
     * @return a list of bought products
     */
    public List<LightInfo> getProductsHistoryInfo() {
        List<LightInfo> list = new ArrayList<>();
        for (StoredProduct st : products) {
            ProductDTO byIpk = productEngine.findByIpk(st.getIpk());
            if (byIpk != null) {
                list.add(new LightInfo(byIpk.getName(), st.getIpk(), st.getCount()));
            }
        }
        return list;
    }

    public List<LightInfo> getProductsTempInfo() {
        List<LightInfo> list = new ArrayList<>();
        for (StoredProduct st : tempProducts) {
            ProductDTO byIpk = productEngine.findByIpk(st.getIpk());
            if (byIpk != null) {
                list.add(new LightInfo(byIpk.getName(), st.getIpk(), st.getCount()));
            }
        }
        return list;
    }

    public String simplyDate(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")).concat(" ");
    }

    public String simplyTime(LocalDateTime time) {
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public void selectedProducts(LocalDateTime selectedDate) {
        products = getHistory().get(selectedDate);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getHistory() {
        if (userEngine.getUser().getUserhistory() == null) {
            return new HashMap<>();
        }
        return userEngine.getUser().getUserhistory();
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getTemp() {
        if (userEngine.getUser().getUsertemp() == null) {
            return new HashMap<>();
        }
        return userEngine.getUser().getUsertemp();
    }

    public List<LocalDateTime> getTempDates() {
        ArrayList<LocalDateTime> list = new ArrayList<>(getTemp().keySet());
        list.sort(Comparator.reverseOrder());
        return list;
    }

    /**
     * Declines current order and returns product count in DB to initial state.
     *
     * @param time time of order making
     */
    public void decline(LocalDateTime time) {
        HashMap<LocalDateTime, List<StoredProduct>> usertemp = getTemp();
        List<StoredProduct> products = usertemp.get(time);
        usertemp.remove(time);
        for (StoredProduct p : products) {
            try {
                int count = productEngine.findByIpk(p.getIpk()).getCount();
                count += p.getCount();
                productEngine.updateCount(count, p.getIpk());
            } catch (Exception e) {
                logger.warn("Product with ID " + p.getIpk() + " not exists.");
            }
        }
        userEngine.setUserTemp(usertemp);
        userEngine.saveUser();
        logger.info("User's order declined.");
    }

    /**
     * @return all dates when user bought a products in reverse time order.
     */
    public List<LocalDateTime> getDates() {
        HashMap<LocalDateTime, List<StoredProduct>> history = getHistory();
        if (history == null) {
            history = new HashMap<>();
        }
        dates.addAll(history.keySet());
        ArrayList<LocalDateTime> list = new ArrayList<>(new HashSet<>(dates));
        list.sort(Comparator.reverseOrder());
        return list;
    }

    public void selectedTemps(LocalDateTime selectedDate) {
        tempProducts = getTemp().get(selectedDate);
    }


}
