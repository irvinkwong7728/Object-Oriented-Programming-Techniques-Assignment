package models;

import javax.swing.JOptionPane;

public class Book {
    private String bookID;
    private String genre;
    private String title;
    private String author;
    private int publicationYear;
    private boolean bookStatus;

    public Book(String bookID, String genre, String title, String author, int publicationYear , boolean bookStatus){
        this.bookID = bookID;
        this.genre = genre;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.bookStatus = bookStatus;
    }
    public String getBookID(){
        return bookID;
    }

    public void setBookID(String bookID){
        this.bookID = bookID;
    }

    public String getGenre(){
        return genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public int getPublicationYear(){
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear){
        this.publicationYear = publicationYear;
    }

    public boolean getStatus(){
        return bookStatus;
    }   

    public void setStatus(boolean bookStatus){
        this.bookStatus = bookStatus;
    }

    public static boolean validateBook(Book book){
        StringBuilder errorMessages = new StringBuilder();
        String author = book.author;
        int publicationYear = book.publicationYear;

        if (!author.matches("[a-zA-Z\\s]+")){
            errorMessages.append("Author name cannot contain numbers\n");
        }

        if (publicationYear < 1900 || publicationYear > 2026){
            errorMessages.append("Invalid Year!\n");
        }

        if(errorMessages.length() > 0){
            JOptionPane.showMessageDialog(null, errorMessages.toString());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public String toString(){
        return "Book{"+
                "bookID="+bookID+
                ", genre='"+genre+'\''+
                ", title='"+title+ '\''+
                ", author='"+author+'\''+
                ", publicationYear="+ publicationYear +
                "," + "status=" + bookStatus +'}';
            }
}
