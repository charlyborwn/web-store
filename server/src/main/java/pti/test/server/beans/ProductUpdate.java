package pti.test.server.beans;


import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Type;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.ImagesEngine;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.TypeEngine;
import pti.test.server.interfaces.Update;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is responsible for updating information about product.
 */
@ManagedBean
@RestController
@ViewScoped
public class ProductUpdate implements Update {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private ImagesEngine imagesEngine;

    @Autowired
    private TypeEngine typeEngine;

    @Autowired
    private Logger logger;

    private long ipk;
    private ProductDTO newProduct = new ProductDTO();
    private UploadedFile uploadedFile;
    private HashMap<String, String> info = new HashMap<>();

    /**
     * For autocomplete in <code>JSF</code>
     */
    private Map<String, Map<String, String>> data = new HashMap<>();
    private Map<String, String> types;
    private Map<String, String> categories;
    private String category;
    private String type;

    /**
     * For galleria in <code>JSF</code>
     */
    private ArrayList<String> images;
    private StreamedContent mainImage;

    /**
     * Collects all fields values and creates new <code>ProductDTO</code> product
     * with same internal ID and saves it into DB.
     *
     * @throws IOException when any reading/writing error happens
     */
    @Override
    public void update() throws IOException {

        ProductDTO temp = getProductDTO();
        temp.setName(getName());
        temp.setCount(getCount());
        temp.setPrice(getPrice());
        if (category != null && type != null && !category.equals("") && !type.equals("")) {
            Type t = typeEngine.getTypeByCategory(category);
            temp.setType(t);
        }
        temp.setInfo(info);
        temp.setIpk(ipk);
        temp.setCountry(getCountry());
        temp.setSupplier(getSupplier());
        productEngine.save(temp);
        JSFMessages.info("The product " + getName() + " was successfully updated.");

    }

    /**
     * Creates new <code>ProductDTO</code> object.
     */
    public void initProduct() {
        if (newProduct.getIpk() != ipk) {
            newProduct = new ProductDTO();
            newProduct.setIpk(ipk);
        }
        types = new HashMap<>();
        imagesEngine.serveFileSystem(ipk);
    }

    /**
     * Loads categories of selected type to correspondent collection.
     */
    public void onTypeChange() {
        categories = new HashMap<>();
        if (type != null && !type.equals("")) {
            typeEngine.getTypeList().stream()
                    .filter(x -> x.getType().equals(type))
                    .map(x -> x.getCategory())
                    .forEach(x -> categories.put(x, x));
        }
    }

    /**
     * Responsible for correct additional info writing and reflecting.
     * If empty field or only spaces was entered then 'null'
     * or '0' will be placed in accordance to product characteristic.
     *
     * @param value name of additional info item
     * @return correct representing of additional info item value
     */
    private String propertyAnalysis(String value) {
        String defaultValue;
        if (value.equals("Date") | value.equals("Description")) {
            defaultValue = "null";
        } else {
            defaultValue = "0";
        }
        String prop = getProductDTO().getInfo().get(value).trim();//may replaced by light BD access
        if (newProduct.getInfo() != null) {
            String newProp = newProduct.getInfo().get(value);
            if (newProp == null) {
                return prop;
            }
            if (newProp.trim().equals("")) {
                return defaultValue;
            }
            return newProp;
        }
        if (prop.equals("")) {
            return defaultValue;
        }
        return prop;

    }

    /**
     * Uploads an image from file system with further proportional resizing and writing to collection.
     *
     * @param event image upload event
     * @throws IOException when any reading/writing error happens
     */
    public synchronized void upload(FileUploadEvent event) throws IOException {
        String localAddress = String.valueOf(ipk).concat("/images/");
        String address = imagesEngine.writeImage(imagesEngine.resizeImage(event.getFile(), -1, -1), "png",
                localAddress, Math.abs(UUID.randomUUID().hashCode()) + ".png");
        ProductDTO productDTO = getProductDTO();
        ArrayList<String> pics = productDTO.getPics();
        pics.add(address);
        productDTO.setPics(pics);
        productEngine.save(productDTO);
    }

    /**
     * Removes from DB all product's images.
     */
    public void clearFromDB() {
        ProductDTO product = getProductDTO();
        product.setPics(new ArrayList<>());
        productEngine.save(product);
        newProduct.setPics(new ArrayList<>());
        images = new ArrayList<>();
    }

    public void uploadMainImage(FileUploadEvent event) throws IOException {
        uploadedFile = event.getFile();
        ProductDTO productDTO = getProductDTO();
        productDTO.setPic(imagesEngine.writeImage(
                imagesEngine.resizeImage(uploadedFile, 150, 150), "png",
                String.valueOf(ipk).concat("/"), Math.abs(UUID.randomUUID().hashCode()) + ".png")
        );
        productEngine.save(productDTO);
        uploadedFile = null;
    }

    public StreamedContent getMainImage() throws IOException {
        return imagesEngine.streamedImage(getProductDTO().getPic());
    }

    /**
     * Reduces all product's fields to default except images.
     */
    public void setToDefault() {
        newProduct = productEngine.findByIpk(newProduct.getIpk());
    }

    public void deleteImageFromGalleria() {
        String address = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("address");
        imagesEngine.deleteImage(address);
    }

    private ProductDTO getProductDTO() {
        return productEngine.findByIpk(ipk);
    }

    public long getIpk() {
        return ipk;
    }

    public void setIpk(long ipk) {
        this.ipk = ipk;
    }

    public String getName() {
        if (newProduct.getName() == null) {
            return getProductDTO().getName();
        }
        return newProduct.getName();
    }

    public void setName(String name) {
        if (name.trim().equals("")) {
            return;
        }
        if (productEngine.findByName(name) != null) {
            return;
        }
        newProduct.setName(name);
    }

    public int getCount() {
        if (newProduct.getCount() == 0) {
            return getProductDTO().getCount();
        }
        return newProduct.getCount();
    }

    public void setCount(int count) {
        newProduct.setCount(count);
    }

    public double getPrice() {
        if (newProduct.getPrice() == 0.0) {
            return getProductDTO().getPrice();
        }
        return newProduct.getPrice();
    }

    public void setPrice(double price) {
        newProduct.setPrice(price);
    }

    public String getWarranty() {
        return propertyAnalysis("Warranty");
    }

    public void setWarranty(String warranty) {
        info.put("Warranty", warranty);
        newProduct.setInfo(info);
    }

    public String getDate() {
        return propertyAnalysis("Date");
    }

    public void setDate(String date) {
        info.put("Date", date);
        newProduct.setInfo(info);
    }

    public String getDiscount() {
        return propertyAnalysis("Discount");
    }

    public void setDiscount(String discount) {
        info.put("Discount", discount);
        newProduct.setInfo(info);
    }

    public String getDescription() {
        return propertyAnalysis("Description");
    }

    public void setDescription(String description) {
        info.put("Description", description);
        newProduct.setInfo(info);
    }

    public void setTYPE() {
        Type type = typeEngine.getTypeByCategory(category);
        newProduct.setType(type);
    }

    public Type getTYPE() {
        return getProductDTO().getType();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, String> getTypes() {
        Set<String> typeNames = typeEngine.getTypeList().stream().map(Type::getType)
                .collect(Collectors.toCollection(TreeSet::new));
        for (String s : typeNames) {
            types.put(s, s);
        }
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }

    public Map<String, String> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, String> categories) {
        this.categories = categories;
    }

    public Map<String, Map<String, String>> getData() {
        return data;
    }

    public void setData(Map<String, Map<String, String>> data) {
        this.data = data;
    }

    public ArrayList<String> getImages() {
        images = getProductDTO().getPics();
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getCountry() {
        if (newProduct.getCountry() == null) {
            String country = getProductDTO().getCountry();
            if (country.equals("")) {
                return "null";
            }
            return country;
        }
        if (newProduct.getCountry().trim().equals("")) {
            return "null";
        }
        return newProduct.getCountry();
    }

    public void setCountry(String country) {
        newProduct.setCountry(country);
    }

    public String getSupplier() {
        if (newProduct.getSupplier() == null) {
            String supplier = getProductDTO().getSupplier();
            if (supplier.equals("")) {
                return "null";
            }
            return supplier;
        }
        if (newProduct.getSupplier().trim().equals("")) {
            return "null";
        }
        return newProduct.getSupplier();
    }

    public void setSupplier(String supplier) {
        newProduct.setSupplier(supplier);
    }

    public ProductDTO getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(ProductDTO newProduct) {
        this.newProduct = newProduct;
    }

}

