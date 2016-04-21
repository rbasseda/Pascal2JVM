package pascal2jvm;

import java.io.*;

/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */

public class Lexer
{
  public boolean eofFlag;
  public boolean eofFlagRep=false;
  public int openflag;
  static int r,c;
  char nextch,lastch='\0';
  FileDescriptor fdobj;
  FileReader fReader;
  static int selection=0;
  //--------Symbols--------
  static int s_error=-1;
  static int s_nocode=0;
  static int s_add=1;
  static int s_and=2;
  static int s_begin=3;
  static int s_case=4;
  static int s_char=5;
  static int s_closepar=6;
  static int s_colon=7;
  static int s_comma=8;
  static int s_div=9;
  static int s_do=10;
  static int s_dot=11;
  static int s_end=12;
  static int s_float=13;
  static int s_for=14;
  static int s_function=15;
  static int s_identifier=16;
  static int s_if=17;
  static int s_integer=18;
  static int s_mul=19;
  static int s_number=20;
  static int s_openpar=21;
  static int s_or=22;
  static int s_procedure=23;
  static int s_program=24;
  static int s_repeat=25;
  static int s_semicolon=26;
  static int s_character=27;
  static int s_sub=28;
  static int s_then=29;
  static int s_until=30;
  static int s_var=31;
  static int s_while=32;
  static int s_assign=33;
  static int s_equal=34;
  static int s_GT=35;
  static int s_GTE=36;
  static int s_LT=37;
  static int s_LTE=38;
  static int s_NE=39;
  static int s_EOF=40;
  static int s_Type=41;
  static int s_real=42;
  static int s_NOT=43;
  static int s_CONST=44;
  static int s_of=45;
  static int s_to=46;
  static int s_else=47;

  //------------------
  static int s_const;
  static int s_type;
//------end of symbols------
  private boolean isAlpha(char ch)
   {
    if( ( (ch >= 'a') && (ch<='z') )||( ( (ch >= 'A') && (ch<='Z') ) ) )
      return true;
      else return false;
   }

  private boolean isDigit(char ch)
   {
    if ( (ch >= '0') && (ch<='9') )
      return true;
      else return false;
   }
  private void lexerError()
   {
   //reserved
   }
 private void openfile()
 {

   fdobj=new FileDescriptor();
   try {
     FileInputStream fileIn = new FileInputStream("d:\\test.txt");
     fdobj=fileIn.getFD();
    }
    catch(FileNotFoundException e) {
//      System.exit(0);
    }
    catch(IOException e) {
//      System.+exit(0);
    }
    fReader=new FileReader(fdobj);
 }
 private void openfile(String fPath)
 {
   fdobj=new FileDescriptor();
   try {
     FileInputStream fileIn = new FileInputStream(fPath);
     fdobj=fileIn.getFD();
    }
    catch(FileNotFoundException e) {
//      System.exit(0);
    }
    catch(IOException e) {
    }
    fReader=new FileReader(fdobj);
 }

 private  char gettingch()
 {
   //reading file
    int i;
    i=0;
    if (eofFlag) return (char) -1;
    try{
      selection++;
      i=fReader.read();
    }
    catch(IOException e) {
//    System.exit(0);
    }
   if (i<=0) {eofFlag=true;selection--;}
   return (char) i;
 }
  private Token makeToken(int code,int len,int row,int col,int nestedblockno, StringBuffer nw)
   {
   String temp;
    Token mytoken = new Token(code,len,row,col,nestedblockno,nw.toString());
    return mytoken;
   }
//**************** main lexer function
   public Token nextSymbol()
   {
   int counter=0,state,length,code=0;
    StringBuffer nextword,tmpnextword;
    state=0;length=0;
    nextword = new StringBuffer();
    tmpnextword = new StringBuffer();

    boolean tokenfound=false;
   // while(EOFFlag!=true)
    do
     {
      code= s_nocode;
      if(lastch!='\0')
       {
        nextch=lastch;
        lastch='\0';
       }
      else
      nextch=gettingch();
      if (eofFlagRep)  break;

      c++;
      if(nextch=='\n')
      {
        c=0;
        r++;
      }
      nextword.append(nextch);
      length++;

      switch(state)
      {
        case 0 :if((nextch==' ')||(nextch=='\n')||(nextch=='\t')||(nextch=='\r'))
                  {
                     length=nextword.length();
                     nextword.deleteCharAt(length-1);
                   length=nextword.length();
                  }
                else if(isAlpha(nextch)) state = 1;
                else if(isDigit(nextch)) state = 2;
                else if(nextch=='.') state = 4;
                else if(nextch=='(') state = 5;
                else if(nextch=='\'') state = 8;
                else if(nextch=='<') state = 10;
                else if(nextch=='>') state = 11;
                else if(nextch==':') state = 12;
                else if(nextch=='{') state = 13;
                else if(nextch=='/'){
                                      code =  s_div;
                                      tokenfound=true;
                                     }
                else if(nextch==';') {
                                       code =  s_semicolon;
                                       tokenfound=true;
                                      }
                else if(nextch=='=') {
                                       code =  s_equal;
                                       tokenfound=true;
                                      }
                else if(nextch==',') {
                                       code =  s_comma;
                                       tokenfound=true;
                                      }
                else if(nextch=='-') {
                                       code =  s_sub;
                                       tokenfound=true;
                                      }
                else if(nextch=='+') {
                                       code =  s_add;
                                       tokenfound=true;
                                      }

                else if(nextch=='*') {
                                       code =  s_mul;
                                       tokenfound=true;
                                      }
                else if(eofFlag) {
                                       code =  s_EOF;
                                       tokenfound=true;
                                       eofFlagRep=true;
                }
                else if(nextch==')') {
                                        code =  s_closepar;
                                        tokenfound=true;
                                      }

                else lexerError();
                break;

        case 1:if(isDigit(nextch) || isAlpha(nextch)||nextch=='$') {state = 1;break;}
               else {
                      int l;
                      //l=nextword.length();
                      //--l;
                      code = iskeyword(nextword);
                      //nextword.deleteCharAt(l);
                      lastch=nextch;
                      tokenfound=true;
                    }
               break;

        case 2:if(isDigit(nextch)) state = 2;
               else if(nextch=='.') state = 3;
               else
               {
                 code =  s_number;
                 lastch=nextch;
                 nextword.deleteCharAt(nextword.length()-1);
                 tokenfound=true;
               }
               break;

        case 3:if(isDigit(nextch)) state = 3;
               else {
               code =  s_float;
                nextword.deleteCharAt(nextword.length()-1);
                lastch=nextch;
                 tokenfound=true;
               }
               break;

        case 4:if(isDigit(nextch)) state = 3;
               else
               {
                code =  s_dot;
                nextword.deleteCharAt(nextword.length()-1);
                lastch=nextch;
                tokenfound=true;
               }
               break;

        case 5:if(nextch=='*') state = 6;
               else {
               code =  s_openpar;
                 lastch=nextch;
                nextword.deleteCharAt(nextword.length()-1);
                 tokenfound=true;
               }

               break;

        case 6:if(nextch=='*') state = 7;
               else state = 6;
               break;

        case 7:if(nextch=='*') state = 7;
               else if(nextch==')') { state = 0; nextword.delete(0,nextword.length());lastch='\0';}

               else state = 6;
               break;

        case 8:if(nextch=='\'') state = 9;
               else if(nextch=='\r')
                {
                 lastch=nextch;
                 nextword.deleteCharAt(nextword.length()-1);
                 if(nextword.length()>1) lexerError();
                 code =  s_character;
                 tokenfound=true;
                }
               else state = 8;
               break;

        case 9:if(nextch=='\'') state = 8;
               else {
                 code = s_character;
                 lastch=nextch;
                 length=nextword.length();
                 nextword.deleteCharAt(nextword.length()-1);
                 tokenfound=true;
               }
               break;

        case 10:if(nextch=='>') {
                                  code =  s_NE;
                                  tokenfound=true;
                                 }
                else if(nextch=='=') {
                                      code =  s_LTE;
                                      tokenfound=true;
                                      }
                else {
                      code =  s_LT;
                      lastch=nextch;
                      tokenfound=true;
                      length=nextword.length();
                      nextword.deleteCharAt(nextword.length()-1);
                      }
                break;

        case 11:if(nextch=='=') {
                                  tokenfound=true;
                                  code =  s_GTE;
                                }
                else {
                code =  s_GT;
                 lastch=nextch;
                 tokenfound=true;
                 length=nextword.length();
                 nextword.deleteCharAt(nextword.length()-1);
               }
                break;

        case 12:if(nextch=='='){
                               code =  s_assign;
                               tokenfound=true;
                               }
                else {
                code =  s_colon;
                nextword.deleteCharAt(nextword.length()-1);
                 lastch=nextch;
                 tokenfound=true;
               }
                break;

        case 13:if(nextch=='}'){ state = 0; nextword.delete(0,nextword.length());}

                else state = 13;
                break;

        }//end of switch
        if(tokenfound==true)
          {
         length=nextword.length();
         tmpnextword.delete(0,tmpnextword.length());
         tmpnextword.append(nextword.toString());
         nextword.delete(0,nextword.length());
          break;
          }

    }while(!eofFlag);//end of while

   return makeToken(code,length,r,c-length,0,tmpnextword);

 }///end of method NextSymbol()*/

  public int iskeyword(StringBuffer lexicalWord)
  {
     int i=lexicalWord.length();
     int r=16;
     lexicalWord.deleteCharAt(i-1);
     if (lexicalWord.toString().equalsIgnoreCase("begin"))r=3;
     if (lexicalWord.toString().equalsIgnoreCase("case"))r=4;
     if (lexicalWord.toString().equalsIgnoreCase("char"))r=5;
     if (lexicalWord.toString().equalsIgnoreCase("do"))r=10;
     if (lexicalWord.toString().equalsIgnoreCase("end"))r=12;
     if (lexicalWord.toString().equalsIgnoreCase("for"))r=14;
     if (lexicalWord.toString().equalsIgnoreCase("function"))r=15;
     if (lexicalWord.toString().equalsIgnoreCase("if"))r=17;
     if (lexicalWord.toString().equalsIgnoreCase("integer"))r=18;
     if (lexicalWord.toString().equalsIgnoreCase("procedure"))r=23;
     if (lexicalWord.toString().equalsIgnoreCase("program"))r=24;
     if (lexicalWord.toString().equalsIgnoreCase("repeat"))r=25;
     if (lexicalWord.toString().equalsIgnoreCase("then"))r=29;
     if (lexicalWord.toString().equalsIgnoreCase("until"))r=30;
     if (lexicalWord.toString().equalsIgnoreCase("var"))r=31;
     if (lexicalWord.toString().equalsIgnoreCase("while"))r=32;
     if (lexicalWord.toString().equalsIgnoreCase("type"))r=41;
     if (lexicalWord.toString().equalsIgnoreCase("real"))r=42;
     if (lexicalWord.toString().equalsIgnoreCase("not"))r=43;
     if (lexicalWord.toString().equalsIgnoreCase("const"))r=44;
     if (lexicalWord.toString().equalsIgnoreCase("and"))r=2;
     if (lexicalWord.toString().equalsIgnoreCase("or"))r=22;
     if (lexicalWord.toString().equalsIgnoreCase("of"))r=45;
     if (lexicalWord.toString().equalsIgnoreCase("to"))r=46;
     if (lexicalWord.toString().equalsIgnoreCase("else"))r=47;
     return r;
  }
  public Lexer()
  {
    r=1;
    c=0;
    selection=0;
    openfile();
  }
  public Lexer(String fPath)
  {
    r=1;
    c=0;
    selection=0;
    openfile(fPath);
  }
  public void closefile()
  {
   try{
    fReader.close();
   }
   catch(IOException e) {
   }


  }
}