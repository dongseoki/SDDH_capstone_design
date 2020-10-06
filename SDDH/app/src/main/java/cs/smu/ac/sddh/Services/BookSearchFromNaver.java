package cs.smu.ac.sddh.Services;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import javax.xml.namespace.QName;

/*
 * Usage : Thread 를 이용하여 구현해야함
 * EX)
 * btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                EditText searchText = (EditText) findViewById(R.id.editText);
                String searchValue = searchText.getText().toString();

                final BookSearchFromNaver naver = new BookSearchFromNaver(searchValue);
                new Thread(){
                    public void run(){
                        try{
                            naver.connect();
                            runOnUiThread(new Runnable(){
                                public void run(){
                                    BookSearchFromNaver.Book book[] = naver.getBookInstance();
                                    TextView show = (TextView) findViewById(R.id.textView);
                                    show.setText(book[0].toString());
                                }
                            });
                        }
                        catch(Exception e){

                        }
                    }
                }.start();

            }
        });
 */


public class BookSearchFromNaver {
    public static class Book implements Parcelable {
        public String title = "";
        public String link = "";
        public String image = "";
        public String author = "";
        public int price = 0;
        public int discount = 0;
        public String publisher = "";
        public String ISBN = "";
        public String description = "";
        public String pubdate = "";
        //public String getTitle(){return title;}
        //public void setTitle(String title){this.title = title;}

        public Book() {
        }
        public Book(String title, String link, String image, String author, int price, int discount, String publisher, String ISBN, String description, String pubdate) {
            this.title = title;
            this.link = link;
            this.image = image;
            this.author = author;
            this.price = price;
            this.discount = discount;
            this.publisher = publisher;
            this.ISBN = ISBN;
            this.description = description;
            this.pubdate = pubdate;
        }

        protected Book(Parcel in) {
            title = in.readString();
            link = in.readString();
            image = in.readString();
            author = in.readString();
            price = in.readInt();
            discount = in.readInt();
            publisher = in.readString();
            ISBN = in.readString();
            description = in.readString();
            pubdate = in.readString();
        }

        public static final Creator<Book> CREATOR = new Creator<Book>() {
            @Override
            public Book createFromParcel(Parcel in) {
                return new Book(in);
            }

            @Override
            public Book[] newArray(int size) {
                return new Book[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeString(link);
            dest.writeString(image);
            dest.writeString(author);
            dest.writeInt(price);
            dest.writeInt(discount);
            dest.writeString(publisher);
            dest.writeString(ISBN);
            dest.writeString(description);
            dest.writeString(pubdate);
        }

        public void readFromParcel(Parcel in) {
            title = in.readString();
            link = in.readString();
            image = in.readString();
            author = in.readString();
            price = in.readInt();
            discount = in.readInt();
            publisher = in.readString();
            ISBN = in.readString();
            description = in.readString();
            pubdate = in.readString();
        }
    }


    int connectionState = 0;
    ArrayList<Book> book = new ArrayList<Book>(20);
    Book tempBook = new Book();
    int bookIndex = 0;
    String searchValue;
    String clientID = "---------------------------";
    String clientPW = "---------------------------";
    StringBuffer sb = new StringBuffer();

    //BookSearchFromNaver(){}
    public BookSearchFromNaver(String str) {
        searchValue = str;
    }

    public BookSearchFromNaver() {
    }

    void insertsearchValue(String str) {
        searchValue = str;
    }

    public ArrayList<Book> getBookInstance() {
        return book;
    }

    public void connect() {
        try {

            /*
                AsyncTask<String, Void, HttpResponse> asyncTask = new AsyncTask<String, Void, HttpResponse>() {
                    @Override
                    protected HttpResponse doInBackground(String... url) {
                        HttpGet request = new HttpGet(url[0]);
                        HttpResponse response = null;
                        try {
                            response = new DefaultHttpClient().execute(request);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return response;
                    }
                };

                 */

            String text = URLEncoder.encode(searchValue);
            String apiURL = "https://openapi.naver.com/v1/search/book.xml?query=" + text + "&display=20" + "&start=1";
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientPW);

            int responseCode;
            try {
                responseCode = conn.getResponseCode();  //http 상태코드
                switch (responseCode) {
                    case 200:   //Query OK
                        break;
                    case 400:   //Invalid display value
                        connectionState = 400;
                        break;
                    case 404:   //Invalid search api
                        connectionState = 404;
                        break;
                    case 500:   //System Error
                        connectionState = 500;
                        break;
                    default:
                        connectionState = -1;
                        break;
                }
            } catch (IOException e) {
                //IF Cannot Connect To Server, then Throw IOException
            }

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            String tag;
            //inputStream으로부터 xml값 받기
            parser.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            parser.next();
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {    //2:Open  3:Close  4:내용
                    case XmlPullParser.START_TAG:
                        tag = parser.getName(); //태그 이름 얻어오기

                        switch (tag) {    //자세한 Response는 맨 하단 주석 참조
                            case "lastBuildDate":   //검색 결과를 생성한 시간
                                //parser.next();
                                //저장할거면 여기서 변수 저장
                                //parser.next(); // Type 3으로 바로 넘겨서 닫기
                                break;
                            case "total":  //검색 결과 문서의 총 개수를 의미한다.
                                break;
                            case "start":   //검색 결과 문서 중, 문서의 시작점을 의미한다.
                                break;
                            case "display": //검색된 검색 결과의 개수이다.
                                break;
                            case "item":    //XML 포멧에서 item 태그로, JSON 포멧에서는 items 속성으로 표현되며, 개별 검색 결과이며 title, link, description 을 포함한다.
                                parser.next();
                                while (true) {
                                    String tag2 = parser.getName();
                                    eventType = parser.getEventType();
                                    if (tag2.equals("item") && eventType == 3) {
                                        bookIndex++;
                                        book.add(tempBook);
                                        tempBook = new Book();
                                        break;
                                    }
                                    if (eventType == 3 || eventType == 4) {
                                        parser.next();
                                        continue;
                                    }
                                    switch (tag2) {
                                        case "title":   //검색 결과 문서의 제목을 나타낸다. 제목에서 검색어와 일치하는 부분은 태그로 감싸져 있다.
                                            parser.next();
                                            //tempBook.title = parser.getText();
                                            tempBook.title = parser.getText().replaceAll("<b>", "");
                                            tempBook.title = tempBook.title.replaceAll("</b>", "");
                                            break;
                                        case "link":    //검색 결과 문서의 하이퍼텍스트 link를 나타낸다.
                                            parser.next();
                                            tempBook.link = parser.getText();
                                            break;
                                        case "image":   //썸네일 이미지의 URL이다. 이미지가 있는 경우만 나타납니다.
                                            parser.next();
                                            //book[bookIndex].image = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                            tempBook.image = parser.getText();
                                            break;
                                        case "author":  //저자정보이다.
                                            parser.next();
                                            tempBook.author = parser.getText().replaceAll("<b>", "");
                                            tempBook.author = tempBook.author.replaceAll("</b>", "");
                                            //tempBook.author = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                            break;
                                        case "price":   //정가 정보이다. 절판 도서 등으로 가격이 없으면 나타나지 않는다.
                                            parser.next();
                                            try {
                                                tempBook.price = Integer.parseInt(parser.getText());
                                            } catch (Exception e) {
                                                tempBook.price = -1;
                                            }
                                            //tempBook.price = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                            break;
                                        case "discount":    //할인 가격 정보이다. 절판 도서 등으로 가격이 없으면 나타나지 않는다.
                                            parser.next();
                                            try {
                                                tempBook.discount = Integer.parseInt(parser.getText());
                                            } catch (Exception e) {
                                                tempBook.discount = -1;
                                            }
                                            //tempBook.discount = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                            break;
                                        case "publisher":   //출판사 정보이다.
                                            parser.next();
                                            tempBook.publisher = parser.getText();
                                            //tempBook.publisher = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                            break;
                                        case "isbn":    //ISBN 넘버이다.
                                            parser.next();
                                            String isss = parser.getText();
                                            String isbn1 = isss.split(" ")[0];
                                            String isbn2 = isss.split(" ")[1];
                                            if (isbn1.charAt(0) == '9') tempBook.ISBN = isbn1;
                                            else if (isbn2.charAt(0) == '9') tempBook.ISBN = isbn2;
                                            //tempBook.ISBN = parser.getText();
                                            //tempBook.ISBN = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                            break;
                                        case "description": //검색 결과 문서의 내용을 요약한 패키지 정보이다. 문서 전체의 내용은 link를 따라가면 읽을 수 있다.
                                            parser.next();
                                            tempBook.description = parser.getText();
                                            //tempBook.description = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                            break;
                                        case "pubdate": //출간일 정보이다.
                                            parser.next();
                                            tempBook.pubdate = parser.getText();
                                            //tempBook.pubdate = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                            break;
                                    }
                                    parser.next();
                                }
                                break;
                            default:
                                break;
                        }
                        break;  //switch(eventType) case XmlPullParser.START_TAG
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
        }
    }

    //ISBN 으로 상세검색
    public Book searchFromISBN(String ISBN) {
        String text = URLEncoder.encode(ISBN);
        final Book[] ret = {new Book()};
        try {
            String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?d_isbn=" + text;
            URL url = new URL(apiURL);

            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("X-Naver-Client-Id", clientID);
            conn.setRequestProperty("X-Naver-Client-Secret", clientPW);

            final int[] responseCode = new int[1];
            try {
                responseCode[0] = conn.getResponseCode();  //http 상태코드
                switch (responseCode[0]) {
                    case 200:   //Query OK
                        break;
                    case 400:   //Invalid display value
                        connectionState = 400;
                        break;
                    case 404:   //Invalid search api
                        connectionState = 404;
                        break;
                    case 500:   //System Error
                        connectionState = 500;
                        break;
                    default:
                        connectionState = -1;
                        break;
                }

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                String tag;
                //inputStream으로부터 xml값 받기
                parser.setInput(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                parser.next();
                int eventType = parser.getEventType();
                tempBook = new Book();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {    //2: Open 3 : Close 4: 내용 인듯하다
                        case XmlPullParser.START_TAG:
                            tag = parser.getName(); //태그 이름 얻어오기

                            switch (tag) {    //자세한 Response는 맨 하단 주석 참조
                                case "lastBuildDate":   //검색 결과를 생성한 시간
                                    //parser.next();
                                    //저장할거면 여기서 변수 저장
                                    //parser.next(); // Type 3으로 바로 넘겨서 닫기
                                    break;
                                case "total":  //검색 결과 문서의 총 개수를 의미한다.
                                    break;
                                case "start":   //검색 결과 문서 중, 문서의 시작점을 의미한다.
                                    break;
                                case "display": //검색된 검색 결과의 개수이다.
                                    break;
                                case "item":    //XML 포멧에서 item 태그로, JSON 포멧에서는 items 속성으로 표현되며, 개별 검색 결과이며 title, link, description 을 포함한다.
                                    parser.next();
                                    while (true) {
                                        String tag2 = parser.getName();
                                        eventType = parser.getEventType();
                                        if (tag2.equals("item") && eventType == 3) {
                                            ret[0] = tempBook;
                                            return ret[0];
                                        }
                                        if (eventType == 3 || eventType == 4) {
                                            parser.next();
                                            continue;
                                        }
                                        switch (tag2) {
                                            case "title":   //검색 결과 문서의 제목을 나타낸다. 제목에서 검색어와 일치하는 부분은 태그로 감싸져 있다.
                                                parser.next();
                                                //tempBook.title = parser.getText();
                                                tempBook.title = parser.getText().replaceAll("<b>", "");
                                                tempBook.title = tempBook.title.replaceAll("</b>", "");
                                                break;
                                            case "link":    //검색 결과 문서의 하이퍼텍스트 link를 나타낸다.
                                                parser.next();
                                                tempBook.link = parser.getText();
                                                break;
                                            case "image":   //썸네일 이미지의 URL이다. 이미지가 있는 경우만 나타납니다.
                                                parser.next();
                                                //book[bookIndex].image = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                                tempBook.image = parser.getText();
                                                break;
                                            case "author":  //저자정보이다.
                                                parser.next();
                                                tempBook.author = parser.getText();
                                                //tempBook.author = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                                break;
                                            case "price":   //정가 정보이다. 절판 도서 등으로 가격이 없으면 나타나지 않는다.
                                                parser.next();
                                                try {
                                                    tempBook.price = Integer.parseInt(parser.getText());
                                                } catch (Exception e) {
                                                    tempBook.price = -1;
                                                }
                                                //tempBook.price = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                                break;
                                            case "discount":    //할인 가격 정보이다. 절판 도서 등으로 가격이 없으면 나타나지 않는다.
                                                parser.next();
                                                try {
                                                    tempBook.discount = Integer.parseInt(parser.getText());
                                                } catch (Exception e) {
                                                    tempBook.discount = -1;
                                                }
                                                //tempBook.discount = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                                break;
                                            case "publisher":   //출판사 정보이다.
                                                parser.next();
                                                tempBook.publisher = parser.getText();
                                                //tempBook.publisher = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                                break;
                                            case "isbn":    //ISBN 넘버이다.
                                                parser.next();
                                                ///
                                                String isss = parser.getText();
                                                String isbn1 = isss.split(" ")[0];
                                                String isbn2 = isss.split(" ")[1];
                                                if (isbn1.charAt(0) == '9') tempBook.ISBN = isbn1;
                                                else if (isbn2.charAt(0) == '9') tempBook.ISBN = isbn2;
                                                ///
                                                // tempBook.ISBN = parser.getText();
                                                //tempBook.ISBN = Integer.parseInt(parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", ""));
                                                break;
                                            case "description": //검색 결과 문서의 내용을 요약한 패키지 정보이다. 문서 전체의 내용은 link를 따라가면 읽을 수 있다.
                                                parser.next();
                                                tempBook.description = parser.getText();
                                                //tempBook.description = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                                break;
                                            case "pubdate": //출간일 정보이다.
                                                parser.next();
                                                tempBook.pubdate = parser.getText();
                                                //tempBook.pubdate = parser.getText().replaceAll("<(/)?([a-zA-Z]*)(\\\\s[a-zA-Z]*=[^>]*)?(\\\\s)*(/)?>", "");
                                                break;
                                        }
                                        parser.next();
                                    }
                                default:
                                    break;
                            }
                            break;  //switch(eventType) case XmlPullParser.START_TAG
                    }
                    eventType = parser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                //IF Cannot Connect To Server, then Throw IOException
            }

        } catch (Exception e) {
            Book bs = new Book();
        }
        return ret[0];
    }
}