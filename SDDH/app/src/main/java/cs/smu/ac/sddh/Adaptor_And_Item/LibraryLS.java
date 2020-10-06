package cs.smu.ac.sddh.Adaptor_And_Item;

import java.util.ArrayList;

public class LibraryLS{

    public String sid = "";
    public ArrayList<LoanStatus> loanStatusList;
    public String errorMessage;

    public LibraryLS(String sid) {
        this.sid = sid;
    }
}


// sid 설명.
//        KW_CODE =0
//        KM_CODE =1
//        DJ_CODE=2
//        DS_CODE=3
//        DD_CODE=4
//        MJ_CODE=5
//        SY_CODE=6
//        SM_CODE=7
//        SK_CODE=8
//        SW_CODE=9
//        SS_CODE=10
//        HS_CODE=11

//    sid 부여번호	학교	도서관 홈페이지
//        0	광운대학교	http://kupis.kw.ac.kr/
//        1	국민대학교	http://lib.kookmin.ac.kr/
//        2	대진대학교	http://library.daejin.ac.kr/
//        3	덕성여자대학교	http://discover.duksung.ac.kr/
//        4	동덕여자대학교	http://library.dongduk.ac.kr/
//        5	명지대학교	http://lib.mju.ac.kr/
//        6	삼육대학교	http://lib.syu.ac.kr/
//        7	상명대학교	http://lib.smu.ac.kr/
//        8	서경대학교	http://library.skuniv.ac.kr/
//        9	서울여자대학교	http://lib.swu.ac.kr/
//        10	성신여자대학교	http://lib.sungshin.ac.kr/
//        11	한성대학교	http://hsel.hansung.ac.kr/

//참고. http://www.ulcns.net/modules/doc/index.php?doc=non_01&___M_ID=37