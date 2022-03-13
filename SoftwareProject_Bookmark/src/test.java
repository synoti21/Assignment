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
    public String timeValidPattern = "^2\\d\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$"; //checks the formats of created time
    public String urlValidPattern = "^http://\\w*$";


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

//class that reads file and makes bookmark and bookmark list
class FileReader{
    LineChecker lineChecker = new LineChecker();
    void lineParser(String str){
        String[] tempBookmark = str.split(";,");
        if(lineChecker.validityCheck(tempBookmark) == 1){
            //make bookmark
        }


        //checking process


    }
}

//class that checks validity of each line by using url pattern, and time pattern
class LineChecker{
    class PatternChecker{
        public int urlCheck(String str){
            if(str.matches()){
                return 1;
            }else{
                return 0;
            }
        }
        public int timeCheck(String str){
            if(){
                return 1;
            }else{
                return 0;
            }
        }
    }
    int validityCheck(String[] str){ // checks each line if it satisfies Bookmark condition
        PatternChecker patternChecker = new PatternChecker();
        if(patternChecker.urlCheck(str[0]) == 1){
            if(patternChecker.timeCheck(str[1]) == 1){ //if all conditions are satisfied (url, time)
                //make Bookmark and bookmark list with groupname and memo
            }else{ //if time pattern is invalid, then it's invalid
                System.out.println("invalid input");
            }
        }else{ //it could be name, so check the next token
            if(patternChecker.urlCheck(str[0])==1){
                if(patternChecker.timeCheck(str[1]) == 1){
                    //make Bookmark and Bookmark list with groupname and memo
                }else{ //invalid
                    System.out.println("invalid input");
                }
            }else{ //invalid
                System.out.println("invalid input");
            }
        }
        //checking process
    }


}


class Test{
    public static void main(String[] args) {
        Bookmark bookmark = new Bookmark("http://asdf");
        bookmark.print();

        File file = new File("/Users/jasonahn/IdeaProjects/SoftwareProject_BookMark/src/af.txt");
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