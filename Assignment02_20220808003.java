/**@author Ozan Ege Çalışır
@since 15.04.2023 **/
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
public class Assignment02_20220808003 {
    public static void main(String[] args) {
        Store s = new Store ("Migros", "www.migros.com.tr");
        Customer c = new Customer ("CSE 102");
        ClubCustomer cc = new ClubCustomer ("Club CSE 102", "05551234567");
        //s.addCustomer (c); // compiler error because c is Customer not ClubCustomer
        s.addCustomer(cc);
        Product p = new Product (123456L, "Computer", 20, 1000.00);
        FoodProduct fp = new FoodProduct (456798L, "Snickers", 100, 2, 250, true,
                true, true, false);
        CleaningProduct cp = new CleaningProduct (31654L, "Mop", 28, 99,
                false, "Multi-room");
        s.addProduct(p);
        s.addProduct(fp);

        s.addProduct(cp);
        System.out.println(s.getInventorySize());

        //System.out.println(s.getProduct("shoes")); // Exception due to product not being in store

        System.out.println(cp.purchase(2));

        s.getProduct("Computer").addToInventory(3);
        //System.out.println(fp.purchase (200)); // results in Exception
        c.addToCart(p, 2);
        c.addToCart(s.getProduct("snickers"), -2); // NOTE: This does not stop the program because the Exception is caught
        c.addToCart(s.getProduct("snickers"), 1);
        System.out.println("Total due - " + c.getTotalDue());
        System.out.println("\n\nReceipt:\n" + c.receipt());
//System.out.println("After paying:" + c.pay (1000)); // results in Exception " );
        System.out.println("After paying: "+ c.pay(2020));
        System.out.println("Total due "+c.getTotalDue());
        System.out.println("\n\nReceipt 1:\n" + c.receipt());
        //Customer c2 = s.getCustomer("05551234568"); // Exception
        cc.addToCart(s.getProduct("snickers"), 2);
        cc.addToCart(s.getProduct("snickers"), 1);
        System.out.println("\n\nReceipt 2:\n" + cc.receipt());
        Customer c3 = s.getCustomer("05551234567");
        c3.addToCart(s.getProduct("snickers"), 10);
        System.out.println("\n\nReceipt 3:\n" + cc.receipt());
        System.out.println(((ClubCustomer) c3). pay (26, false));
        c3.addToCart(s.getProduct (31654L), 3);
        System.out.println(c3.getTotalDue());
        System.out.println(c3. receipt());
        System.out.println(cc.pay (3*99, false));
        c3.addToCart(s.getProduct (31654L), 3);
        System.out.println(c3.getTotalDue());
        System.out.println(c3. receipt());
        System.out.println(cc.pay(3*99, true));
        }
    }
class Product {
    Long id;
    String name;
    int quantity;
    double price;

    public Product(Long id, String name, int quantity, double price)throws InvalidPriceException
    ,InvalidAmountException{
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price)throws InvalidPriceException {

        this.price = price;
            throw new InvalidPriceException(price);

    }

    public int remaining() {

        return quantity;
    }

    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    public int getQuantity() {

        return quantity;
    }

    public int addToInventory(int amount) throws InvalidAmountException {
        if (amount >= 0) {
            quantity += amount;
            return quantity;
        } else {
            throw new InvalidAmountException(amount);
        }
    }

    public double purchase(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException(amount);
        }
        else if (amount > quantity){
            throw new InvalidAmountException(amount,quantity);
        }
        else {
            quantity -= amount;
            double total = amount * price;
            return total;
        }
    }

    public String toString() {

        return ("Product " + name + " has " + quantity + " remaining");
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        Product other = (Product) o;
        return Math.abs(price - other.getPrice()) < 0.001;
    }
}

class FoodProduct extends Product {
    int calories;
    boolean dairy;
    boolean eggs;
    boolean peanuts;
    boolean gluten;

    public FoodProduct(Long id, String name, int quantity, double price, int calories,
                       boolean dairy, boolean eggs, boolean peanuts, boolean gluten)throws InvalidAmountException{
        super(id, name, quantity, price);
        this.calories = calories;
        this.dairy = dairy;
        this.eggs = eggs;
        this.peanuts = peanuts;
        this.gluten = gluten;
    }

    public int getCalories() {

        return calories;
    }

    public void setCalories(int calories)throws InvalidAmountException{
        this.calories = calories;
        if(calories<0){
            throw new InvalidAmountException(calories);
        }
    }

    public boolean containsDairy() {

        return dairy;
    }

    public boolean containsEggs() {

        return eggs;
    }

    public boolean containsPeanuts() {

        return peanuts;
    }

    public boolean containsGluten() {

        return gluten;
    }
}

class CleaningProduct extends Product {
    private boolean liquid;
    private String whereToUse;

    public CleaningProduct(Long id, String name, int quantity,
                           double price, boolean liquid, String whereToUse) {
        super(id, name, quantity, price);
        this.whereToUse = whereToUse;
        this.liquid = liquid;
    }

    public String getWhereToUse() {

        return whereToUse;
    }

    public void setWhereToUse(String whereToUse) {

        this.whereToUse = whereToUse;
    }

    public boolean isLiquid() {

        return liquid;
    }
}

class Customer {
    private String name;
    protected List<String> cart;
    protected double total;


    public Customer(String name) {
        this.name = name;
        this.cart = new ArrayList<>();
        this.total = 0.0;
    }

    public void addToCart(Product product, int count)throws InvalidAmountException{
        try{
            product.purchase(count);
            double totalCost = product.getPrice() * count;
            cart.add(product.getName() + " - "+ product.getPrice() + " X " + count + " = " + totalCost);
            total += totalCost;
        }catch (InvalidAmountException e){
            System.out.print("ERROR: " + e + "\n");

        }
    }

    public String receipt() {
        String receipt = "";
        for (int i = 0; i < cart.size(); i++) {
            String item = cart.get(i);
            receipt += item + "\n";
        }
        receipt += "Total Due - {" + total + "}";
        return receipt;
    }

    public double getTotalDue(){
        return total;
    }

    public double pay(double amount){
        if(amount >= total) {
            double temp = total;
            System.out.println("Thank you");
            cart.clear();
            total = 0.0;
            return amount - temp;
        }else{
            throw new InsufficientFundsException(total, amount);
        }
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {

        return name;
    }
}

class ClubCustomer extends Customer {
    private String phone;
    private int points;

    public ClubCustomer(String name, String phone) {
        super(name);
        this.phone = phone;
        this.points = 0;
    }

    public String getPhone() {

        return phone;
    }

    public void setPhone(String phone) {

        this.phone = phone;
    }

    public int getPoints() {

        return points;
    }

    public void addPoints(int points) {
        if (points > 0) {
            this.points += points;
        }
    }

    public String toString() {

        return (super.getName() + " has " + points + " points");
    }

    public double pay(double amount, boolean usePoints) throws InsufficientFundsException {
        if (usePoints) {
            double discount = points * 0.01;
            if (amount >= getTotalDue() - discount) {
                if (getTotalDue() - discount < 0) {
                    points -= amount * 100;
                    cart.clear();
                    total = 0;
                    return amount;
                } else {
                    points = 0;
                    cart.clear();
                    double temp = total - discount;
                    total = 0;
                    points += temp;
                    return amount - temp;
                }
            } else {
                throw new InsufficientFundsException(total, amount);
            }
        }
        points += amount;
        return super.pay(amount);
    }
}

class Store {
    private String name;
    private String website;
    private List<Product> products;
    private List<ClubCustomer> customers;

    public Store(String name, String website) {
        this.name = name;
        this.website = website;
        this.products = new ArrayList<Product>();
        this.customers = new ArrayList<ClubCustomer>();
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getWebsite() {

        return website;
    }

    public void setWebsite(String website) {

        this.website = website;
    }

    public int getInventorySize() {

        return products.size();
    }

    public void addProduct(Product product, int index) {
        if (index < 0 || index > products.size()) {
            products.add(product);
        } else {
            products.add(index, product);
        }
    }

    public void addProduct(Product product) {

        products.add(product);
    }

    public Product getProduct(Long ID)throws ProductNotFoundException{
        for (Product product : products ){
            if(product.getId().equals(ID)){
                return product;
            }
        }
        throw new ProductNotFoundException(ID);
    }

    public Product getProduct(String name)throws ProductNotFoundException{
        for (Product product : products ){
            if(product.getName().equalsIgnoreCase(name)){
                return product;
            }
        }
        throw new ProductNotFoundException(name);
    }

    public Product getProduct(int index) {
        if (index >= 0 && index < products.size()) {
            return products.get(index);
        } else {
            return null;
        }
    }

    public int getProductIndex(Product p) {

        return products.indexOf(p);
    }
    public void addCustomer(ClubCustomer customer) {
        customers.add(customer);
    }
    public ClubCustomer getCustomer(String phone) throws CustomerNotFoundException {
        for (ClubCustomer customer : customers) {
            if (customer.getPhone().equals(phone)) {
                return customer;
            }
        }
        throw new CustomerNotFoundException(phone);
    }
    public void removeProduct(Long ID)throws ProductNotFoundException{
        boolean deleted = false;
        for (Product product : products ){
            if(product.getId() == ID){
                products.remove(product);
                deleted = true;
                break;
            }
        }
        if(!deleted){
        throw new ProductNotFoundException(ID);
        }
    }

    public void removeProduct(String name)throws ProductNotFoundException{
        boolean deleted = false;
        for (Product product : products ){
            if(product.getName().equals(name)){
                products.remove(product);
                deleted = true;
                break;
            }
        }
        if(!deleted){
            throw new ProductNotFoundException(name);
        }
    }

    public void removeCustomer(String phone)throws CustomerNotFoundException{
        boolean deleted = false;
        for (ClubCustomer customer : customers) {
            if (customer.getPhone().equals(phone)) {
                customers.remove(customer);
                deleted = true;
                break;
            }
        }
        if(!deleted){
            throw new CustomerNotFoundException(phone);
        }
    }
}

class CustomerNotFoundException extends IllegalArgumentException{
    private String phone;
    public CustomerNotFoundException(String phone){
        this.phone = phone;
    }
    public String toString(){
        return ("CustomerNotFoundException: " + phone);
    }
}

class InsufficientFundsException extends RuntimeException{
    private double total;
    private double payment;
    public InsufficientFundsException(double total, double payment){
        this.total = total;
        this.payment = payment;
    }
    public String toString(){
        return ("InsufficientFundsException: " + total + " due, but only "
        + payment + " given");
    }
}

class InvalidAmountException extends RuntimeException{
    private int amount;
    private int quantity;
    public InvalidAmountException(int amount){
        this.amount = amount;
    }
    public InvalidAmountException(int amount, int quantity){
        this.amount = amount;
        this.quantity = quantity;
    }
    public String toString(){
        if(quantity <= 0) {
            return ("InvalidAmountException: " + amount);
        }else {
            return ("InvalidAmountException: " + amount + " was requested, but only " +
                    quantity + " remaining");
        }
    }
}

class InvalidPriceException extends RuntimeException{
    private double price;
    public InvalidPriceException(double price){
        this.price = price;
    }
    public String toString(){
        return("InvalidPriceException: " + price);
    }
}

class ProductNotFoundException extends IllegalArgumentException{
    private Long ID;
    private String name;
    public ProductNotFoundException(Long ID){
        this.ID = ID;
        this.name = null;
    }
    public ProductNotFoundException(String name){
        this.name = name;
        this.ID = 0L;
    }
    public String toString(){
        if(name==null){
            return ("ProductNotFoundException: ID -" + ID);
        }else{
            return ("ProductNotFoundException: Name -" + name);
        }
    }
}