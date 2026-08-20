import java.util.*;

public class OrderManagement {

    static class Product {
        String productId;
        String category;
        int quantity;
        double unitPrice;
        double discount;
        double tax;

        Product(String productId, String category, int quantity,
                double unitPrice, double discount, double tax) {
            this.productId = productId;
            this.category = category;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.discount = discount;
            this.tax = tax;
        }
    }

    static class OrderResult {
        double subtotal;
        double categoryDiscount;
        double couponDiscount;
        double discount;
        double gst;
        double shipping;
        double finalAmount;
        boolean success;
        String message;

        OrderResult(double subtotal, double categoryDiscount,
                    double couponDiscount, double discount,
                    double gst, double shipping,
                    double finalAmount, boolean success,
                    String message) {

            this.subtotal = subtotal;
            this.categoryDiscount = categoryDiscount;
            this.couponDiscount = couponDiscount;
            this.discount = discount;
            this.gst = gst;
            this.shipping = shipping;
            this.finalAmount = finalAmount;
            this.success = success;
            this.message = message;
        }
    }

    // Process order
    public static OrderResult processOrder(
            Product[] products, String coupon) {

        if (products == null || products.length == 0) {
            throw new IllegalArgumentException("Order cannot be empty");
        }

        double subtotal = 0;
        double categoryDiscount = 0;

        // Product processing
        for (Product p : products) {

            if (p == null) {
                throw new IllegalArgumentException("Invalid product");
            }

            if (p.productId == null || p.productId.isEmpty()) {
                throw new IllegalArgumentException("Invalid product ID");
            }

            if (p.quantity < 0) {
                throw new IllegalArgumentException(
                        "Negative quantity is not allowed");
            }

            if (p.quantity == 0) {
                throw new IllegalArgumentException(
                        "Quantity cannot be zero");
            }

            if (p.unitPrice <= 0) {
                throw new IllegalArgumentException(
                        "Invalid product price");
            }

            // Out-of-stock products
            if (p.quantity > 100) {
                throw new IllegalArgumentException(
                        "Product is out of stock");
            }

            double productTotal =
                    p.quantity * p.unitPrice;

            subtotal += productTotal;

            // Category-specific discount
            double categoryRate = 0;

            if (p.category.equalsIgnoreCase("Electronics")) {
                categoryRate = 10;
            }
            else if (p.category.equalsIgnoreCase("Clothing")) {
                categoryRate = 15;
            }
            else if (p.category.equalsIgnoreCase("Grocery")) {
                categoryRate = 5;
            }

            categoryDiscount +=
                    productTotal * categoryRate / 100;
        }

        // Maximum discount limit = 30%
        if (categoryDiscount > subtotal * 0.30) {
            categoryDiscount = subtotal * 0.30;
        }

        // Coupon discount
        double couponDiscount = 0;

        if (coupon != null && !coupon.isEmpty()) {

            if (coupon.equalsIgnoreCase("SAVE10")) {
                couponDiscount = subtotal * 0.10;
            }
            else if (coupon.equalsIgnoreCase("SAVE20")) {
                couponDiscount = subtotal * 0.20;
            }
            else {
                throw new IllegalArgumentException(
                        "Invalid coupon code");
            }
        }

        // Maximum total discount = 40%
        double totalDiscount =
                categoryDiscount + couponDiscount;

        if (totalDiscount > subtotal * 0.40) {
            totalDiscount = subtotal * 0.40;
        }

        // Bulk order discount
        int totalQuantity = 0;

        for (Product p : products) {
            totalQuantity += p.quantity;
        }

        if (totalQuantity >= 20) {
            double bulkDiscount = subtotal * 0.05;

            totalDiscount += bulkDiscount;

            if (totalDiscount > subtotal * 0.40) {
                totalDiscount = subtotal * 0.40;
            }
        }

        // Amount after discount
        double discountedAmount =
                subtotal - totalDiscount;

        // GST = 18%
        double gst = discountedAmount * 0.18;

        // Free shipping above Rs.1000
        double shipping;

        if (discountedAmount >= 1000) {
            shipping = 0;
        } else {
            shipping = 100;
        }

        // Final amount
        double finalAmount =
                discountedAmount + gst + shipping;

        return new OrderResult(
                subtotal,
                categoryDiscount,
                couponDiscount,
                totalDiscount,
                gst,
                shipping,
                finalAmount,
                true,
                "Order processed successfully"
        );
    }

    public static void main(String[] args) {

        // Built-in input
        Product[] products = {
            new Product(
                "P101",
                "Electronics",
                2,
                1000,
                10,
                18
            ),
            new Product(
                "P102",
                "Clothing",
                1,
                800,
                15,
                18
            )
        };

        String coupon = "SAVE10";

        try {

            OrderResult result =
                    processOrder(products, coupon);

            System.out.println(
                    "===== E-COMMERCE ORDER SYSTEM =====");

            System.out.printf(
                    "Subtotal          : Rs.%.2f\n",
                    result.subtotal);

            System.out.printf(
                    "Category Discount : Rs.%.2f\n",
                    result.categoryDiscount);

            System.out.printf(
                    "Coupon Discount   : Rs.%.2f\n",
                    result.couponDiscount);

            System.out.printf(
                    "Total Discount    : Rs.%.2f\n",
                    result.discount);

            System.out.printf(
                    "GST               : Rs.%.2f\n",
                    result.gst);

            System.out.printf(
                    "Shipping           : Rs.%.2f\n",
                    result.shipping);

            System.out.printf(
                    "Final Amount      : Rs.%.2f\n",
                    result.finalAmount);

            System.out.println(
                    "Status            : " + result.message);

            System.out.println(
                    "====================================");

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage());
        }
    }
}