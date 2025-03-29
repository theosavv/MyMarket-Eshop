package api;

/**
 * This class represents a product with attributes such as title, description, category, subcategory, price, quantity, and measurement unit.
 * Provides methods for accessing and modifying product details and creating a copy of the product.
 */
public class Product {


    private String productTitle;
    private String productDescription;
    private String category;
    private String subCategory;
    private double productPrice;
    private int productQuantity;
    private String measurementUnit;

    /**
     * Constructs a new {@link Product} with the specified details.
     *
     * @param productTitle The title or name of the product.
     * @param productDescription A brief description of the product.
     * @param category The main category of the product.
     * @param subCategory The subcategory of the product.
     * @param productPrice The price of the product.
     * @param productQuantity The quantity of the product in stock.
     * @param measurementUnit The unit of measurement for the product's quantity (e.g., "kg" or "τεμάχια").
     */
    public Product(String productTitle, String productDescription, String category, String subCategory
                   ,double productPrice, int productQuantity,String measurementUnit) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.category = category;
        this.subCategory = subCategory;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.measurementUnit = measurementUnit;
    }

    /**
     * Creates and returns a copy of the current {@link Product}.
     *
     * @return A new {@link Product} object with the same attributes as the current product.
     */
    public Product copy()
    {
        return new Product(productTitle, productDescription, category, subCategory, productPrice, productQuantity, measurementUnit);
    }


    /**
     * Gets the title of the product.
     *
     * @return The title of the product.
     */
    public String getProductTitle() {
        return productTitle;
    }

    /**
     * Gets the description of the product.
     *
     * @return The description of the product.
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * Gets the category of the product.
     *
     * @return The category of the product.
     */
    public String  getProductCategory() {
        return category;
    }

    /**
     * Gets the subcategory of the product.
     *
     * @return The subcategory of the product.
     */
    public String getProductSubCategory() {
        return subCategory;
    }

    /**
     * Gets the price of the product.
     *
     * @return The price of the product.
     */
    public double getProductPrice() {
        return productPrice;
    }

    /**
     * Gets the quantity of the product.
     *
     * @return The quantity of the product.
     */
    public int getProductQuantity() {
        return productQuantity;
    }

    /**
     * Gets the measurement unit for the product's quantity.
     *
     * @return The measurement unit for the product's quantity (e.g., "kg" or "τεμάχια").
     */
    public String getProductMeasurementUnit() {
        return measurementUnit;
    }

    /**
     * Sets the title of the product.
     *
     * @param productTitle The new title of the product.
     */
    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    /**
     * Sets the description of the product.
     *
     * @param productDescription The new description of the product.
     */
    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    /**
     * Sets the category of the product.
     *
     * @param category The new category of the product.
     */
    public void setProductCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the subcategory of the product.
     *
     * @param subCategory The new subcategory of the product.
     */
    public void setProductSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * Sets the price of the product.
     *
     * @param productPrice The new price of the product.
     * @return {@code true} if the price is successfully set, or {@code false} if the price is negative.
     */
    public boolean setProductPrice(double productPrice) {
        if(productPrice < 0) {
            return false;
        }
        this.productPrice = productPrice;
        return true;
    }

    /**
     * Sets the quantity of the product.
     *
     * @param productQuantity The new quantity of the product.
     * @return {@code true} if the quantity is successfully set, or {@code false} if the quantity is negative.
     */
    public boolean setProductQuantity(int productQuantity) {
        if(productQuantity < 0) {
            return false;
        }
        this.productQuantity = productQuantity;
        return true;
    }

    /**
     * Sets the measurement unit for the product's quantity.
     *
     * @param measurementUnit The new measurement unit for the product's quantity (e.g., "kg" or "τεμάχια").
     * @return {@code true} if the measurement unit is successfully set, or {@code false} if the unit is invalid.
     */
    public boolean setProductMeasurementUnit(String measurementUnit) {
        if( !measurementUnit.equals("kg") && !measurementUnit.equals("τεμάχια"))
            return false;
        this.measurementUnit = measurementUnit;
        return true;
    }



}