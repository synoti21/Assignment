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
        String[] temp = str.split(";|,");
        if(lineChecker.validityCheck(temp) == 1){
            /*for(int i = 0; i < temp.length; i++){
                System.out.println("valid input : " + temp[i]); //TEST
            }
            System.out.println("\n");*/

            //make bookmark


        }else{
            System.out.println("invalid input");
        }


        //checking process


    }
}

//class that checks validity of each line by using url pattern, and time pattern
class LineChecker{
    public String timeValidPattern = "^2\\d\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$"; //checks the formats of created time
    public String urlValidPattern = "^http://\\S*$";

    class PatternChecker{
        public int urlCheck(String str){
            if(str.matches(urlValidPattern)){
                //System.out.println(str + " valid");
                return 1;
            }else{
                //System.out.println(str + " invalid");

                return 0;
            }
        }
        public int timeCheck(String str){
            if(str.matches(timeValidPattern)){
                //System.out.println(str + " valid");

                return 1;
            }else{
                //System.out.println(str + " invalid");

                return 0;
            }
        }
    }
    int validityCheck(String[] str){ // checks each line if it satisfies Bookmark condition
        PatternChecker patternChecker = new PatternChecker();
        if(patternChecker.timeCheck(str[1]) == 1){
            if(patternChecker.urlCheck(str[2]) == 1){ //if all conditions are satisfied (url, time)
                return 1; // ***TEST***

                //make Bookmark and bookmark list with groupname and memo
            }else{ //if url pattern is invalid, then it's invalid
                //System.out.println(str[1] + " invalid input , url \n");
                return 0;
            }
        }
        else{ //it could be name, so check the next token
            /*if(patternChecker.timeCheck(str[1])==1){
                if(patternChecker.urlCheck(str[2]) == 1){
                    return 1; // ***TEST***

                    //make Bookmark and Bookmark list with groupname and memo
                }else{ //invalid
                   // System.out.println(str[2] + " invalid input, url\n");
                    return 0;
                }
            }else{ //invalid
               // System.out.println(str[1] + " invalid input, time\n");
                return 0;
            }*/
            return 0;
        }
        //checking process
    }


}


class Test{
    public static void main(String[] args) {
        Bookmark bookmark = new Bookmark("http://asdf");

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