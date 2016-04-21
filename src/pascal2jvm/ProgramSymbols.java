package pascal2jvm;

/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */
import java.*;
public class ProgramSymbols {
  public int code;
  public String name;
  public boolean exist;
  public ProgramSymbols(int c,String str) {
    code = c;
    name = str;
    exist = false;

  }
  public int getCode(){return code;}
  public String getName(){return name;}
  public void setCode(int c){code = c;}
  public void setName(String str){name=str;}
}