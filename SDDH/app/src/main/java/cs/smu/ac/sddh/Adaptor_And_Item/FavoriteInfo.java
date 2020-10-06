package cs.smu.ac.sddh.Adaptor_And_Item;

public class FavoriteInfo {
    int bookImage;
    String author;
    String bookTitle;
    String publishingCompany;
    String publishingDate;
    FavoriteInfo(int favBookImg,String author,String bookTitle,String publishingCompany,
                 String publishingDate){
        this.bookImage=favBookImg;
        this.author=author;
        this.bookTitle=bookTitle;
        this.publishingCompany=publishingCompany;
        this.publishingDate=publishingDate;
    }

    public int getBookImage() {
        return bookImage;
    }

    public void setBookImage(int bookImage) {
        this.bookImage = bookImage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getPublishingCompany() {
        return publishingCompany;
    }

    public void setPublishingCompany(String publishingCompany) {
        this.publishingCompany = publishingCompany;
    }

    public String getPublishingDate() {
        return publishingDate;
    }

    public void setPublishingDate(String publishingDate) {
        this.publishingDate = publishingDate;
    }
}
