package cs.smu.ac.sddh.Adaptor_And_Item;

public class BookSchoolActivityListItem {
    private String bookLocation;
    private String bookInfo;
    private String bookCN;
    private String borrowAble;
    private String returnDate;
    private String bookReserve;

    public BookSchoolActivityListItem(String bookLocation, String bookInfo, String bookCN, String borrowAble, String returnDate, String bookReserve){
        this.bookLocation = bookLocation;
        this.bookInfo = bookInfo;
        this.bookCN = bookCN;
        this.borrowAble = borrowAble;
        this.returnDate = returnDate;
        this.bookReserve = bookReserve;
    }

    public String getBookLocation(){ return bookLocation; }
    public void setBookLocation(String bookLocation){ this.bookLocation = bookLocation; }

    public String getBookInfo(){ return bookInfo; }
    public void setBookInfo(String bookInfo){ this.bookInfo = bookInfo; }

    public String getBookCN(){ return bookCN; }
    public void setBookCN(String bookCN){ this.bookCN = bookCN; }

    public String getBorrowAble(){ return borrowAble; }
    public void setBorrowAble(String borrowAble){ this.borrowAble = borrowAble; }

    public String getReturnDate(){ return returnDate; }
    public void setReturnDate(String returnDate){ this.returnDate = returnDate; }

    public String getBookReserve(){ return bookReserve; }
    public void setBookReserve(String bookReserve){ this.bookReserve = bookReserve; }
}
