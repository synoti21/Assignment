import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;


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

                }else{
                }
                bookmarkList.printBookmarks();
            }
        });

        //UP
        jbtUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        //DOWN
        jbtDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

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

                    bookmarkList.bookmarks.add(tempBookmark);

                    temp = tempBookmark.bookmarkToArray();

                    //Case Distributing
                    if(tempBookmark.groupName.isEmpty()){ //groupName이 없으면
                        tablePanel.model.addRow(temp);
                    }else{ //있으면 다시 케이스 분류
                        for(int i = 0; i < tablePanel.model.getRowCount(); i++){
                            if(tablePanel.model.getValueAt(i,1).equals(tempBookmark.groupName)){ //table에 있으면
                                if(tablePanel.model.getValueAt(i,0).equals("V")){
                                    tablePanel.model.insertRow(i+bookmarkList.countGroup(tempBookmark.groupName),temp);
                                    bookmarkList.mergeByGroup();
                                    break;
                                }
                                break;
                            }
                            if(i >= tablePanel.model.getRowCount()-1){
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