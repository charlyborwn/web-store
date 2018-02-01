package pti.test.dao.utils;

import pti.test.model.DTO.ProductDTO;
import pti.test.model.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Converts DTO to entity and entity do DTO classes.
 */
public class Converter {

    /**
     * Converts DTO instance to <code>Product</code> one.
     * @param productDTO DTO instance to be converted
     * @return <code>Product</code> instance
     */
    public static Product fromDTO(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setCount(productDTO.getCount());
        product.setType(productDTO.getType());
        product.setIpk(productDTO.getIpk());
        product.setPrice(productDTO.getPrice());
        product.setInfo(productDTO.getInfo());
        product.setPic(productDTO.getPic());
        product.setPics(productDTO.getPics());
        product.setComments(productDTO.getComments());
        product.setCountry(productDTO.getCountry());
        product.setSupplier(productDTO.getSupplier());

        return product;
    }

    /**
     * Converts <code>Product</code> entity to DTO instance.
     * @param product <code>Product</code> instance to be converted
     * @return DTO instance
     */
    public static ProductDTO toDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCount(product.getCount());
        productDTO.setType(product.getType());
        productDTO.setIpk(product.getIpk());
        productDTO.setPrice(product.getPrice());
        productDTO.setInfo(product.getInfo());
        productDTO.setPic(product.getPic());
        productDTO.setPics(product.getPics());
        productDTO.setComments(product.getComments());
        productDTO.setCountry(product.getCountry());
        productDTO.setSupplier(product.getSupplier());
        return productDTO;
    }

    /**
     * Converts <code>Product</code> collection to DTO reflection one.
     * @param products <code>Product</code> collection to be converted
     * @return <code>ProductDTO</code> collection
     */
    public static List<ProductDTO> DTOList(List<Product> products) {
        List<ProductDTO> list = products.stream().map(Converter::toDTO).collect(Collectors.toCollection(() -> new LinkedList<>()));
        return list;
    }

    /**
     * Updates <code>Product</code> from DTO reflection.
     * @param product <code>Product</code> entity to be updated
     * @param productDTO new or updated <code>ProductDTO</code> object
     * @return updated <code>Product</code> entity
     */
    public static Product update(Product product, ProductDTO productDTO) {
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPic(productDTO.getPic());
        product.setIpk(productDTO.getIpk());
        product.setCount(productDTO.getCount());
        product.setInfo(productDTO.getInfo());
        product.setType(productDTO.getType());
        product.setPrice(productDTO.getPrice());
        product.setPics(productDTO.getPics());
        product.setComments(productDTO.getComments());
        product.setCountry(productDTO.getCountry());
        product.setSupplier(productDTO.getSupplier());
        return product;
    }

}
