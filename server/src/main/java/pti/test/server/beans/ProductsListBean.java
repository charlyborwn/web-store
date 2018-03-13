package pti.test.server.beans;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;
import pti.test.model.DTO.ProductDTO;
import pti.test.model.Type;
import pti.test.server.interfaces.ProductEngine;
import pti.test.server.interfaces.TypeEngine;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for products list demonstration.
 *
 * @author Syrotyuk R.
 */
@ManagedBean
@RestController
@Scope("view")
public class ProductsListBean {

    @Autowired
    private ProductEngine productEngine;

    @Autowired
    private TypeEngine typeEngine;

    @Autowired
    private Logger logger;

    private List<ProductDTO> productsDTO;

    /**
     * For filtering, selecting and searching products.
     */
    private ProductDTO selectedProduct;
    private String searchRequest;
    private String selectedCategory;

    /**
     * Initiates the products list from DB. Fills list with all products in default case
     * (if no any filtering or searching requests are present). If search request is not empty
     * then only products with names that satisfy search request will be written into product list.
     * If filtering request or both search and filtering request are not null then
     * only approaching products will be written.
     */
    public void init() {
        if ((searchRequest != null && !searchRequest.equals("")) & selectedCategory != null) {
            if (isType(selectedCategory)) {
                productsDTO = productEngine.getProducts().stream()
                        .filter(x -> x.getType().getType().equals(selectedCategory))
                        .filter(x -> x.getName().toLowerCase().contains(searchRequest.toLowerCase()))
                        .collect(Collectors.toList());
            } else {
                productsDTO = productEngine.getProducts().stream()
                        .filter(x -> x.getType().getCategory().equals(selectedCategory))
                        .filter(x -> x.getName().toLowerCase().contains(searchRequest.toLowerCase()))
                        .collect(Collectors.toList());
            }
        } else if (searchRequest != null && !searchRequest.equals("")) {
            productsDTO = productEngine.getProducts().stream()
                    .filter(x -> x.getName().toLowerCase().contains(searchRequest.toLowerCase()))
                    .collect(Collectors.toList());
        } else if (selectedCategory != null) {
            if (isType(selectedCategory)) {
                productsDTO = productEngine.getProducts().stream().filter(x -> x.getType().getType().equals(selectedCategory))
                        .collect(Collectors.toList());
            } else {
                productsDTO = productEngine.getProducts().stream().filter(x -> x.getType().getCategory().equals(selectedCategory))
                        .collect(Collectors.toList());
            }
        } else {
            productsDTO = productEngine.getProducts();
        }
    }

    /**
     * Determines the parameter representing item - type or category.
     *
     * @param s filtering request
     * @return <code>true</code> if request represents type, <code>false</code>
     * if request represents category
     */
    private boolean isType(String s) {
        List<Type> types = typeEngine.getTypeList();
        if (types.stream().anyMatch(x -> x.getType().equals(s))) {
            return true;
        }
        return false;
    }

    public List<ProductDTO> getProductsDTO() {
        init();
        return productsDTO;
    }

    public void setProductsDTO(List<ProductDTO> productsDTO) {
        this.productsDTO = productsDTO;
    }

    /**
     * Determines selected product using faces context
     * for dialog window.
     *
     * @return selected product
     */
    public ProductDTO getSelectedProduct() {
        FacesContext context = FacesContext.getCurrentInstance();
        String id = context.getExternalContext().getRequestParameterMap().get("id");
        return id == null ? selectedProduct :
                productsDTO.stream().filter(x -> x.getIpk() == Long.valueOf(id)).collect(Collectors.toList()).get(0);
    }

    public void setSelectedProduct(ProductDTO selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public String getSearchRequest() {
        return searchRequest;
    }

    public void setSearchRequest(String searchRequest) {
        this.searchRequest = searchRequest;
    }

    public String getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

}
