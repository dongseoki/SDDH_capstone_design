package cs.smu.ac.sddh.Services;
/*
    현재 좌표를 넣어서 생성자로 객체를 만든다.
    distances 함수를 호출하면, 하버사인 공식에 의해 현재위치에서부터 각 학교까지의 거리가 반환된다.
    setLong_Let_here 함수를 이용해 현재 좌표값을 변경할 수 있다.
 */
public class DistanceManager {
    private class lat_lon {
        double latitude;  // 위도
        double longitude; // 경도

        private lat_lon(double lat, double lon) {
            this.latitude = lat;
            this.longitude = lon;
        }

        private void setLat_Long(double lat, double lon) {this.latitude = lat; this.longitude = lon;}
        double getLatitude() { return this.latitude;}
        double getLongitude() {return this.longitude;}
    };

    lat_lon here;
    lat_lon[] univs;

    public DistanceManager(double lat, double lon) {
        here = new lat_lon(lat, lon);
        univs = new lat_lon[12];
        univs[0] = new lat_lon(37.619405, 127.059726 ); // 광운대
        univs[1] = new lat_lon(37.610985, 126.997203 ); // 국민대
        univs[2] = new lat_lon(37.873394, 127.154388 ); // 대진대
        univs[3] = new lat_lon(37.651199, 127.016158 ); // 덕성여대
        univs[4] = new lat_lon(37.606998, 127.042461 ); // 동덕여대
        univs[5] = new lat_lon(37.580720, 126.924434 ); // 명지대
        univs[6] = new lat_lon(37.643109, 127.106557 ); // 삼육대
        univs[7] = new lat_lon(37.602182, 126.955222 ); // 상명대
        univs[8] = new lat_lon(37.614867, 127.011863 ); // 서경대
        univs[9] = new lat_lon(37.628297, 127.091073 ); // 서울여대
        univs[10] = new lat_lon(37.590815, 127.021511 ); // 성신여대
        univs[11] = new lat_lon(37.582294, 127.009557 ); // 한성대
    }
    private void setLong_Lat_here(double lat, double lon) {here.setLat_Long(lat, lon);}

    private double distanceInKilometerByHaversine(double lat1, double lat2, double lon1, double lon2) { // x : lat, y:lon
        double distance;
        double radius = 6371; // 지구 반지름(km)
        double toRadian = Math.PI / 180;

        double deltaLatitude = Math.abs(lat1 - lat2) * toRadian;
        double deltaLongitude = Math.abs(lon1 - lon2) * toRadian;

        double sinDeltaLat = Math.sin(deltaLatitude / 2);
        double sinDeltaLng = Math.sin(deltaLongitude / 2);
        double squareRoot = Math.sqrt(
                sinDeltaLat * sinDeltaLat +
                        Math.cos(lat1 * toRadian) * Math.cos(lat2 * toRadian) * sinDeltaLng * sinDeltaLng);

        distance = 2 * radius * Math.asin(squareRoot);

        return distance;
    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    public double[] distances() {
        double[] dis = new double[12];
        for (int i = 0; i < 12; i++) {
            dis[i] = distanceInKilometerByHaversine(here.latitude, univs[i].getLatitude(), here.longitude, univs[i].getLongitude() );
        }
        return dis;
    }
}
