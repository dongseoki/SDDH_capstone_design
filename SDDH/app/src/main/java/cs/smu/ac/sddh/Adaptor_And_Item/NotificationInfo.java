package cs.smu.ac.sddh.Adaptor_And_Item;

public class NotificationInfo {

    int requestCode;
    String bookTitle;
    String Univ;
    String bannap;

    public NotificationInfo(int requestCode, String bookTitle, String Univ, String bannap){
        //this.bookImage=BookImg;
        this.bookTitle=bookTitle;
        this.bannap = bannap;
        this.Univ = Univ;
        this.requestCode = requestCode;
    }
    /*
    public int getBookImage() {
        return bookImage;
    }

    public void setBookImage(int bookImage) {
        this.bookImage = bookImage;
    }
*/
    public String getTitle() {
        return bookTitle;
    }

    public void setTitle(String Title) {
        this.bookTitle = Title;
    }

    public String getdate() {
        return bannap;
    }

    public String getUniv() {
        return Univ;
    }

    public void setdate(String date) {
        this.bannap = date;
    }

    public int getRequestCode() {
        return requestCode;
    }
}
