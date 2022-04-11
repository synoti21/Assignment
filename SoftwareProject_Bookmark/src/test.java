import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.net.*;

class Bookmark{
    //necessary
    public String url= "";
    public String createdTime= "";

    //unnecessary
    public String bookMarkName = "";
    public String groupName= "";
    public String memo= "";

    //validity check
    public String urlValidPattern = "^https?://\\w*$";


    //constructor
    public Bookmark(){
        this.bookMarkName = "";
        this.createdTime = "";
        this.groupName = "";
        this.memo = "";
        this.url = "";
    };

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
        try{
            System.out.println(
                    ((this.bookMarkName + ",")) +
                            this.createdTime+ "," + this.url +
                            (("," + this.groupName)) +
                            ("," + this.memo));
        }catch(Exception e){
            e.getMessage();
        }

    }
}
class BookmarkList{
    Bookmark[] bookmarks;
    File fileStream;
    int index =0;


    //method
    public int numBookmarks(){
        return index;
    };
    public Bookmark getBookmark(int i){
        if(i > index){
            System.out.println("Range out of Bound. Returned void bookmark");
            return new Bookmark();
        }else{
            return bookmarks[i];
        }
    };
    public void mergeByGroup(){
        Bookmark temp = new Bookmark();
        try{
            for(int i =0 ;i < index; i++){
                for(int j =i+1; j<index; j++){
                    if(bookmarks[i].groupName.equals(bookmarks[j].groupName)){
                        temp = bookmarks[j];
                        bookmarks[j] = bookmarks[i+1];
                        bookmarks[i+1] = temp;
                        i++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    };

    FileReader filereader = new FileReader();

    public void bookmarkToFile() {
        File file = new File("/Users/jasonahn/IdeaProjects/SoftwareProject_BookMark/src/af2.txt");
        try {
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            for (int i = 0; i < index; i++) {
                printWriter.print(this.bookmarks[index].bookMarkName + ";");
                printWriter.print(this.bookmarks[index].createdTime + ";");
                printWriter.print(this.bookmarks[index].url + ";");
                printWriter.print(this.bookmarks[index].groupName + ";");
                printWriter.print(this.bookmarks[index].memo + ";\n");
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //constructor
    public BookmarkList(String bookmarkFilename){
        bookmarks = new Bookmark[100];
        fileStream = new File(bookmarkFilename);
        this.fillBookmarkList();
    }


    public void fillBookmarkList(){ //fill BookmarkList with Bookmarks
        try{
            Scanner scanner = new Scanner(fileStream);
            while (scanner.hasNext()){
                String str = scanner.nextLine();
                if(str.startsWith("//") | str.isEmpty()){
                    continue;
                }else if(filereader.lineParser(str)){
                    bookmarks[index] = filereader.instanceCreator();
                    index++;
                }else{
                    System.out.println("in line : " + str);
                };
            }
        } catch(IOException e){
            System.out.println("Unknown BookmarkList data File");
        }catch (Exception e){
            e.getStackTrace();
            System.out.println(e.getMessage());
        }
    }
}

//class that reads file and makes bookmark instances
class FileReader{
    LineChecker lineChecker = new LineChecker();
    String[] BookMarkInfo = new String[5]; //name, time, url, groupname, memo
    boolean lineParser(String str) throws Exception{
        Arrays.fill(this.BookMarkInfo,"");
        try{
            String[] temp = str.split(";|,");
            for(int i =0 ; i < temp.length; i++) {
                temp[i] = temp[i].trim();
            }
            if(lineChecker.validityCheck(temp) == 1){
                for(int i = 0 ; i < temp.length; i++){
                    this.BookMarkInfo[i] = temp[i];
                }
                for(int i =0 ; i < temp.length; i++) {
                    this.BookMarkInfo[i] = this.BookMarkInfo[i].trim();
                }
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            System.out.println("ERROR : " + e.getMessage());
        }
        return false;
    }

    Bookmark instanceCreator(){ //method that makes bookmark instances and fills bookmark list
        Bookmark temp = new Bookmark();

        temp.bookMarkName = this.BookMarkInfo[0];
        temp.createdTime = this.BookMarkInfo[1];
        temp.url = this.BookMarkInfo[2];
        temp.groupName = this.BookMarkInfo[3];
        temp.memo = this.BookMarkInfo[4];

        return temp;
    }

}

//class that checks validity of each line by using url pattern, and time pattern
class LineChecker{
    public String timeValidPattern = "^2\\d\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])_([0-1][0-9]|2[0-3]):[0-5][0-9]$"; //checks the formats of created time

    class PatternChecker{ //subclass of line checker that checks url, time pattern
        public int urlCheck(String str) throws Exception{
            try {
                URL aurl = new URL(str);
                return 1;
            }catch(MalformedURLException e) {
                System.out.print("MalformedURLException: wrong URL - Invalid URL Pattern OR No URL ; ");
            }
            return 0;
        }
        public int timeCheck(String str){
            try{
                if(str.matches(timeValidPattern)){
                    return 1;
                }else if(str.isEmpty()) {
                    System.out.print("Date Format Error -> No Created Time ");
                    return 0;
                }
                else{
                    System.out.print("Date Format Error -> Invalid Time Pattern ");
                    return 0;
                }
            }catch(Exception e){
                System.out.println("ERROR" + e.getMessage());
            }
            return 0;
        }
    }
    int validityCheck(String[] str) throws Exception{ // checks each line if it satisfies Bookmark condition
        PatternChecker patternChecker = new PatternChecker();
        if(patternChecker.timeCheck(str[1]) == 1){
            if(patternChecker.urlCheck(str[2]) == 1){ //if all conditions are satisfied (url, time)
                return 1; // ***TEST***
            }else{ //if url pattern is invalid, then it's invalid
                return 0;
            }
        }
        else{
            return 0;
        }
    }
}


class Test{
    public static void main(String[] args) {
        String str = "/Users/jasonahn/IdeaProjects/SoftwareProject_BookMark/src/af.txt";
        BookmarkList bookmarkList = new BookmarkList(str);
        for(int i = 0; i < bookmarkList.index; i++ ){
            bookmarkList.bookmarks[i].print();
        }
    }
}