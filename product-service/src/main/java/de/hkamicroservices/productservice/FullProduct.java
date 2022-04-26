package de.hkamicroservices.productservice;

public class FullProduct extends Product {
    private String categoryName;

    public FullProduct(Product baseProduct, CategoryDto categoryDto) {
        super();
        this.setId(baseProduct.getId());
        this.setDetails(baseProduct.getDetails());
        this.setName(baseProduct.getName());
        this.setPrice(baseProduct.getPrice());
        this.setCategoryId(baseProduct.getCategoryId());
        if (categoryDto != null)
            this.setCategoryName(categoryDto.getName());
    }


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
