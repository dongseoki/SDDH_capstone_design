package cs.smu.ac.sddh.Enum;

public enum ESchoolID {
    gwangwoon, kookmin, daejin, duksung, dongduk, myongji, sahmyook, sangmyung, seokyeong, seoulwoman, sungshin, hansung, None;
    public int convertInt(ESchoolID eSchoolID){
        int val = -1;
        switch(eSchoolID){
            case gwangwoon: val = 0; break;
            case kookmin: val = 1; break;
            case daejin: val = 2; break;
            case duksung: val = 3; break;
            case dongduk: val = 4; break;
            case myongji: val = 5; break;
            case sahmyook: val = 6; break;
            case sangmyung: val = 7; break;
            case seokyeong: val = 8; break;
            case seoulwoman: val = 9; break;
            case sungshin: val = 10; break;
            case hansung: val = 11; break;
        }
        return val;
    }
    public ESchoolID convertIntToESchoolID(int val){
        ESchoolID ret = None;
        switch(val){
            case 0: ret =  gwangwoon; break;
            case 1: ret =  kookmin; break;
            case 2: ret =  daejin; break;
            case 3: ret =  duksung; break;
            case 4: ret =  dongduk; break;
            case 5: ret =  myongji; break;
            case 6: ret =  sahmyook; break;
            case 7: ret =  sangmyung; break;
            case 8: ret =  seokyeong; break;
            case 9: ret =  seoulwoman; break;
            case 10: ret =  sungshin; break;
            case 11: ret =  hansung; break;
        }
        return ret;
    }
}
