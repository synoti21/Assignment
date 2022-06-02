import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;


//Main Frame

class BookmarkManager extends JFrame{ //main frame of Bookmark Manager

    BookmarkListTablePanel bookmarkListTablePanel;
    BookmarkListButtonPanel bookmarkListButtonPanel;

    BookmarkManager(BookmarkList bookmarkList) throws Exception {
        super("Bookmark Manager");

        bookmarkListTablePanel = new BookmarkListTablePanel(bookmarkList);
        bookmarkListButtonPanel = new BookmarkListButtonPanel(bookmarkList,bookmarkListTablePanel);

        setLayout(new BorderLayout());

        add(bookmarkListTablePanel,BorderLayout.WEST);
        add(bookmarkListButtonPanel,BorderLayout.EAST);

        pack();
        setTitle("Bookmark Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,600);
        setVisible(true);

    }
}



//Main Panels ; Main Table of BookmarkList

class BookmarkListTablePanel extends JPanel{  //Main panel that displays registered Bookmarks
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;


    String[] header = {"","Group","Name","URL","Created Time","Memo"};

    /*public static void refreshTable(BookmarkList bookmarkList, DefaultTableModel model) throws Exception{

    }*/

    public int getRealIndexOfBookmark(BookmarkList bookmarkList){
        int selectedRowIndex = table.getSelectedRow();
        int realIndex = selectedRowIndex;
        for(int i = 0 ; i < selectedRowIndex; i++){
            if(model.getValueAt(i,3).toString().isEmpty()){ // recognizes group mark
                System.out.println("Group Found in row : " + i);

                String groupForIndexing = model.getValueAt(i,1).toString(); //counting members of group
                if(model.getValueAt(i,0).toString().equals(">")){ //counting opened
                    realIndex += (bookmarkList.countGroup(groupForIndexing) -1);
                    System.out.println("Minus Group" + (bookmarkList.countGroup(groupForIndexing) -1));
                }else{ //counting closed
                    realIndex -= 1;
                    System.out.println("Mi" + (-1));

                }
            }
        }
        return realIndex;
    }

    public BookmarkListTablePanel(BookmarkList bookmarkList) throws Exception {
        bookmarkList.mergeByGroup();

        //table default set
        this.model = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.model.setColumnCount(header.length);
        this.model.setColumnIdentifiers(header);

        //get real index of selected row

        //set each column's default size
        table = new JTable(this.model);
        table.getColumnModel().getColumn(0).setPreferredWidth(20); //v
        table.getColumnModel().getColumn(1).setPreferredWidth(100); //group
        table.getColumnModel().getColumn(2).setPreferredWidth(120); //name
        table.getColumnModel().getColumn(3).setPreferredWidth(200); //url
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // created time
        table.getColumnModel().getColumn(5).setPreferredWidth(110); //memo


        scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(700,600));

        //adding bookmark info to each row
        bookmarkList.mergeByGroup();
        model.setRowCount(0);
        System.out.println("current Bookmarks : "+ bookmarkList.numBookmarks());
        System.out.println("current real count : " + bookmarkList.numBookmarks());
        for(int i =0 ; i < bookmarkList.numBookmarks(); i++){
            System.out.println(bookmarkList.getBookmark(i).bookMarkName);

        }

        bookmarkList.printBookmarks();

        String currentGroup = "";
        String[] bookmarkToArrayTemp = new String[6];
        Bookmark currentBookmark;

        for(int i =0 ; i < bookmarkList.numBookmarks(); i++){
            currentBookmark = bookmarkList.getBookmark(i);


            if(!currentGroup.equals(currentBookmark.groupName )&& !currentBookmark.groupName.isEmpty()){
                bookmarkToArrayTemp[0] = ">";
                bookmarkToArrayTemp[1] = currentBookmark.groupName;
                bookmarkToArrayTemp[2] = "";
                bookmarkToArrayTemp[3] = "";
                bookmarkToArrayTemp[4] = "";
                bookmarkToArrayTemp[5] = "";

                model.addRow(bookmarkToArrayTemp);
                currentGroup = currentBookmark.groupName; //hiding bookmarks with group
            }else {
                if (!currentGroup.equals(currentBookmark.groupName) || currentBookmark.groupName.isEmpty()) {
                    bookmarkToArrayTemp[0] = "";
                    bookmarkToArrayTemp[1] = currentBookmark.groupName;
                    bookmarkToArrayTemp[2] = currentBookmark.bookMarkName;
                    bookmarkToArrayTemp[3] = currentBookmark.url;
                    bookmarkToArrayTemp[4] = currentBookmark.createdTime;
                    bookmarkToArrayTemp[5] = currentBookmark.memo;

                    model.addRow(bookmarkToArrayTemp);
                    currentGroup = ""; //showing bookmarks with no group
                }
            }

        }
        //****
        add(scrollPane);
        setSize(700,600);


        //opening, closing group
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() > 0){
                    JTable target = (JTable)e.getSource();
                    if(target != table) return;

                    if(table.getSelectedColumn() == 0){
                        String mark = (String) table.getValueAt(table.getSelectedRow(),0);
                        String selectedGroup = (String) table.getValueAt(table.getSelectedRow(),1);
                        if (mark.equals(">")) {
                            System.out.println("clicked");
                            String[] tempArray;
                            int targetIndex = bookmarkList.indexOfGroupName(selectedGroup);
                            int numofMember = bookmarkList.countGroup(selectedGroup);
                            for(int i =0; i < numofMember; i++){
                                tempArray = bookmarkList.bookmarks.get(targetIndex+i).bookmarkToArray();
                                model.insertRow(table.getSelectedRow()+1+i,tempArray);
                            }
                            table.setValueAt("V",table.getSelectedRow(),0);

                        }else if(mark.equals("V")){
                            int numofMember = bookmarkList.countGroup(selectedGroup);
                            for(int i=0;i < numofMember; i++){
                                model.removeRow(table.getSelectedRow()+1);
                            }
                            table.setValueAt(">",table.getSelectedRow(),0);

                        }
                    }
                }
            }
        });
    }
}

class BookmarkListButtonPanel extends JPanel{ //Sub Panel that contains the buttons of program
    JButton jbtAdd;
    JButton jbtDelete;
    JButton jbtUp;
    JButton jbtDown;
    JButton jbtSave;



    BookmarkListButtonPanel(BookmarkList bookmarkList, BookmarkListTablePanel tablePanel){
        setLayout(new GridLayout(5,1));
        //setBackground(Color.blue);

        jbtAdd = new JButton("ADD");
        jbtDelete = new JButton("DELETE");
        jbtUp = new JButton("UP");
        jbtDown = new JButton("DOWN");
        jbtSave = new JButton("SAVE");

        add(jbtAdd);
        add(jbtDelete);
        add(jbtUp);
        add(jbtDown);
        add(jbtSave);


        //Each Button's Action
        //ADD
        jbtAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new BookmarkInfo(bookmarkList,tablePanel);
            }
        });

        //DELETE
        jbtDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = tablePanel.table.getSelectedRow();

                String targetGroup = tablePanel.model.getValueAt(selectedRowIndex,1).toString();

                int numOfTargetMember = bookmarkList.countGroup(targetGroup);
                int indexOfTargetGroup = bookmarkList.indexOfGroupName(targetGroup);

                if(tablePanel.model.getValueAt(selectedRowIndex,0).equals(">")){
                    //delete all members of group
                    for(int i = 0; i < numOfTargetMember; i++){
                        bookmarkList.bookmarks.remove(indexOfTargetGroup);
                    }
                    //remove data from table
                    tablePanel.model.removeRow(selectedRowIndex);
                }else if(tablePanel.model.getValueAt(selectedRowIndex,0).equals("V")){
                    //delete all members of group
                    for(int i = 0; i < numOfTargetMember; i++){
                        bookmarkList.bookmarks.remove(indexOfTargetGroup);
                    }
                    //remove data from table
                    for(int i =0 ; i < numOfTargetMember+1; i++){
                        tablePanel.model.removeRow(selectedRowIndex);
                    }

                }else{ //deleting only bookmark => getting real index of selected Bookmark
                    int realIndex = tablePanel.getRealIndexOfBookmark(bookmarkList);

                    //DEBUG
                    //System.out.println("Selected Row : " + selectedRowIndex);
                    //System.out.println("Real Index : " + realIndex);

                    tablePanel.model.removeRow(selectedRowIndex);
                    bookmarkList.bookmarks.remove(realIndex);


                    //DEBUG
                    /*System.out.println(tablePanel.model.getValueAt(selectedRowIndex-1,0).toString() //if group is opened,
                            .equals("V"));
                    System.out.println(bookmarkList.countGroup(tablePanel.model.getValueAt(selectedRowIndex-1,1).toString()));*/


                    if(selectedRowIndex > 0 && tablePanel.model.getValueAt(selectedRowIndex-1,0).toString() //if group is opened,
                            .equals("V") &&
                    bookmarkList.countGroup(tablePanel.model.getValueAt(selectedRowIndex-1,1).toString()) <=0
                    ){
                        //and there's no group member after deleting selected Bookmark,
                        tablePanel.model.removeRow(selectedRowIndex-1); //delete group mark
                    }

                }
                bookmarkList.printBookmarks();
            }
        });

        //UP
        jbtUp.addActionListener(new ActionListener() {


            public void moveUpTable(int selectedIndex){
                tablePanel.model.moveRow(selectedIndex,selectedIndex,selectedIndex-1);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tablePanel.table.getSelectedRow();
                int realIndex = tablePanel.getRealIndexOfBookmark(bookmarkList);
                //맨위가 아닐 경우에만 작동
                if(selectedIndex > 0){
                    //up할 대상이 그룹이고 열린(open)상태일 경우 -> 경고 메시지
                    if(tablePanel.model.getValueAt(selectedIndex,0).toString().equals("V")){
                        System.out.println("Group is opened");
                    }//up할 대상이 그룹 표시 행이고 닫힌 상태일 경우 -> 2가지 경우로 나뉘어 작동
                    else if(tablePanel.model.getValueAt(selectedIndex,0).toString().equals(">")){
                        String selectedGroup = tablePanel.model.getValueAt(selectedIndex,1).toString();
                        // 그 위가 단일 북마크이면서 그룹 이름이 없을 경우*****
                        if(tablePanel.model.getValueAt(selectedIndex-1,0).toString().isEmpty()
                        && tablePanel.model.getValueAt(selectedIndex-1,1).toString().isEmpty()){
                            Bookmark temp = bookmarkList.bookmarks.get(realIndex-1); //그 위 북마크를 임시로 제거한다.
                            bookmarkList.bookmarks.remove(realIndex-1);
                            bookmarkList.bookmarks.add(realIndex+bookmarkList.countGroup(selectedGroup)-1,temp);
                            moveUpTable(selectedIndex);
                            System.out.println("dfd");

                        }// 그 위가 그룹이며, 닫힌 상태 일경우*******
                        else if(tablePanel.model.getValueAt(selectedIndex-1,0).toString().equals(">")){
                            ArrayList<Bookmark> tempArray = new ArrayList<Bookmark>();
                            String upperGroup = tablePanel.model.getValueAt(selectedIndex-1,1).toString();
                            int countOfSelectedGroup = bookmarkList.countGroup(selectedGroup);

                            for(int i = 0; i < countOfSelectedGroup; i++){
                                tempArray.add(bookmarkList.bookmarks.get(realIndex));
                                System.out.println("selected Group : " + selectedGroup);
                                System.out.println("selected Group count: " + bookmarkList.countGroup(selectedGroup));

                                System.out.println("moved : "+bookmarkList.bookmarks.get(realIndex).url);
                                bookmarkList.bookmarks.remove(realIndex);

                            }
                            bookmarkList.bookmarks.addAll(realIndex-bookmarkList.countGroup(upperGroup),tempArray);
                            moveUpTable(selectedIndex);

                        }else{//열리면 난입 금지 작동 X
                            System.out.println("upper Group is opened");
                        }
                    }else{//up할 대상이 단일 북마크일 경우
                        //up할 대상이 그룹 없는 북마크일 경우
                        if(tablePanel.model.getValueAt(selectedIndex,1).toString().isEmpty()){
                            //그 위가 단일 북마크일 경우 -> 동작*
                            if(tablePanel.model.getValueAt(selectedIndex-1,1).toString().isEmpty()){
                                Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                bookmarkList.bookmarks.remove(realIndex);
                                bookmarkList.bookmarks.add(realIndex-1,temp);
                                moveUpTable(selectedIndex);

                            }else{ //그 위가 그룹명이 있는 경우 => 그룹 있는 단일 북마크이거나, 그룹 표시 행일 수 있다.
                                if(tablePanel.model.getValueAt(selectedIndex-1,0).toString().isEmpty()){
                                    //그 위가 그룹은 적혀있는데 >또는 V가 없을 경우 => 열린 그룹안에 있는 북마크이므로 난입 금지
                                    System.out.println("opened group");
                                }else{ //그 위가 닫힌 그룹이면 동작*****
                                    String upperGroup = tablePanel.model.getValueAt(selectedIndex-1,1).toString();
                                    Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                    bookmarkList.bookmarks.remove(realIndex);
                                    bookmarkList.bookmarks.add(realIndex-bookmarkList.countGroup(upperGroup),temp);
                                    moveUpTable(selectedIndex);
                                }
                            }
                        }else{ //up할 대상이 그룹 있는 북마크일 경우 -> 열린 그룹(V)안에 있는 북마크일경우, 그 안에서 움직여야한다.
                            //그 위에 url이 안적혀있으면 그룹 표시 행, 곧 상한선 이므로 동작 금지
                            if(tablePanel.model.getValueAt(selectedIndex-1,3).toString().isEmpty()){
                                System.out.println("그룹 밖으로 나갈 수 없습니다.");
                            }else{ //안쪽이므로 동작*
                                Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                bookmarkList.bookmarks.remove(realIndex);
                                bookmarkList.bookmarks.add(realIndex-1,temp);
                                moveUpTable(selectedIndex);
                            }

                        }
                    }
                }else{ //맨위일 경우
                    System.out.println("Top bookmark");
                }
                bookmarkList.printBookmarks();
            }
        });

        //DOWN
        jbtDown.addActionListener(new ActionListener() {

            public void moveDownTable(int selectedIndex){
                tablePanel.model.moveRow(selectedIndex,selectedIndex,selectedIndex+1);
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = tablePanel.table.getSelectedRow();
                int realIndex = tablePanel.getRealIndexOfBookmark(bookmarkList);
                //맨 아래가 아닐 경우에만 작동
                if(selectedIndex < tablePanel.model.getRowCount()-1){
                    //down할 대상이 그룹이고 열린(open)상태일 경우 -> 경고 메시지
                    if(tablePanel.model.getValueAt(selectedIndex,0).toString().equals("V")){
                        System.out.println("Group is opened");
                    }//down할 대상이 그룹 표시 행이고 닫힌 상태일 경우 -> 2가지 경우로 나뉘어 작동
                    else if(tablePanel.model.getValueAt(selectedIndex,0).toString().equals(">")){
                        String selectedGroup = tablePanel.model.getValueAt(selectedIndex,1).toString();
                        // 그 아래가 단일 북마크이면서 그룹 이름이 없을 경우*****
                        if(tablePanel.model.getValueAt(selectedIndex+1,0).toString().isEmpty()
                                && tablePanel.model.getValueAt(selectedIndex+1,1).toString().isEmpty()){
                            Bookmark temp = bookmarkList.bookmarks.get(realIndex+bookmarkList.countGroup(selectedGroup)); //그 위 북마크를 임시로 제거한다.
                            bookmarkList.bookmarks.remove(realIndex+bookmarkList.countGroup(selectedGroup));
                            bookmarkList.bookmarks.add(realIndex,temp);
                            moveDownTable(selectedIndex);
                            System.out.println("dfd");

                        }// 그 아래가 그룹이며, 닫힌 상태 일경우*******
                        else if(tablePanel.model.getValueAt(selectedIndex+1,0).toString().equals(">")){
                            ArrayList<Bookmark> tempArray = new ArrayList<Bookmark>();
                            String lowerGroup = tablePanel.model.getValueAt(selectedIndex+1,1).toString();
                            int countOfSelectedGroup = bookmarkList.countGroup(selectedGroup);

                            for(int i = 0; i < countOfSelectedGroup; i++){
                                tempArray.add(bookmarkList.bookmarks.get(realIndex));
                                System.out.println("selected Group : " + selectedGroup);
                                System.out.println("selected Group count: " + bookmarkList.countGroup(selectedGroup));

                                System.out.println("moved : "+bookmarkList.bookmarks.get(realIndex).url);
                                bookmarkList.bookmarks.remove(realIndex);

                            }
                            bookmarkList.bookmarks.addAll(realIndex+bookmarkList.countGroup(lowerGroup),tempArray);
                            moveDownTable(selectedIndex);

                        }else{//열리면 난입 금지 작동 X
                            System.out.println("upper Group is opened");
                        }
                    }else{//down할 대상이 단일 북마크일 경우
                        //down할 대상이 그룹 없는 북마크일 경우
                        if(tablePanel.model.getValueAt(selectedIndex,1).toString().isEmpty()){
                            //그 아래가 단일 북마크일 경우 -> 동작*
                            if(tablePanel.model.getValueAt(selectedIndex+1,1).toString().isEmpty()){
                                Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                bookmarkList.bookmarks.remove(realIndex);
                                bookmarkList.bookmarks.add(realIndex+1,temp);
                                moveDownTable(selectedIndex);

                            }else{ //그 아래가 그룹명이 있는 경우 => 무조건 그룹 마크 행이다
                                if(tablePanel.model.getValueAt(selectedIndex+1,0).toString().equals("V")){
                                    //V이면 => 열린 그룹안에 있는 북마크이므로 난입 금지
                                    System.out.println("opened group");
                                }else{ //그 아래 닫힌 그룹이면 동작*****
                                    String lowerGroup = tablePanel.model.getValueAt(selectedIndex+1,1).toString();
                                    Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                    bookmarkList.bookmarks.remove(realIndex);
                                    bookmarkList.bookmarks.add(realIndex+bookmarkList.countGroup(lowerGroup),temp);
                                    moveDownTable(selectedIndex);
                                }
                            }
                        }else{ //down할 대상이 그룹 있는 북마크일 경우 -> 열린 그룹(V)안에 있는 북마크일경우, 그 안에서 움직여야한다.
                            //그 아래의 북마크의 그룹명이 다르다면 경계선이다
                            if(!tablePanel.model.getValueAt(selectedIndex,1).toString()
                                    .equals(tablePanel.model.getValueAt(selectedIndex+1,1).toString())){
                                System.out.println("그룹 밖으로 나갈 수 없습니다.");
                            }else{ //안쪽이므로 동작*
                                Bookmark temp = bookmarkList.bookmarks.get(realIndex);
                                bookmarkList.bookmarks.remove(realIndex);
                                bookmarkList.bookmarks.add(realIndex+1,temp);
                                moveDownTable(selectedIndex);
                            }

                        }
                    }
                }else{ //맨아래일 경우
                    System.out.println("Lowest bookmark");
                }
                bookmarkList.printBookmarks();
            }

        });

        //SAVE
        jbtSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookmarkList.bookmarkToFile();
                JOptionPane.showMessageDialog(null,"Bookmark Saved");
            }
        });
    }

}

//Subframe of ADD

class BookmarkInfo extends JFrame {  //Adding new Bookmark
    JButton jbtInput;
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;

    String[] header = {"Group","Name","URL","Memo"};
    String[] temp = {"","","","","",""};

    //subclass of bookmarkinfo => table
    class BookmarkInfoTable extends JPanel{
        public BookmarkInfoTable(){
            //setting default table
            model = new DefaultTableModel();
            model.setColumnCount(header.length);
            model.setColumnIdentifiers(header);

            table = new JTable(model);

            table.getColumnModel().getColumn(0).setPreferredWidth(70); //group
            table.getColumnModel().getColumn(1).setPreferredWidth(80); //name
            table.getColumnModel().getColumn(2).setPreferredWidth(90); //url
            table.getColumnModel().getColumn(3).setPreferredWidth(60); //memo

            scrollPane = new JScrollPane(table);
            scrollPane.setPreferredSize(new Dimension(400,100));

            model.addRow(temp);
            add(scrollPane);


        }
    }


    public BookmarkInfo(BookmarkList bookmarkList, BookmarkListTablePanel tablePanel){
        super("Input New Bookmark");
        setLayout(new BorderLayout());

        System.out.println("TEST : " + bookmarkList.numBookmarks());
        System.out.println("ADDED : " + bookmarkList.numBookmarks());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        jbtInput = new JButton("Input");
        jbtInput.setSize(100,100);
        inputPanel.add(jbtInput,BorderLayout.CENTER);

        add(new BookmarkInfoTable(),BorderLayout.WEST);
        add(inputPanel,BorderLayout.EAST);

        pack();

        this.setSize(500,100);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);

        jbtInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Bookmark tempBookmark;
                URL aurl;
                try{
                    aurl = new URL((String)model.getValueAt(0,2));
                    tempBookmark = new Bookmark(aurl.toString());
                    tempBookmark.groupName = (String)model.getValueAt(0,0);
                    tempBookmark.bookMarkName = (String)model.getValueAt(0,1);
                    tempBookmark.memo = (String)model.getValueAt(0,3);

                    bookmarkList.bookmarks.add(tempBookmark); //일단 북마크리스트에 먼저 북마크 추가, 그룹 정렬은 나중에 할 예정

                    temp = tempBookmark.bookmarkToArray();

                    //Case Distributing
                    if(tempBookmark.groupName.isEmpty()){ //groupName이 없으면
                        tablePanel.model.addRow(temp);
                    }else{ //있으면 다시 케이스 분류
                        for(int i = 0; i < tablePanel.model.getRowCount(); i++){
                            if(tablePanel.model.getValueAt(i,1).equals(tempBookmark.groupName)){ //table에 이미 group이 있으면
                                if(tablePanel.model.getValueAt(i,0).equals("V")){ //열린 상태일경우 맨 밑에 추가
                                    tablePanel.model.insertRow(i+bookmarkList.countGroup(tempBookmark.groupName),temp);
                                    bookmarkList.mergeByGroup();
                                    break;
                                } //없으면 테이블은 가만히 둔다.
                                bookmarkList.mergeByGroup();
                                break;
                            }
                            if(i >= tablePanel.model.getRowCount()-1){ //table에 없는 group이면
                                temp[0] = ">";
                                temp[2] = "";
                                temp[3] = "";
                                temp[4] = "";
                                temp[5] = "";
                                tablePanel.model.addRow(temp);
                                bookmarkList.mergeByGroup();
                                System.out.println("added");
                                break;
                            }
                        }
                        if(tablePanel.model.getRowCount() < 1){ //if there's no data
                            temp[0] = ">";
                            temp[2] = "";
                            temp[3] = "";
                            temp[4] = "";
                            temp[5] = "";
                            tablePanel.model.addRow(temp);
                        }

                    }
                    bookmarkList.printBookmarks();

                    dispose();
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"정확한 URL을 입력해주세요");
                }
            }
        });
    }
}

class Test{
    public static void main(String[] args) throws Exception {
        BookmarkList bookmarkList = new BookmarkList("/Users/jasonahn/Downloads/프로젝트5-배포자료/bookmark.txt");
        BookmarkManager frame = new BookmarkManager(bookmarkList);
    }
}