package book_store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Book {
    public static ArrayList<Book> books = new ArrayList<>();
    private String bookId;
    private String title;
    private int unitPrice;
    private String author;
    private String desc;
    private String category;
    private String releaseDate;
    
    public Book(){}
    public Book(String bookId, String title, int unitPrice, String author, String desc, String category, String releaseDate) {
        this.bookId = bookId;
        this.title = title;
        this.unitPrice = unitPrice;
        this.author = author;
        this.desc = desc;
        this.category = category;
        this.releaseDate = releaseDate;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static void addBook(Book book){

    }

    public static void loadBooks(){
        books.removeAll(books);
        try {
            File file = new File("G:\\내 드라이브\\Coding\\Language\\Java\\Project_resources\\Bookstore\\books.txt");
            BufferedReader fReader = new BufferedReader(new FileReader(file)); 
            // read file into book arr
            String line;
            while((line = fReader.readLine()) != null){
                books.add(new Book(line, fReader.readLine(), Integer.parseInt(fReader.readLine()), fReader.readLine(), fReader.readLine(), fReader.readLine(), fReader.readLine()));
            }
            fReader.close();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
