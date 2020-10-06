package cs.smu.ac.sddh.Adaptor_And_Item;

import android.os.Parcel;
import android.os.Parcelable;

public class LoanStatus implements Parcelable {
    String title;
    String RN;// 등록번호.
    String CN; // 청구기호.
    String POS; // 소장 위치
    int STATE;
    // STATE 상태 설명. -1 : 오류, 0 : 대출중 , 1 : 이용가능 , 2: 지정도서.
    public String RDD; // 반납 예정일. EX) 2020-08-30
    int BN; // 예약자수.
    String errorMessage; // 에러메세지.

    LoanStatus(Parcel in){
        title = in.readString();
        RN = in.readString();
        CN = in.readString();
        POS = in.readString();
        STATE = in.readInt();
        RDD = in.readString();
        BN = in.readInt();
        errorMessage = in.readString();
    }

    public static final Creator<LoanStatus> CREATOR = new Creator<LoanStatus>() {
        @Override
        public LoanStatus createFromParcel(Parcel in) {
            return new LoanStatus(in);
        }

        @Override
        public LoanStatus[] newArray(int size) {
            return new LoanStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(RN);
        dest.writeString(CN);
        dest.writeString(POS);
        dest.writeInt(STATE);
        dest.writeString(RDD);
        dest.writeInt(BN);
        dest.writeString(errorMessage);
    }

    public String getTitle() {return this.title;}
    public String getRN() {return this.RN;}
    public String getCN() {return this.CN;}
    public String getPOS() {return this.POS;}
    public int getSTATE() {return this.STATE;}
    public String getRDD() {return this.RDD;}
    public int getBN() {return this.BN;}
    public String getErrorMessage() {return this.errorMessage;}
}
