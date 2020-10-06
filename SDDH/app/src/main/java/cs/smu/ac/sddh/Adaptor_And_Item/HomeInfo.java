package cs.smu.ac.sddh.Adaptor_And_Item;

public class HomeInfo {
    int univImage;
    String univName;
    public HomeInfo(int univImage, String univName){
        this.univImage=univImage;
        this.univName=univName;
    }

    public int getUnivImage() {
        return univImage;
    }

    public void setUnivImage(int univImage) {
        this.univImage = univImage;
    }

    public String getUnivName() {
        return univName;
    }

    public void setUnivName(String univName) {
        this.univName = univName;
    }
}
