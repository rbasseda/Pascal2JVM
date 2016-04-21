package pascal2jvm;
import java.util.*;

/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */

public class SymTab
  {
//  public String SymName=new String();
//  public int Offset=0;
//  public String Value="";
//  private program_symbols Group;//Group= Const=1;Type=2;Var=3;Function=4;
//  private program_symbols Type;//Char=1;Integer=2;Real=3;
  private HashMap<String,Integer> off;
  private HashMap<String,String> val;
  private HashMap<String,ProgramSymbols> grp;
  private HashMap<String,ProgramSymbols> typ;
  public boolean containsKey(String Name){
  return (typ.containsKey(Name));
  }
  public void put(String Name,int offset,String value,ProgramSymbols group,ProgramSymbols typeOfSymbol){
    off.put(Name,new Integer(offset));
    val.put(Name,value);
    grp.put(Name,group);
    typ.put(Name,typeOfSymbol);
  }
  public SymTab(){
  off=new HashMap<String,Integer>();
  val=new HashMap<String,String>();
  grp=new HashMap<String,ProgramSymbols>();
  typ=new HashMap<String,ProgramSymbols>();
  }
  public String getSymVal(String Name){
  return((String)val.get(Name) );
  }
  public int getSymOff(String Name){
  return(((Integer)off.get(Name)).intValue());
  }
  public ProgramSymbols getSymType(String Name){
  ProgramSymbols myType=new ProgramSymbols(-1,"S_error");
  String temp=new String();
  temp= typ.get(Name).getName();
  if (temp=="S_integer"){
                         myType.setCode(18);
                         myType.setName(temp);
                         }
  if (temp=="S_char"){
                         myType.setCode(5);
                         myType.setName(temp);
                         }
  if (temp=="S_real"){
                         myType.setCode(42);
                         myType.setName(temp);
                         }
  return(myType);
  }
  public ProgramSymbols getSymGroup(String Name){
  ProgramSymbols group=new ProgramSymbols(-1,"S_error");
  String temp=new String();
  temp= grp.get(Name).getName();
  if (temp=="S_const"){
                         group.setCode(44);
                         group.setName(temp);
                         }
  if (temp=="S_type"){
                         group.setCode(41);
                         group.setName(temp);
                         }
  if (temp=="S_var"){
                         group.setCode(31);
                         group.setName(temp);
                         }
  return(group);
  }
  public void remove(String Name){
  off.remove(Name);
  val.remove(Name);
  grp.remove(Name);
  typ.remove(Name);
  }
  public void clear(){
  off.clear();
  val.clear();
  grp.clear();
  typ.clear();
  }
}