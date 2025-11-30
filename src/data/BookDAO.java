package data;

import models.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class BookDAO {
    private static final String FILE_PATH = "books.txt";
    
    public List<Book> getAllBooks() {
        List<Book> bookList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String bookID = data[0];
                    String genre = data[1];
                    String title = data[2];
                    String author = data[3];
                    int publicationYear = Integer.parseInt(data[4]);
                    boolean isAvailable = Boolean.parseBoolean(data[5]);

                    Book book = new Book(bookID, genre, title, author, publicationYear , isAvailable);
                    bookList.add(book);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading book data: " + e.getMessage());
        }

        return bookList;
    }

    public static String generateBookID() {
        List<Book> books = new BookDAO().getAllBooks();
        int maxNumber = 0;
        for (Book book : books) {
            String id = book.getBookID();
            if (id.startsWith("B")) {
                try {
                    int number = Integer.parseInt(id.substring(1));
                    if (number > maxNumber) {
                        maxNumber = number;
                    }
                } catch (NumberFormatException ignored) { }
            }
        }
        return String.format("B%04d", maxNumber + 1);
    }

    public String getBookTitleByID(String bookID){
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] data = line.split(",");
                String storedBookID = data[0];
                if (storedBookID.equals(bookID)){
                    return data[2];
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return "Unknown Title";
    }

    public boolean isBookAvailable(String bookID){
        try (BufferedReader reader = new BufferedReader(new FileReader("books.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] data = line.split(",");
                String storedBookID = data[0];
                if (storedBookID.equals(bookID)){
                    boolean isAvailable = Boolean.parseBoolean(data[data.length - 1].trim());
                    return isAvailable;
                }
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public void addBook(String genre, String title, String author, int year) {
        String bookID = BookDAO.generateBookID();

        Book newBook = new Book(bookID, genre, title, author, year , true);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(newBook.getBookID() + "," + newBook.getGenre() + "," + newBook.getTitle() + "," + newBook.getAuthor() + "," + newBook.getPublicationYear() + "," + newBook.getStatus());
            writer.newLine();
            JOptionPane.showMessageDialog(null, "Book Added Successfully!", "Info", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error Saving Book", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void removeOldBooks(int year) {
        ArrayList<String> updatedBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                if (Integer.parseInt(bookDetails[4]) >= year) {
                    updatedBooks.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String book : updatedBooks) {
                writer.write(book);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving books: " + e.getMessage());
        }
    }

    public void updateBookStatus(String bookID, boolean isAvailable) {
        File inputFile = new File("books.txt");
        File tempFile = new File("temp_books.txt");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            while ((line = reader.readLine()) != null){
                String[] data = line.split(",");
                String storedBookID = data[0];
                if (storedBookID.equals(bookID)) {
                    data[data.length - 1] = String.valueOf(isAvailable);
                    writer.write(String.join(",", data));
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp_books.txt to books.txt");
            }
        } else {
            System.err.println("Failed to delete books.txt");
        }
    }
    
    public static ArrayList<Book> searchBooks(String genre, String title, String author, String year) {
        ArrayList<Book> matchingBooks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] bookDetails = line.split(",");
                String bookGenre = bookDetails[1];
                String bookTitle = bookDetails[2];
                String bookAuthor = bookDetails[3];
                String bookYear = bookDetails[4];
                boolean status = Boolean.parseBoolean(bookDetails[5]);
                
                if(status == true) {
                    
                    boolean matches = true;
                if (!genre.isEmpty() && !bookGenre.equalsIgnoreCase(genre)) {
                    matches = false;
                }
                if (!title.isEmpty() && !bookTitle.equalsIgnoreCase(title)) {
                    matches = false;
                }
                if (!author.isEmpty() && !bookAuthor.equalsIgnoreCase(author)) {
                    matches = false;
                }
                if (!year.isEmpty() && !bookYear.equals(year)) {
                    matches = false;
                }

                if (matches) {
                    Book book = new Book(bookDetails[0], bookGenre, bookTitle, bookAuthor, Integer.parseInt(bookYear) , Boolean.parseBoolean(bookDetails[5]));
                    matchingBooks.add(book);
                }
            }
                
            }
        } catch (IOException e) {
            System.out.println("Error reading books: " + e.getMessage());
        }
        return matchingBooks;
    }

    public void updateBookDetails(String bookID, String newGenre, String newTitle, String newAuthor, int newYear) {
        File inputFile = new File(FILE_PATH);
        File tempFile = new File("temp_books.txt");
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            boolean found = false;
    
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String storedBookID = data[0];
    
                if (storedBookID.equals(bookID)) {
                    String updatedLine = bookID + "," + newGenre + "," + newTitle + "," + newAuthor + "," + newYear + "," + data[5];
                    writer.write(updatedLine);
                    found = true;
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
    
            if (!found) {
                JOptionPane.showMessageDialog(null, "Book ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
    
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        if (inputFile.delete()) {
            if (!tempFile.renameTo(inputFile)) {
                System.err.println("Failed to rename temp file.");
            }
        } else {
            System.err.println("Failed to delete original file.");
        }
    }

}
