package pti.test.server.beans;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Type;
import pti.test.server.JSFMessages;
import pti.test.server.interfaces.ImagesEngine;
import pti.test.server.interfaces.ProductEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for products main information list showing.
 */
@ManagedBean
@RestController
@ViewScoped
public class ProductsSimpleBean {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private ImagesEngine imagesEngine;

    @Autowired
    private Logger logger;

    private List<ProductDTO> products;
    private ProductDTO selectedProduct;
    private List<ProductDTO> filteredProduct;
    private String type;
    private String category;

    /**
     * Initiates a list of products.
     */
    public void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        if (context.getCurrentPhaseId() != PhaseId.RESTORE_VIEW) {
            filteredProduct = null;
        }
        if (type != null && !type.equals("") && category != null && !category.equals("")) {
            products = productEngine.getProducts().stream()
                    .filter(x -> x.getType().getType().equals(type) && x.getType().getCategory().equals(category))
                    .collect(Collectors.toList());
        } else if (type != null && !type.equals("")) {
            products = productEngine.getProducts().stream()
                    .filter(x -> x.getType().getType().equals(type))
                    .collect(Collectors.toList());
        } else {
            products = productEngine.getProducts();
        }
    }

    /**
     * Removes from DB the product with defined external key.
     */
    public void deleteProduct() {
        FacesContext context = FacesContext.getCurrentInstance();
        Long id = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("id"));
        ProductDTO dto = productEngine.findByIpk(id);
        imagesEngine.deleteProductImages(id);
        productEngine.delete(dto);
        filteredProduct = null;
        logger.info("Product was successfully deleted.");
        JSFMessages.info("Product was successfully deleted.");

    }

    public long productsCount() {
        return productEngine.count();
    }

    public List<ProductDTO> getProducts() {
        init();
        return products.stream().distinct().collect(Collectors.toList());
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public ProductDTO getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(ProductDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public List<ProductDTO> getFilteredProduct() {
        return filteredProduct;
    }

    public void setFilteredProduct(List<ProductDTO> filteredProduct) {
        this.filteredProduct = filteredProduct;
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
}
