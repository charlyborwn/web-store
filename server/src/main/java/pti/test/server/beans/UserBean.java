package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.StoredProduct;
import pti.test.model.authorization.Users;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.UserEngine;

import javax.faces.bean.ManagedBean;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class is responsible for all users's information
 * showing including shopping history.
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class UserBean {

    @Autowired
    private UserEngine userEngine;

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private Logger logger;

    private Users user;
    private String mail;
    private HashMap<LocalDateTime, List<StoredProduct>> history;
    private HashMap<LocalDateTime, List<StoredProduct>> temp;
    private List<LocalDateTime> dates = new ArrayList<>();
    private List<LocalDateTime> tempDates = new ArrayList<>();
    private List<StoredProduct> products = new ArrayList<>();
    private List<StoredProduct> tempProducts = new ArrayList<>();

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

    /**
     * Forms a collection of bought products in certain chosen date.
     *
     * @return a list of <code>LightInfo</code> class that consists of
     * bought product name, external ID and count.
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

    public Users getUser() {
        return userEngine.findUserByMail(mail);
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getHistory() {
        if (userEngine.findUserByMail(mail).getUserhistory() == null) {
            return new HashMap<>();
        }
        return userEngine.findUserByMail(mail).getUserhistory();
    }

    public HashMap<LocalDateTime, List<StoredProduct>> getTemp() {
        if (userEngine.findUserByMail(mail).getUsertemp() == null) {
            return new HashMap<>();
        }
        return userEngine.findUserByMail(mail).getUsertemp();
    }

    public List<LocalDateTime> getTempDates() {
        ArrayList<LocalDateTime> list = new ArrayList<>(getTemp().keySet());
        list.sort(Comparator.reverseOrder());
        return list;
    }

    public void accept(LocalDateTime t) {
        HashMap<LocalDateTime, List<StoredProduct>> usertemp = getTemp();
        HashMap<LocalDateTime, List<StoredProduct>> history = getHistory();
        history.put(t, usertemp.get(t));
        usertemp.remove(t);
        Users user = userEngine.findUserByMail(mail);
        user.setUsertemp(usertemp);
        user.setUserhistory(history);
        userEngine.saveUser(user);
        logger.info("User's order accepted.");
    }

    public void selectedProducts(LocalDateTime selectedDate) {
        products = getHistory().get(selectedDate);
    }

    public void selectedTemps(LocalDateTime selectedDate) {
        tempProducts = getTemp().get(selectedDate);
    }

    public void clearTemps() {
        tempProducts = new ArrayList<>();
    }

}
