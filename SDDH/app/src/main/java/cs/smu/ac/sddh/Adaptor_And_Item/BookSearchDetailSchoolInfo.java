package cs.smu.ac.sddh.Adaptor_And_Item;

import cs.smu.ac.sddh.Enum.ESchoolID;

///책 상세정보 하단에 대한 변수들
public class BookSearchDetailSchoolInfo implements Comparable<BookSearchDetailSchoolInfo>{
    ESchoolID schoolID;
    int schoolImage;//학교로고
    int distance;//거리
    int borrowCount;//대출가능건수

    @Override
    public int compareTo(BookSearchDetailSchoolInfo o) {
        return this.distance < o.distance ? -1 : 1;
    }

    int searchCount;//검색건수
    int borrowAble; //대출가능여부
    public BookSearchDetailSchoolInfo(ESchoolID schoolID, int schoolImage, int distance, int borrowCount, int searchCount, int borrowAble){
        this.schoolID = schoolID;
        this.schoolImage=schoolImage;
        this.distance=distance;
        this.borrowCount=borrowCount;
        this.searchCount=searchCount;
        this.borrowAble = borrowAble;
    }

    public ESchoolID getSchoolID(){ return schoolID; }
    public void setSchoolID(ESchoolID schoolID){ this.schoolID = schoolID; }

    public int getSchoolImage() {
        return schoolImage;
    }

    public void setSchoolImage(int schoolImage) {
        this.schoolImage = schoolImage;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public int getSearchCount() {
        return searchCount;
    }

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public int getBorrowAble(){ return borrowAble; }
    public void setBorrowAble(int borrowAble){ this.borrowAble = borrowAble; }
}
