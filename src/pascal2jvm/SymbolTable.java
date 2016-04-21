package pascal2jvm;

import java.util.Hashtable;
import java.lang.*;



/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */

public class SymbolTable{
  int endPos=0;
  SymTab symb;
  public SymbolTable(){
  symb=new SymTab();
  }
  public boolean insertVar(Token T,ProgramSymbols Group,ProgramSymbols Type){
  if(Type.getName()=="S_char")endPos=endPos+1;
  if(Type.getName()=="S_integer")endPos=endPos+2;
  if(Type.getName()=="S_real")endPos=endPos+4;
  if(symb.containsKey(T.lexeme)==true){
                                        scemanticError(T,1);
                                        return(false);
                                        }
  else {
        symb.put(T.lexeme,endPos,"",Group,Type);
        return(true);
        }
  }
  public boolean insertConst(Token T,String V,ProgramSymbols Group,ProgramSymbols Type){
  if(symb.containsKey(T.lexeme)==true){scemanticError(T,1);
                                        return(false);
                                        }
  else {symb.put(T.lexeme,0,V,Group,Type);
        return (true);}
  }
  public void scemanticError(Token T,int i){
/*  if(i==1)emitln("Scemantic Error:Duplicate declaration " + T.lexeme +" at row " + T.row + ", col "+T.col);
  if(i==2)emitln("Scemantic Error:Not declarated " + T.lexeme +" at row " + T.row + ", col "+T.col);*/
  }
  public ProgramSymbols lookUp(Token T){
  ProgramSymbols Outing=new ProgramSymbols(-1,"S_error");
  if(symb.containsKey(T.lexeme)==false){
      scemanticError(T,2);
      return(Outing);
      }
  else{
      return(symb.getSymType(T.lexeme));
      }
  }
  public StringBuffer lookUp(String ConName){
  String Temp=new String();
  StringBuffer Outing=new StringBuffer();
  ProgramSymbols ConType=new ProgramSymbols(-1,"S_error");
  ConType=symb.getSymType(ConName);
  Temp=symb.getSymVal(ConName);
  Outing.append(Temp);
  return(Outing);
  }
  public boolean remove(Token T){
  if(symb.containsKey(T.lexeme)==false)scemanticError(T,2);
  else symb.remove(T.lexeme);
  return (true);
  }
  public void clearTable(){
  symb.clear();
  }
  public ProgramSymbols getGroup(Token T){
  ProgramSymbols outing=new ProgramSymbols(-1,"S_error");
  if(symb.containsKey(T.lexeme)==false){
      scemanticError(T,2);
      return(outing);
      }
  else{
      outing=symb.getSymGroup(T.lexeme);
      return(outing);
      }

  }
}