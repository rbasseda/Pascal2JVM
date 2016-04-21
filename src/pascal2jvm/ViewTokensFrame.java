package pascal2jvm;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import java.lang.Object;
import java.io.*;

public class ViewTokensFrame extends JFrame {
  //Variables
  private boolean flgSet;
  private boolean opened;
  private String path;
  public Lexer myLexer;
  private Token myToken;
  private JPanel contentPane;
  private ImageIcon image1;
  private ImageIcon image2;
  private ImageIcon image3;
  private BorderLayout borderLayout1 = new BorderLayout();
  private JPanel jPanel1 = new JPanel();
  private JButton jLexerButton = new JButton();
  private JLabel jLabel1 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JLabel jlblCode = new JLabel();
  private JLabel jlblRow = new JLabel();
  private JLabel jlblCol = new JLabel();
  JLabel jLabel4 = new JLabel();
  JLabel jLabel5 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea fileout = new JTextArea();
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileOpen = new JMenuItem();
  JMenuItem jMenuFileSave = new JMenuItem();
  JMenu jMenuComplie = new JMenu();
  JMenuItem jMenuComplieLexer = new JMenuItem();
  JMenuItem jMenuCompileCompile = new JMenuItem();

  /**Construct the frame*/
  public ViewTokensFrame() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    flgSet = true;
    //Opened = false;
    jLexerButton.setVisible(false);
    jlblCol.setVisible(false);
    jlblRow.setVisible(false);
    jlblCode.setVisible(false);
    jLabel1.setVisible(false);
    jLabel2.setVisible(false);
    jLabel3.setVisible(false);
    jLabel4.setVisible(false);
    jLabel5.setVisible(false);


  }
  /**Component initialization*/
  private void jbInit() throws Exception  {
    image1 = new ImageIcon(pascal2jvm.ViewTokensFrame.class.getResource("../icons/openFile.gif"));
    image2 = new ImageIcon(pascal2jvm.ViewTokensFrame.class.getResource("../icons/closeFile.gif"));
    image3 = new ImageIcon(pascal2jvm.ViewTokensFrame.class.getResource("../icons/help.gif"));
    //setIconImage(Toolkit.getDefaultToolkit().createImage(ViewTokens_Frame.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    contentPane.setLayout(borderLayout1);
    this.getContentPane().setBackground(SystemColor.textHighlightText);
    this.setJMenuBar(jMenuBar1);
    this.setResizable(false);
    this.setSize(new Dimension(400, 427));
    this.setTitle("View Tokens");
    jPanel1.setLayout(null);
    jLexerButton.setBounds(new Rectangle(18, 213, 165, 27));
    jLexerButton.setBackground(new Color(141, 125, 98));
    jLexerButton.setEnabled(false);
    jLexerButton.setToolTipText("");
    jLexerButton.setText("Lexer: Test Next Symbol");
    jLexerButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Lexer_actionPerformed(e);
      }
    });
    jPanel1.setBackground(Color.lightGray);
    jLabel1.setText("S_code");
    jLabel1.setBounds(new Rectangle(142, 277, 66, 21));
    jLabel2.setBounds(new Rectangle(142, 307, 66, 21));
    jLabel2.setText("Row");
    jLabel3.setBounds(new Rectangle(142, 333, 66, 21));
    jLabel3.setText("Column");
    jlblCode.setText("Code of Token");
    jlblCode.setBounds(new Rectangle(17, 276, 107, 23));
    jlblRow.setText("Row");
    jlblRow.setBounds(new Rectangle(17, 303, 98, 23));
    jlblCol.setBounds(new Rectangle(19, 329, 98, 23));
    jlblCol.setText("Column");
    jLabel4.setText("Word");
    jLabel4.setBounds(new Rectangle(18, 252, 86, 25));
    jLabel5.setText("Word");
    jLabel5.setBounds(new Rectangle(142, 252, 85, 26));
    jScrollPane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane1.setBounds(new Rectangle(10, 19, 166, 179));
    jTextArea1.setText("No File Loaded");
    jTextArea1.setBackground(new Color(226, 202, 152));
    jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    jScrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    jScrollPane2.setBounds(new Rectangle(188, 18, 192, 181));
    jMenuFile.setText("File");
    jMenuFileOpen.setText("Open");
    jMenuFileOpen.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuFileOpen_actionPerformed(e);
      }
    });
    jMenuFileSave.setEnabled(false);
    jMenuFileSave.setText("Save");
    jMenuFileSave.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuFileSave_actionPerformed(e);
      }
    });
    jMenuComplie.setText("Compile");
    jMenuComplieLexer.setEnabled(false);
    jMenuComplieLexer.setText("Lexer Analyser");
    jMenuComplieLexer.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuComplieLexer_actionPerformed(e);
      }
    });
    jMenuCompileCompile.setEnabled(false);
    jMenuCompileCompile.setText("Compile");
    jMenuCompileCompile.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        jMenuCompileCompile_actionPerformed(e);
      }
    });
    contentPane.setToolTipText("");
    contentPane.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jScrollPane1, null);
    jPanel1.add(jLexerButton, null);
    jPanel1.add(jlblCol, null);
    jPanel1.add(jLabel4, null);
    jPanel1.add(jLabel2, null);
    jPanel1.add(jLabel3, null);
    jPanel1.add(jScrollPane2, null);
    jPanel1.add(jlblCode, null);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jLabel5, null);
    jPanel1.add(jlblRow, null);
    jScrollPane2.getViewport().add(fileout, null);
    jScrollPane1.getViewport().add(jTextArea1, null);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuComplie);
    jMenuFile.add(jMenuFileOpen);
    jMenuFile.add(jMenuFileSave);
    jMenuComplie.add(jMenuComplieLexer);
    jMenuComplie.add(jMenuCompileCompile);
  }
  /**File | Exit action performed*/
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }
  /**Help | About action performed*/
  public void jMenuHelpAbfileout_actionPerformed(ActionEvent e) {
    ViewTokensFrameAboutBox dlg = new ViewTokensFrameAboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.show();
  }
  /**Overridden so we can exit when window is closed*/
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      System.exit(0);
    }
  }

  void Lexer_actionPerformed(ActionEvent e) {
  String Tempcode;
  if(flgSet)myToken=myLexer.nextSymbol();
  Tempcode=(String)myToken.getcode();
//if (Tempcode=="End") return;
  if ((!Tempcode.equalsIgnoreCase("s_nocode"))&&flgSet)
      {
       jLabel1.setText(Tempcode);
       jLabel2.setText(myToken.gerrow());
       jLabel3.setText(myToken.getcol());
       jLabel5.setText(myToken.lexeme) ;

       }
 // if(Lexer1.EOFFlag)FlgSet=false;
  }



  void jMenuFileOpen_actionPerformed(ActionEvent e) {
    JFileChooser chooser = new JFileChooser();
    int i=0;
    StringBuffer temppath;
    temppath=new StringBuffer();
    String fStr;
    File f;
    FileReader fReader;
    int returnVal = chooser.showOpenDialog(this);
    if(returnVal == JFileChooser.APPROVE_OPTION) {
            f=chooser.getSelectedFile();
            path=f.getPath();
            try{
                fReader=new FileReader(f);
                StringBuffer Temp=new StringBuffer();
                while(i!=-1){
                    char T='\0';
                    try{
                        i=fReader.read() ;
                        T=(char) i;
                    }
                    catch(IOException E){
                    }
                    if(i==-1) break;
                    else
                      Temp.append(T);
                }
                fStr=Temp.toString();
                jTextArea1.setText(fStr) ;
                jMenuFileSave.setEnabled(true);
                jMenuCompileCompile.setEnabled(true);
                jMenuComplieLexer.setEnabled(true);
                try{
                  fReader.close();
                }
                catch(IOException E){
                }

            }
            catch(FileNotFoundException E){
            }
            }

  }

  void jMenuFileSave_actionPerformed(ActionEvent e) {
    int i=0,T=0;
    String fStr;
    File f;
    FileWriter fWriter;
    try{
      f=new File(path);
      try{
        fWriter=new FileWriter(f);
        byte Temp[]=new byte[256];
        fStr=jTextArea1.getText();
        Temp=fStr.getBytes();
//        fWriter.write(fStr);
        boolean done=false;
        for(i=0;(i<(fStr.length()));++i)
        {
          char Test;
          if((Temp[i]=='\r')&&(Temp[i+1]!='\n'))++i;
          Test=(char) Temp[i];
          if((Temp[i]=='\n')&&(Temp[i-1]!='\r')&&(done==false)){
            Test='\r';
            T=(int) Test;
            --i;
            done=true;
            }
          else{
            T=(int)Temp[i];
            done=false;
          }
          Test=(char) T;
          fWriter.write(T);
        }
        try{
          fWriter.close();
           f=null;
           fWriter=null;
        }
        catch(IOException E){
        }

      }
      catch(IOException E){
      }
  }
  catch(NullPointerException E){
  }
  }

  void jMenuComplieLexer_actionPerformed(ActionEvent e) {
	  jLexerButton.setEnabled(true);
   flgSet=true;
/*   if(Opened)
    Lexer1.closefile();
   else
       Opened=true;*/
   if(path==null){
     myLexer=new Lexer();
   }
   else{
     myLexer=new Lexer(path);
   }
   jLexerButton.setVisible(true);
   jlblCol.setVisible(true);
   jlblRow.setVisible(true);
   jlblCode.setVisible(true);
   jLabel1.setVisible(true);
   jLabel2.setVisible(true);
   jLabel3.setVisible(true);
   jLabel4.setVisible(true);
   jLabel5.setVisible(true);
  }


  void jMenuCompileCompile_actionPerformed(ActionEvent e) {
    flgSet=true;
    if(path==null){
      myLexer=new Lexer();
    }
    else{
      myLexer=new Lexer(path);
    }

    fileout.selectAll();
    fileout.cut();
    SyntaxAnalyser Syntax_Analyser1=new SyntaxAnalyser(myLexer);

    fileout.append(Syntax_Analyser1.output.toString());
    myLexer.closefile();
  }

  }
