public class OrderManagementQA {

    static int passed = 0;
    static int failed = 0;

    static void check(String testName, boolean condition) {

        if (condition) {
            System.out.println("[PASS] " + testName);
            passed++;
        } else {
            System.out.println("[FAIL] " + testName);
            failed++;
        }
    }

    static OrderManagement.Product product(
            String id,
            String category,
            int quantity,
            double price) {

        return new OrderManagement.Product(
                id, category, quantity, price, 0, 18);
    }

    public static void main(String[] args) {

        System.out.println(
                "========== ORDER MANAGEMENT QA ==========\n");


        // 1. Single product
        try {
            OrderManagement.Product[] p = {
                product("P1", "Electronics", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Single product", r.success);

        } catch (Exception e) {
            check("Single product", false);
        }


        // 2. Multiple products
        try {
            OrderManagement.Product[] p = {
                product("P1", "Electronics", 2, 1000),
                product("P2", "Clothing", 2, 500)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Multiple products", r.success);

        } catch (Exception e) {
            check("Multiple products", false);
        }


        // 3. Zero quantity
        try {
            OrderManagement.Product[] p = {
                product("P3", "Grocery", 0, 500)
            };

            OrderManagement.processOrder(p, null);

            check("Zero quantity", false);

        } catch (IllegalArgumentException e) {
            check("Zero quantity", true);
        }


        // 4. Negative quantity
        try {
            OrderManagement.Product[] p = {
                product("P4", "Grocery", -2, 500)
            };

            OrderManagement.processOrder(p, null);

            check("Negative quantity", false);

        } catch (IllegalArgumentException e) {
            check("Negative quantity", true);
        }


        // 5. Invalid product ID
        try {
            OrderManagement.Product[] p = {
                product("", "Electronics", 1, 1000)
            };

            OrderManagement.processOrder(p, null);

            check("Invalid product", false);

        } catch (IllegalArgumentException e) {
            check("Invalid product", true);
        }


        // 6. Invalid product price
        try {
            OrderManagement.Product[] p = {
                product("P6", "Electronics", 1, -500)
            };

            OrderManagement.processOrder(p, null);

            check("Invalid product price", false);

        } catch (IllegalArgumentException e) {
            check("Invalid product price", true);
        }


        // 7. Invalid coupon
        try {
            OrderManagement.Product[] p = {
                product("P7", "Electronics", 1, 1000)
            };

            OrderManagement.processOrder(p, "WRONG");

            check("Invalid coupon", false);

        } catch (IllegalArgumentException e) {
            check("Invalid coupon", true);
        }


        // 8. SAVE10 coupon
        try {
            OrderManagement.Product[] p = {
                product("P8", "Electronics", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, "SAVE10");

            check("SAVE10 coupon", r.couponDiscount > 0);

        } catch (Exception e) {
            check("SAVE10 coupon", false);
        }


        // 9. SAVE20 coupon
        try {
            OrderManagement.Product[] p = {
                product("P9", "Electronics", 1, 2000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, "SAVE20");

            check("SAVE20 coupon", r.couponDiscount > 0);

        } catch (Exception e) {
            check("SAVE20 coupon", false);
        }


        // 10. Maximum discount limit
        try {
            OrderManagement.Product[] p = {
                product("P10", "Clothing", 10, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, "SAVE20");

            check("Maximum discount limit",
                    r.discount <= r.subtotal * 0.40 + 0.01);

        } catch (Exception e) {
            check("Maximum discount limit", false);
        }


        // 11. Electronics category discount
        try {
            OrderManagement.Product[] p = {
                product("P11", "Electronics", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Electronics category discount",
                    Math.abs(r.categoryDiscount - 100) < 0.01);

        } catch (Exception e) {
            check("Electronics category discount", false);
        }


        // 12. Clothing category discount
        try {
            OrderManagement.Product[] p = {
                product("P12", "Clothing", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Clothing category discount",
                    Math.abs(r.categoryDiscount - 150) < 0.01);

        } catch (Exception e) {
            check("Clothing category discount", false);
        }


        // 13. Grocery category discount
        try {
            OrderManagement.Product[] p = {
                product("P13", "Grocery", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Grocery category discount",
                    Math.abs(r.categoryDiscount - 50) < 0.01);

        } catch (Exception e) {
            check("Grocery category discount", false);
        }


        // 14. GST calculation
        try {
            OrderManagement.Product[] p = {
                product("P14", "Grocery", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            double expectedGST =
                    (1000 - 50) * 0.18;

            check("GST calculation",
                    Math.abs(r.gst - expectedGST) < 0.01);

        } catch (Exception e) {
            check("GST calculation", false);
        }


        // 15. Free shipping threshold
        try {
            OrderManagement.Product[] p = {
                product("P15", "Electronics", 2, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Free shipping",
                    r.shipping == 0);

        } catch (Exception e) {
            check("Free shipping", false);
        }


        // 16. Paid shipping
        try {
            OrderManagement.Product[] p = {
                product("P16", "Grocery", 1, 100)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Paid shipping",
                    r.shipping == 100);

        } catch (Exception e) {
            check("Paid shipping", false);
        }


        // 17. Bulk order
        try {
            OrderManagement.Product[] p = {
                product("P17", "Grocery", 20, 100)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            check("Bulk order discount",
                    r.discount > r.categoryDiscount);

        } catch (Exception e) {
            check("Bulk order discount", false);
        }


        // 18. Out-of-stock product
        try {
            OrderManagement.Product[] p = {
                product("P18", "Electronics", 101, 1000)
            };

            OrderManagement.processOrder(p, null);

            check("Out-of-stock product", false);

        } catch (IllegalArgumentException e) {
            check("Out-of-stock product", true);
        }


        // 19. Empty order
        try {
            OrderManagement.Product[] p = {};

            OrderManagement.processOrder(p, null);

            check("Empty order handling", false);

        } catch (IllegalArgumentException e) {
            check("Empty order handling", true);
        }


        // 20. Final amount calculation
        try {
            OrderManagement.Product[] p = {
                product("P20", "Grocery", 1, 1000)
            };

            OrderManagement.OrderResult r =
                    OrderManagement.processOrder(p, null);

            double expectedDiscount = 50;
            double discountedAmount = 1000 - expectedDiscount;
            double expectedGST = discountedAmount * 0.18;
            double expectedShipping = 100;

            double expectedFinal =
                    discountedAmount +
                    expectedGST +
                    expectedShipping;

            check("Final amount calculation",
                    Math.abs(r.finalAmount - expectedFinal)
                    < 0.01);

        } catch (Exception e) {
            check("Final amount calculation", false);
        }


        // Final result
        System.out.println(
                "\n==========================================");

        System.out.println("Tests Passed : " + passed);
        System.out.println("Tests Failed : " + failed);
        System.out.println("Total Tests  : " + (passed + failed));

        if (failed == 0) {
            System.out.println(
                    "QA RESULT    : ALL TESTS PASSED");
        } else {
            System.out.println(
                    "QA RESULT    : SOME TESTS FAILED");

            // Makes Jenkins pipeline fail
            System.exit(1);
        }

        System.out.println(
                "==========================================");
    }
}