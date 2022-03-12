import java.io.File;
import java.sql.Array;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

class Bookmark{
    //necessary
    public String url= "";
    public String createdTime= "";

    //unnecessary
    private String bookMarkName = "";
    private String groupName= "";
    private String memo= "";

    //validity check
    String timeValidPattern = "^2\\d\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$"; //checks the formats of created time
    String urlValidPattern = "^http://\\w*$";


    //constructor

    public Bookmark(String url){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm");
        if(url.matches(urlValidPattern)){
            this.url = url;
            this.createdTime = now.format(formatter);
        }else{
            System.out.println("Invalid Input");
        }

    }

    //method
    public void print(){
        System.out.println(
                (this.bookMarkName.isEmpty() ? "" : (this.bookMarkName + ", ")) +
                this.createdTime+ ", " + this.url +
                (this.groupName.isEmpty() ? "" : (", " + this.groupName)) +
                (this.memo.isEmpty() ? "" : ", " + this.memo));
    }
    public void modifyInfo(){}
    public void addInfo(){}
    public void deleteInfo(){}



}
class BookmarkList{
    Bookmark[] bookmarks;

    //method
    //public int numBookmarks(){};
    //public Bookmark getBookmark(int i){};
    public void mergeByGroup(){};

   // public BookmarkList createBookmarkByFile(){};

    public void bookmarkToFile(){};

    //constructor
    public BookmarkList(String bookmarkFileName){}

}

class FileReader{
    void lineParser(String str){
        ArrayList<String> pstr = new ArrayList<String>();
        StringTokenizer st1 = new StringTokenizer(str, ",;");
        while(st1.hasMoreTokens()){
            pstr.add(st1.nextToken());
        }

    }

}


class Test{
    public static void main(String[] args) {
        Bookmark bookmark = new Bookmark("http://asdf");
        bookmark.print();

        File file = new File("af.txt");
        FileReader filereader = new FileReader();

        try{
            Scanner scanner = new Scanner(file);

            while (scanner.hasNext()){
                String str = scanner.nextLine();
                filereader.lineParser(str);
            }
        } catch (java.io.FileNotFoundException e){
            System.out.println("file not found");
        }




        //String pattern test

    }
}