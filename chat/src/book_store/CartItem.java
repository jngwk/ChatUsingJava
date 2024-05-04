package book_store;

import java.util.ArrayList;
import java.util.HashMap;

public class CartItem extends Book{
    private HashMap<Book, Integer> cartItems = new HashMap<>();
    private Book book;
    private int count;
    private int totalPrice;
    
    public CartItem(Book book) {
        this.book = book;
        if(cartItems.containsKey(book)){
            count = cartItems.get(book) + 1;
        }
        else{
            count = 1;
        }
        cartItems.put(book, count);
        this.totalPrice = book.getUnitPrice()*count;
    }

    
}
