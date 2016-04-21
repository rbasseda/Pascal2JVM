package pascal2jvm;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import java.lang.Object;
import java.util.*;
/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */


public class SyntaxAnalyser {
  Token currentToken;
  ProgramSymbols currentSymbol;
  SymbolTable symTbl,procSymTbl;
  Lexer myLexer;
  SetOfSymbols stops;
  boolean syntaxErrFlg;
  int tempNo,labelNo;
  StringBuffer output = new StringBuffer();
  final static int s_error=-1;
  final static int s_char=5;
  final static int s_integer=18;
  final static int s_real=42;
//--------------------------GENERAL FUNCTIONS-----------------------
  //--------------Initializer-------------
  public SyntaxAnalyser(Lexer L) {
  myLexer=L;
  currentToken = new Token(1,1,1,1,1,"S");
  procSymTbl=new SymbolTable();
  symTbl=new SymbolTable();
  output.delete(0,output.length());
  stops=new SetOfSymbols();
  nextSymbol();
  stops.add("S_eof");
  programPas();
  stops.del("S_eof");
  }
  public String getJasType(String type)
  {
    if(type=="S_integer") return "I";
    if(type=="S_real") return "F";
    if(type=="S_char") return "C";
    return "N";
  }

  public void nextSymbol(){
  currentToken=myLexer.nextSymbol();
  currentSymbol=currentToken.symbol;
  }

  public void emitln(String str){
  if(syntaxErrFlg==false)
   {
    output.append(str);
    output.append("\r");
    output.append("\n");
    }
  }
  public void emit(String str){
  if(syntaxErrFlg==false)
   {
    output.append(str);
    }
  }

  public StringBuffer newTemp(){
  StringBuffer Temp=new StringBuffer();
  tempNo++;
  Temp.delete(0,Temp.length());
  Temp.append("T"+tempNo);
  return(Temp);
  }

  public void remTemp(){
  tempNo--;
  }

  public StringBuffer newLabel(){
  StringBuffer temp=new StringBuffer();
  labelNo++;
  temp.delete(0,temp.length());
  temp.append("L" + labelNo);
  return(temp);
  }

  public void syntaxError(String str){
  emitln("Syntax Error:Expect " + str +" at row " + currentToken.row + ", col "+currentToken.col);
  while ( !stops.inSet( currentSymbol.getName() ) )
    nextSymbol();
  }
  public void scemanticComplementError(Token T,int i){
  if(i==1)emitln("Scemantic Error:Duplicate declaration " + T.lexeme +" at row " + T.row + ", col "+T.col);
  if(i==2)emitln("Scemantic Error:Not declarated " + T.lexeme +" at row " + T.row + ", col "+T.col);
  if(i==3)emitln("Scemantic Error:Not compatible  at row " + T.row + ", col "+T.col);
  if(i==4)emitln("Scemantic Error:Not changeable assignment to " + T.lexeme +" at row " + T.row + ", col "+T.col);
  if(i==5)emitln("Scemantic Error:Not defined oprator to character in " + T.lexeme +" at row " + T.row + ", col "+T.col);
  if(i==6)emitln("Scemantic Error:Not defined oprator to float in " + T.lexeme +" at row " + T.row + ", col "+T.col);
  }

  public void expect(String expected_symbol){
  if (currentSymbol.getName() == expected_symbol)
    nextSymbol();
    else
      syntaxError(expected_symbol);
  }
//------------------PROGRAM PASCAL-------------------
 /* ProgramPas -> program id ; BlockBody .  */
  public void programPas(){
    String programname=currentToken.lexeme;
  //Call expect
  stops.add("S_identifier");
 stops.add("S_semicolon");

  //Add First Block Body
  stops.add("S_const");  //First ConstantDefPart
  stops.add("S_type");   //First TypeDefPart
  stops.add("S_var");    //First VarDefPart
  stops.add("S_function");   //First FunctionDef
  stops.add("S_procedure");  //First ProcedureDef
  stops.add("S_begin");      //First CompoundSt
  stops.add("S_dot");
  expect("S_program");
  stops.del("S_identifier");
  //End of Call
  //Call expect
  expect("S_identifier");
  stops.del("S_semicolon");
  //End of Call
  //Call expect
  //-----------------------------
  emitln (".Class Public "+programname);
  emitln(".super java/lang/Object");
//-----------------------------

  expect("S_semicolon");
  //del First Block Body
  stops.del("S_const");  //First ConstantDefPart
  stops.del("S_type");   //First TypeDefPart
  stops.del("S_var");    //First VarDefPart
  stops.del("S_function");   //First FunctionDef
  stops.del("S_procedure");  //First ProcedureDef
  stops.del("S_begin");      //First CompoundSt
  //End of Call
  //Call BlockBody
  blockBody();
  stops.del("S_dot");
  //End of Call
  emitln("return   ");
  emitln(".end method");

  expect("S_dot");
  }//End of ProgramPas

 /* BlockBody -> [ConstantDefPart][TypeDefPart][VarDefPart]
                 { FunctionDef | ProcedureDef } CompoundSt   */
  public void blockBody(){
    if(currentSymbol.getName()=="S_const")
    {//Call ConstDefPart
     stops.add("S_type");   //First TypeDefPart
     stops.add("S_var");    //First VarDefPart
     stops.add("S_function");   //First FunctionDef
     stops.add("S_procedure");  //First ProcedureDef
     stops.add("S_begin");      //First CompoundSt
     constDefPart();
     stops.del("S_type");   //First TypeDefPart
     stops.del("S_var");    //First VarDefPart
     stops.del("S_function");   //First FunctionDef
     stops.del("S_procedure");  //First ProcedureDef
     stops.del("S_begin");      //First CompoundSt
     }
  if(currentSymbol.getName()=="S_type")
    {//Call TypeDefPart
     stops.add("S_var");    //First VarDefPart
     stops.add("S_function");   //First FunctionDef
     stops.add("S_procedure");  //First ProcedureDef
     stops.add("S_begin");      //First CompoundSt
     typeDefPart();
     stops.del("S_var");    //First VarDefPart
     stops.del("S_function");   //First FunctionDef
     stops.del("S_procedure");  //First ProcedureDef
     stops.del("S_begin");      //First CompoundSt
     }
  if(currentSymbol.getName()=="S_var")
    {//Call VarDefPart
     stops.add("S_function");   //First FunctionDef
     stops.add("S_procedure");  //First ProcedureDef
     stops.add("S_begin");      //First CompoundSt
     varDefPart();
     stops.del("S_function");   //First FunctionDef
     stops.del("S_procedure");  //First ProcedureDef
     stops.del("S_begin");      //First CompoundSt
     }
  while(currentSymbol.getName()=="S_procedure")
    {stops.add("S_function");   //First FunctionDef
     stops.add("S_procedure");  //First ProcedureDef
     stops.add("S_begin");      //First CompoundSt
     procDef();
     stops.del("S_function");   //First FunctionDef
     stops.del("S_procedure");  //First ProcedureDef
     stops.del("S_begin");      //First CompoundSt
     }//End of while */

     emitln(".method public <init>()V");
     emitln("aload_0");
     emitln("invokenonvirtual java/lang/Object/<init>()V");
     emitln("return");
     emitln(".end method");
     emitln(".method public static main([Ljava/lang/String;)V");
     emitln(".limit stack 50   ");
     emitln(".limit locals 50");

     compoundSt();
  }//End of BlockBody
  /* ConstDefPart -> const id=(No|id|Float);{id=(No|id|Float);} */
  public void constDefPart(){
  ProgramSymbols grp=new ProgramSymbols(44,"S_const");
  ProgramSymbols typ=new ProgramSymbols(42,"S_real");
  Token ConstID;
  //Call expect const
  stops.add("S_identifier");
  expect("S_const");
  stops.del("S_identifier");
  //End of expect
  //Call expect id
  ConstID=currentToken;
  stops.add("S_equal");
  expect("S_identifier");
  stops.del("S_equal");
  //End of expect
  //Call expect equal
  stops.add("S_number");
  stops.add("S_identifier");
  expect("S_equal");
  stops.del("S_number");
  stops.del("S_identifier");
  //End of expect
  //Call expect  identifier or number
  stops.add("S_semicolon");
  if(currentSymbol.getName()=="S_number"){
                                            typ.setCode(18);
                                            typ.setName("S_integer");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" I ="+currentToken.lexeme);
                                            nextSymbol();
                                            }
    else{   if(currentSymbol.getName()=="S_float"){
                                            typ.setCode(42);
                                            typ.setName("S_real");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" F ="+currentToken.lexeme);
                                            nextSymbol();
                                            }
            else  {
                                            typ.setCode(18);
                                            typ.setName("S_char");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" C ="+currentToken.lexeme);
                                            expect("S_identifier");
                                            }
        }
  stops.del("S_semicolon");
  //End of expect
  //Call expect semicolon
  stops.add("S_identifier");
  expect("S_semicolon");
  stops.del("S_identifier");
  //End of expect
  while(currentSymbol.getName()=="S_identifier")
    {
     ConstID=currentToken;
     nextSymbol();
     //Call expect equal
     stops.add("S_number");
     stops.add("S_identifier");
     expect("S_equal");
     stops.del("S_number");
     stops.del("S_identifier");
     //End of expect
     //Call expect identifier or number
     stops.add("S_semicolon");
  if(currentSymbol.getName()=="S_number"){
                                            typ.setCode(18);
                                            typ.setName("S_integer");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" I ="+currentToken.lexeme);
                                            nextSymbol();
                                            }
    else{   if(currentSymbol.getName()=="S_float"){
                                            typ.setCode(42);
                                            typ.setName("S_real");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" F ="+currentToken.lexeme);
                                            nextSymbol();
                                            }
            else  {
                                            typ.setCode(18);
                                            typ.setName("S_char");
                                            if(symTbl.insertConst(ConstID,currentToken.lexeme,grp,typ)==false)
                                            scemanticComplementError(ConstID,1);
                                            emitln(".field public static "+ConstID.lexeme+" C ="+currentToken.lexeme);
                                            expect("S_identifier");
                                            }
        }
     stops.del("S_semicolon");
     //End of expect
     //Call expect semicolon
     stops.add("S_identifier");
     expect("S_semicolon");
     stops.del("S_identifier");
     //End of expect
    }
  }//End of ConstDefPart

  /* TypeDefPart -> type id=(integer|real|char);{id=(integer|real|char);} */
  public void typeDefPart(){
  ProgramSymbols Grp=new ProgramSymbols(41,"S_type");
  ProgramSymbols Typ=new ProgramSymbols(42,"S_real");
  Token TypeID;
  //Call expect type
  stops.add("S_identifier");
  expect("S_type");
  stops.del("S_identifier");
  //End of expect
  //Call expect id
  TypeID=currentToken;
  stops.add("S_equal");
  expect("S_identifier");
  stops.del("S_equal");
  //End of expect
  //Call expect equal
  stops.add("S_integer");
  stops.add("S_real");
  stops.add("S_char");
  expect("S_equal");
  stops.del("S_integer");
  stops.del("S_real");
  stops.del("S_char");
  //End of expect
  if(currentSymbol.getName()=="S_integer"){boolean ttt;
                                            Typ.setCode(18);
                                            Typ.setName("S_integer");
                                            ttt=symTbl.insertConst(TypeID,"",Grp,Typ);
                                            if(ttt==false)
                                            scemanticComplementError(TypeID,1);
                                            }
  if(currentSymbol.getName()=="S_real"){
                                            Typ.setCode(42);
                                            Typ.setName("S_real");
                                            if(symTbl.insertConst(TypeID,"",Grp,Typ)==false)
                                            scemanticComplementError(TypeID,1);
                                            }
  if(currentSymbol.getName()=="S_char"){
                                            Typ.setCode(5);
                                            Typ.setName("S_integer");
                                            if(symTbl.insertConst(TypeID,"",Grp,Typ)==false)
                                            scemanticComplementError(TypeID,1);
                                            }
  stops.add("S_semicolon");
  if((currentSymbol.getName()=="S_integer")||
     (currentSymbol.getName()=="S_real")||
     (currentSymbol.getName()=="S_char"))
     nextSymbol();
     else syntaxError("S_integer or S_real or S_char");
  stops.del("S_semicolon");
  //Call expect semicolon
  stops.add("S_identifier");
  expect("S_semicolon");
  stops.del("S_identifier");
  //End of expect
  while(currentSymbol.getName()=="S_identifier")
    {TypeID=currentToken;
     nextSymbol();
     //Call expect equal
     stops.add("S_integer");
     stops.add("S_real");
     stops.add("S_char");
     expect("S_equal");
     stops.del("S_integer");
     stops.del("S_real");
     stops.del("S_char");
     //End of expect
  if(currentSymbol.getName()=="S_integer"){
                                            Typ.setCode(18);
                                            Typ.setName("S_integer");
                                            if(symTbl.insertConst(TypeID,"",Grp,Typ)==false)
                                            scemanticComplementError(TypeID,1);
                                            }
  if(currentSymbol.getName()=="S_real"){
                                            Typ.setCode(42);
                                            Typ.setName("S_real");
                                            if(symTbl.insertConst(TypeID,"",Grp,Typ)==false)
                                            scemanticComplementError(TypeID,1);
                                            }
  if(currentSymbol.getName()=="S_char"){
                                            Typ.setCode(5);
                                            Typ.setName("S_integer");
                                            if(symTbl.insertConst(TypeID,"",Grp,Typ)==false)
                                            scemanticComplementError(TypeID,1);
                                            }
     stops.add("S_semicolon");
     if((currentSymbol.getName()=="S_integer")||
        (currentSymbol.getName()=="S_real")||
        (currentSymbol.getName()=="S_char"))
        nextSymbol();
        else syntaxError("S_integer or S_real or S_char");
     stops.del("S_semicolon");
     //Call expect semicolon
     stops.add("S_identifier");
     expect("S_semicolon");
     stops.del("S_identifier");
     //End of expect
    }
  }//End of TypeDefPart

  /* VarDefPart -> var id{,id}:(id|char|real|integer);{id{,id}:(id|char|real|integer);} */
  public void varDefPart(){
  ProgramSymbols Grp=new ProgramSymbols(31,"S_var");
  ProgramSymbols Typ=new ProgramSymbols(42,"S_real");
  Token VarID;
  Stack VarIDs=new Stack();
  //Call expect var
  stops.add("S_identifier");
  expect("S_var");
  stops.del("S_identifier");
  //End of expect
  //Call expect id
  VarIDs.push(currentToken);
  stops.add("S_comma");
  stops.add("S_colon");
  expect("S_identifier");
  stops.del("S_comma");
  stops.del("S_colon");
  //End of expect
  while(currentSymbol.getName()=="S_comma")
    {nextSymbol();
     VarIDs.push(currentToken);
     //Call expect id
     stops.add("S_comma");
     stops.add("S_colon");
     expect("S_identifier");
     stops.del("S_comma");
     stops.del("S_colon");
     //End of expect
     }//End of while
  //Call expect colon
  stops.add("S_identifier");
  stops.add("S_integer");
  stops.add("S_real");
  stops.add("S_char");
  expect("S_colon");
  stops.del("S_identifier");
  stops.del("S_integer");
  stops.del("S_real");
  stops.del("S_char");
  while(VarIDs.empty() !=true){
  VarID=(Token)VarIDs.pop();
  if(symTbl.insertVar(VarID,Grp,currentSymbol)==false)
  scemanticComplementError(VarID,1);
  emitln(".field public  "+VarID.lexeme+" "+getJasType(currentSymbol.getName()));

  }
  //End of expect
  //Call expect id or integer or real or char
  stops.add("S_semicolon");
  if((currentSymbol.getName()=="S_identifier")||
     (currentSymbol.getName()=="S_integer")||
     (currentSymbol.getName()=="S_real")||
     (currentSymbol.getName()=="S_char"))
     nextSymbol();
     else syntaxError("S_identifier or S_integer or S_real or S_char");
  stops.del("S_semicolon");
  //End of expect
  //Call expect semicolon
  stops.add("S_identifier");
  expect("S_semicolon");
  stops.del("S_identifier");
  //End of expect
  while(currentSymbol.getName()=="S_identifier")
    {
     VarIDs.push(currentToken);
     nextSymbol();
    while(currentSymbol.getName()=="S_comma")
       {nextSymbol();
       VarIDs.push(currentToken);
       //Call expect id
       stops.add("S_comma");
       stops.add("S_colon");
       expect("S_identifier");
       stops.del("S_comma");
       stops.del("S_colon");
       //End of expect
       }//End of inner while
     //Call expect colon
     stops.add("S_identifier");
     stops.add("S_integer");
     stops.add("S_real");
     stops.add("S_char");
     expect("S_colon");
     stops.del("S_identifier");
     stops.del("S_integer");
     stops.del("S_real");
     stops.del("S_char");
     while(VarIDs.empty() !=true){
     VarID=(Token)VarIDs.pop();
     if(symTbl.insertVar(VarID,Grp,currentSymbol)==false)
     scemanticComplementError(VarID,1);
     emitln(".field public  "+VarID.lexeme+" "+getJasType(currentSymbol.getName()));

     }
     //End of expect
     //Call expect id or integer or real or char
     stops.add("S_semicolon");
     if((currentSymbol.getName()=="S_identifier")||
        (currentSymbol.getName()=="S_integer")||
        (currentSymbol.getName()=="S_real")||
        (currentSymbol.getName()=="S_char"))
        nextSymbol();
        else syntaxError("S_identifier or S_integer or S_real or S_char");
     stops.del("S_semicolon");
     //End of expect
     //Call expect semicolon
     stops.add("S_identifier");
     expect("S_semicolon");
     stops.del("S_identifier");
     //End of expect
     }//End of outer while
  }//End of VarDefPart
/* ProcVarDefPart -> var id{,id}:(id|char|real|integer);{id{,id}:(id|char|real|integer);} */
  public void procVarDefPart(){
  procSymTbl.clearTable();
  ProgramSymbols Grp=new ProgramSymbols(31,"S_var");
  ProgramSymbols Typ=new ProgramSymbols(42,"S_real");
  Token VarID;
  Stack VarIDs=new Stack();
  //Call expect var
  stops.add("S_identifier");
  expect("S_var");
  stops.del("S_identifier");
  //End of expect
  //Call expect id
  VarIDs.push(currentToken);
  stops.add("S_comma");
  stops.add("S_colon");
  expect("S_identifier");
  stops.del("S_comma");
  stops.del("S_colon");
  //End of expect
  while(currentSymbol.getName()=="S_comma")
    {nextSymbol();
     VarIDs.push(currentToken);
     //Call expect id
     stops.add("S_comma");
     stops.add("S_colon");
     expect("S_identifier");
     stops.del("S_comma");
     stops.del("S_colon");
     //End of expect
     }//End of while
  //Call expect colon
  stops.add("S_identifier");
  stops.add("S_integer");
  stops.add("S_real");
  stops.add("S_char");
  expect("S_colon");
  stops.del("S_identifier");
  stops.del("S_integer");
  stops.del("S_real");
  stops.del("S_char");
  while(VarIDs.empty() !=true){
  VarID=(Token)VarIDs.pop();
  if(procSymTbl.insertVar(VarID,Grp,currentSymbol)==false)
  scemanticComplementError(VarID,1);
  }
  //End of expect
  //Call expect id or integer or real or char
  stops.add("S_semicolon");
  if((currentSymbol.getName()=="S_identifier")||
     (currentSymbol.getName()=="S_integer")||
     (currentSymbol.getName()=="S_real")||
     (currentSymbol.getName()=="S_char"))
     nextSymbol();
     else syntaxError("S_identifier or S_integer or S_real or S_char");
  stops.del("S_semicolon");
  //End of expect
  //Call expect semicolon
  stops.add("S_identifier");
  expect("S_semicolon");
  stops.del("S_identifier");
  //End of expect
  while(currentSymbol.getName()=="S_identifier")
    {
     VarIDs.push(currentToken);
     nextSymbol();
    while(currentSymbol.getName()=="S_comma")
       {nextSymbol();
       VarIDs.push(currentToken);
       //Call expect id
       stops.add("S_comma");
       stops.add("S_colon");
       expect("S_identifier");
       stops.del("S_comma");
       stops.del("S_colon");
       //End of expect
       }//End of inner while
     //Call expect colon
     stops.add("S_identifier");
     stops.add("S_integer");
     stops.add("S_real");
     stops.add("S_char");
     expect("S_colon");
     stops.del("S_identifier");
     stops.del("S_integer");
     stops.del("S_real");
     stops.del("S_char");
     while(VarIDs.empty() !=true){
     VarID=(Token)VarIDs.pop();
     if(procSymTbl.insertVar(VarID,Grp,currentSymbol)==false)
     scemanticComplementError(VarID,1);
     }
     //End of expect
     //Call expect id or integer or real or char
     stops.add("S_semicolon");
     if((currentSymbol.getName()=="S_identifier")||
        (currentSymbol.getName()=="S_integer")||
        (currentSymbol.getName()=="S_real")||
        (currentSymbol.getName()=="S_char"))
        nextSymbol();
        else syntaxError("S_identifier or S_integer or S_real or S_char");
     stops.del("S_semicolon");
     //End of expect
     //Call expect semicolon
     stops.add("S_identifier");
     expect("S_semicolon");
     stops.del("S_identifier");
     //End of expect
     }//End of outer while
  }//End of VarDefPart
//*********************************************************************
  /* CompoundSt ->  begin {LabeledSt} end */
  public void compoundSt(){
  //Call expect begin
  //Add First LabeledSt
  stops.add("S_if");//First IfSt
  stops.add("S_while");//First WhileSt
  stops.add("S_begin");//First CompoundSt
  stops.add("S_identifier");
  stops.add("S_case");
  stops.add("S_for");
  stops.add("S_end");
  expect("S_begin");
  //End of Call
  while((currentSymbol.getName()=="S_if")||
        (currentSymbol.getName()=="S_while")||
        (currentSymbol.getName()=="S_begin")||
        (currentSymbol.getName()=="S_identifier")||
        (currentSymbol.getName()=="S_case")||
        (currentSymbol.getName()=="S_for")
        )
    {
     labeledSt();
     }
  stops.del("S_if");//First IfSt
  stops.del("S_while");//First WhileSt
  stops.del("S_begin");//First CompoundSt
  stops.del("S_identifier");
  stops.del("S_case");
  stops.del("S_for");
  stops.del("S_end");
  expect("S_end");
  }//End of CompoundSt
//*******************************************************************************************8

  /* LabeledSt ->  id( :LabeledSt | (Params); | :=E ;) | IfSt | WhileSt | CompoundSt; | CaseSt;|ForSt; */
  public void labeledSt(){
  Token  currentA_token;
  currentA_token=currentToken;
  if(currentSymbol.getName()=="S_identifier")
    {String ResA;
     ResA = currentToken.lexeme;
     nextSymbol();
     if(currentSymbol.getName()=="S_colon")
       {nextSymbol();
        emitln(ResA+":");
        labeledSt();
        }
     else if(currentSymbol.getName()=="S_openpar")
       {nextSymbol();
        stops.add("S_closepar");
        stops.add("S_semicolon");
        StringBuffer Vars[]=new StringBuffer[30];
        for (int i=0;i<30;i++)
          {
           Vars[i]=new StringBuffer();
           }
        MyInt varcount=new MyInt(0);
        params(Vars,varcount);
        emitln("CALL  " + ResA);
        for(int i=0;i<=varcount.get();i++)
          {
           emitln("POP  "+Vars[i].toString());
           }
        stops.del("S_closepar");
        //Call expect closepar
        expect("S_closepar");
        stops.del("S_semicolon");
        expect("S_semicolon");
        //End of Call
        }
     else if(currentSymbol.getName()=="S_assign")
       {
       ProgramSymbols TypeA=new ProgramSymbols(-1,"S_error");
       ProgramSymbols TypeE=new ProgramSymbols(-1,"S_error");
       nextSymbol();
       //Call E
       stops.add("S_semicolon");
       emitln("aload_0");
       eE(TypeE);
       stops.del("S_semicolon");
       //End of Call
       TypeA=symTbl.lookUp(currentA_token);
       if(TypeA.getName()=="S_error")scemanticComplementError(currentA_token,2);
       else if(isChangeable(currentA_token)==false)scemanticComplementError(currentA_token,4);
       //       if(IsCompatible(TypeA,TypeE).get_name() !=TypeA.get_name() ) ScemanticComplementError(currentA_token,3);
       if(TypeA.getName()!="S_real"&&TypeA.getName()!="S_integer") scemanticComplementError(currentA_token,3);
       if ((TypeA.getName()=="S_real")&&(TypeE.getName()=="S_integer"))
          emitln("i2f");
       if ((TypeA.getName()=="S_integer")&&(TypeE.getName()=="S_real"))
          emitln("f2i");
       emitln("putfield /"+ResA+" "+getJasType(TypeE.getName()));
       expect("S_semicolon");
        }
     else syntaxError("S_colon or S_assign or S_openpar");
     }//End of outer if
  else if(currentSymbol.getName()=="S_if")
    {//Call IfSt
     ifSt();
     }//End of if
  else if(currentSymbol.getName()=="S_while")
    {//Call WhileSt
     whileSt();
     }//End of While
  else if(currentSymbol.getName()=="S_begin")
    {//Call CompoundSt
     stops.add("S_semicolon");
     compoundSt();
     stops.del("S_semicolon");
     expect("S_semicolon");
     }//End of Compound
  else if(currentSymbol.getName()=="S_case")
    {//Call CaseSt
     stops.add("S_semicolon");
     caseSt();
     stops.del("S_semicolon");
     expect("S_semicolon");
     }//End of Case
  else if(currentSymbol.getName()=="S_for")
    {//Call ForSt
     stops.add("S_semicolon");
     forSt();
     stops.del("S_semicolon");
     }//End of ForSt
  else syntaxError("identifier or if or while or begin or case or for");
  }//End of LabeledSt


//----------------------IF PART---------------------
  /*  IfSt -> if Condition then LabeledSt [else LabeledSt] */
  public void ifSt(){
  StringBuffer Lelse=new StringBuffer();
  StringBuffer Lend=new StringBuffer();
  //Call expect
  //Adding First Condition
  stops.add("S_not");
  stops.add("S_openpar");
  stops.add("S_identifier");
  stops.add("S_number");
  stops.add("S_then");
  //Adding First LabeledSt
  stops.add("S_if");  //First IfSt
  stops.add("S_while");  //First WhileSt
  stops.add("S_begin");  //First CompoundSt
//stops.add("S_identifier");  //First AssSt&CallSt
  stops.add("S_else");
  expect("S_if");
  stops.del("S_not");
  stops.del("S_openpar");
  stops.del("S_number");
  //End of call
  condition();
  stops.del("S_then");
  Lelse = newLabel();
  emitln("ifeq "+Lelse);
  //Call expect
  expect("S_then");
  //del First LabeledSt
  stops.del("S_if");  //First IfSt
  stops.del("S_while");  //First WhileSt
  stops.del("S_begin");  //First CompoundSt
  stops.del("S_identifier");  //First AssSt&CallSt
  //End of call
  //----------Call LabeledSt-----------
  labeledSt();
  stops.del("S_else");
  //-------End of call LabeledSt-------
  if(currentSymbol.getName()=="S_else")
    {nextSymbol();
     Lend = newLabel();
     emitln("goto "+Lend);
     emitln(Lelse+":");    //print: Lelse:
     labeledSt();
     emitln(Lend+":");     //Print: Lend:
     }
     else
       emitln(Lelse+":");   //print: Lelse:
  }// end of IfSt
//--------------------  Condition  ------------------------
  /*  Condition -> B1 { or B1 }  */
  public void condition(){
  //Call B1
  stops.add("S_or");
  bB1();
  stops.del("S_or");
  //End of Call
  while(currentSymbol.getName()=="S_or")
    {nextSymbol();
     //Call B1
     stops.add("S_or");
     bB1();
     stops.del("S_or");
     //End Of Call
     emitln("ior");
    }//End of While
  }//End of Condition
//--------------------------------------------------
  /*  B1 -> B { and B }   */
  public void bB1(){
  //Call B
  stops.add("S_and");
  bB();
  stops.del("S_and");
  //End of Call
  while(currentSymbol.getName()=="S_and")
    {nextSymbol();
     //Call B
     stops.add("S_and");
     bB();
     stops.del("S_and");
     //End Of Call
     emitln("iand");
     }//End of While
  }//End of B1
//---------------------------------------------------------

  /*  B -> not B | E ( < | <= | > | >= | <> | = ) E  */
  public void bB(){
  Token currentE_token;
  StringBuffer Ltrue=new StringBuffer();
  StringBuffer Lend=new StringBuffer();
  ProgramSymbols TypeE1=new ProgramSymbols(-1,"S_error");
  ProgramSymbols TypeE2=new ProgramSymbols(-1,"S_error");
  ProgramSymbols relop;
  Ltrue = newLabel();
  Lend = newLabel();
  if(currentSymbol.getName()=="S_not")
    {nextSymbol();
     bB();
     emitln("ifeq "+Ltrue);
     }
    else
    {//Call E
     stops.add("S_LT");
     stops.add("S_LTE");
     stops.add("S_GT");
     stops.add("S_GTE");
     stops.add("S_NE");
     stops.add("S_equal");
     //add First E
     currentE_token=currentToken;
     stops.add("S_not");
     stops.add("S_openpar");
     stops.add("S_identifier");
     stops.add("S_character");
     eE(TypeE1);
     stops.del("S_LT");
     stops.del("S_LTE");
     stops.del("S_GT");
     stops.del("S_GTE");
     stops.del("S_NE");
     stops.del("S_equal");
     //End of Call
     relop = currentSymbol;
     if((currentSymbol.getName()=="S_LT")||
        (currentSymbol.getName()=="S_LTE")||
        (currentSymbol.getName()=="S_GT")||
        (currentSymbol.getName()=="S_GTE")||
        (currentSymbol.getName()=="S_NE")||
        (currentSymbol.getName()=="S_equal"))
        nextSymbol();
        else //Syntax Error
          {
           syntaxError("Relop");
           }
     stops.del("S_not");
     stops.del("S_openpar");
     stops.del("S_identifier");
     stops.del("S_character");
     eE(TypeE2);
     isCompatible(TypeE1,TypeE2,TypeE1);
     if(TypeE1.getName()=="S_error")scemanticComplementError(currentE_token,3);
     else if(TypeE1.getName()=="S_integer")
       {
        if (relop.getName()=="S_LT")
          emitln("if_icmplt "+Ltrue);
        else if (relop.getName()=="S_LTE")
          emitln("if_icmple "+Ltrue);
        else if (relop.getName()=="S_GT")
          emitln("if_icmpgt "+Ltrue);
        else if (relop.getName()=="S_GTE")
          emitln("if_icmpge "+Ltrue);
        else if (relop.getName()=="S_NE")
          emitln("if_icmpne "+Ltrue);
        else if (relop.getName()=="S_equal")
          emitln("if_icmpeq "+Ltrue);
        }
     else if(TypeE1.getName()=="S_real")
       {
        if (relop.getName()=="S_LT")
          emitln("if_fcmplt "+Ltrue);
        else if (relop.getName()=="S_LTE")
          emitln("if_fcmple "+Ltrue);
        else if (relop.getName()=="S_GT")
          emitln("if_fcmpgt "+Ltrue);
        else if (relop.getName()=="S_GTE")
          emitln("if_fcmpge "+Ltrue);
        else if (relop.getName()=="S_NE")
          emitln("if_fcmpne "+Ltrue);
        else if (relop.getName()=="S_equal")
          emitln("if_fcmpeq "+Ltrue);
        }
     else if(TypeE1.getName()=="S_char")
       {
        if (relop.getName()=="S_LT")
          emitln("if_icmplt "+Ltrue);
        else if (relop.getName()=="S_LTE")
          emitln("if_icmple "+Ltrue);
        else if (relop.getName()=="S_GT")
          emitln("if_icmpgt "+Ltrue);
        else if (relop.getName()=="S_GTE")
          emitln("if_icmpge "+Ltrue);
        else if (relop.getName()=="S_NE")
          emitln("if_icmpne "+Ltrue);
        else if (relop.getName()=="S_equal")
          emitln("if_icmpeq "+Ltrue);
        }
     }//End of outer else
  emitln("sipush 0");
  emitln("goto "+Lend);
  emitln(Ltrue+":");            //print: Ltrue:
  emitln("sipush 1");
  emitln(Lend+":");
  }//End of B

//----------------------END OF IF PART---------------------

//----------------------FOR PART---------------------
  /*  ForSt -> for id:=E to E do LabeledSt */
  public void forSt(){
  Token currentA_token;
  ProgramSymbols TypeE=new ProgramSymbols(-1,"S_error");
  ProgramSymbols TypeA=new ProgramSymbols(-1,"S_error");
  String ResA;
  StringBuffer Temp=new StringBuffer();
  StringBuffer Ls=new StringBuffer();
  StringBuffer Le=new StringBuffer();
  //Call expect S_for
  stops.add("S_identifier");
  stops.add("S_assign");
  //add First E
  stops.add("S_not");
  stops.add("S_openpar");
  //stops.add("S_identifier");
  stops.add("S_character");
  stops.add("S_to");
  stops.add("S_do");
  //Adding First LabeledSt
  stops.add("S_if");  //First IfSt
  stops.add("S_while");  //First WhileSt
  stops.add("S_begin");  //First CompoundSt
  stops.add("S_identifier");  //First AssSt&CallSt
  expect("S_for");
  //End of Call
  currentA_token=currentToken;
  ResA = currentToken.lexeme;
//********************************************************************
  stops.add("S_to");
  asSt(TypeA);
  stops.del("S_to");
  //End of Call
  if(TypeA.getName()!="S_integer")scemanticComplementError(currentA_token,3);
//**********************************************************************
  emitln("aload_0");
  emitln("getfield /"+ResA+" "+getJasType(TypeA.getName()));

  //Call expect
  expect("S_to");
  stops.del("S_identifier");
  stops.del("S_number");
  stops.del("S_openpar");
  stops.del("S_character");
  //End of Call
  //Call E
  eE(TypeE);
  stops.del("S_do");
  if(TypeE.getName()!="S_integer")scemanticComplementError(currentA_token,3);
  //End of Call
  emitln("swap");
  emitln("dup2");  //print: mov Temp,ResFor
  Ls = newLabel();
  Le = newLabel();
  emitln(Ls+":");                  //print: Ls:
  emitln("if_icmplt "+Le);
  //Call expect
  expect("S_do");
  stops.del("S_if");  //First IfSt
  stops.del("S_while");  //First WhileSt
  stops.del("S_begin");  //First CompoundSt
  stops.del("S_identifier");  //First AssSt&CallSt
  //End of call
  labeledSt();
  emitln("iadd 1");
  emitln("aload_0");
  emitln("swap");
  emitln("putfield /"+ ResA+" "+getJasType(TypeA.getName()));
  emitln("aload_0");
  emitln("getfield /"+ ResA+" "+getJasType(TypeA.getName()));
  emitln("dup2");
  emitln("goto "+Ls);
  emitln(Le+":");                 //print: Le:
  }//End of ForSt
//----------------------END OF FOR PART---------------------
//-----------------------EXPRESION PART---------------------
    /*    F -> id | No | '(' E ')'     */
  public void fF(ProgramSymbols TypeF){
  ProgramSymbols TempType=new ProgramSymbols(-1,"S_error");
  StringBuffer ConstTemp=new StringBuffer();
  if ((currentSymbol.getName()=="S_number")||
      (currentSymbol.getName()=="S_identifier")||
      (currentSymbol.getName()=="S_float")||
      (currentSymbol.getName()=="S_char"))
    {
     if(currentSymbol.getName()=="S_number"){
                                               TempType.setCode(18);
                                               TempType.setName("S_integer");
                                               emitln("ldc "+currentToken.lexeme);
                                               TypeF.setCode(TempType.getCode() );
                                               TypeF.setName(TempType.getName() );
                                               }
     else  {if(currentSymbol.getName()=="S_float"){
                                                    TempType.setCode(42);
                                                    TempType.setName("S_real");
                                                    emitln("ldc "+currentToken.lexeme);
                                                    TypeF.setCode(TempType.getCode() );
                                                    TypeF.setName(TempType.getName() );
                                                    }
            else if(currentSymbol.getName()=="S_identifier"){
                                                              TempType=symTbl.lookUp(currentToken);
                                                              if(TempType.getName()=="S_error")scemanticComplementError(currentToken,2);
                                                              if(TempType.getName()=="S_char")
                                                                {
                                                                emitln("aload_0");
                                                                emitln("getfield /"+currentToken.lexeme+" C");
                                                                }
                                                              else if(TempType.getName()=="S_integer"){
                                                                emitln("aload_0");
                                                                emitln("getfield /"+currentToken.lexeme+" I");
                                                              }
                                                                   else {
                                                                     emitln("aload_0");
                                                                     emitln("getfield /"+currentToken.lexeme+" F");
                                                                   }
                                                              TypeF.setCode(TempType.getCode() );
                                                              TypeF.setName(TempType.getName() );
                                                              }

            }
     nextSymbol();
     }
    else
    {//Call expect
     stops.add("S_identifier");
     stops.add("S_number");
     stops.add("S_openpar");
     expect("S_openpar");
     stops.del("S_identifier");
     stops.del("S_number");
     stops.del("S_openpar");
     //End of Call
     //Call E
     stops.add("S_closepar");
     eE(TypeF);
     stops.del("S_closepar");
     //End of Call
     expect("S_closepar");
     }
  }
  /*   T -> F { ( * | / ) F }    */
  public void tT(ProgramSymbols TypeT){
  StringBuffer ResF=new StringBuffer();
  StringBuffer Temp=new StringBuffer();
  ProgramSymbols OP,TypeF;
  TypeF=new ProgramSymbols(-1,"S_error");
  Token currentF_token;
  stops.add("S_mul");
  stops.add("S_div");
  fF(TypeT);
  stops.del("S_mul");
  stops.del("S_div");
  while ((currentSymbol.getName()=="S_mul")||
         (currentSymbol.getName()=="S_div"))
   {
    OP=currentSymbol;
    nextSymbol();
    currentF_token=currentToken;
    stops.add("S_mul");
    stops.add("S_div");
    fF(TypeF);
    stops.del("S_mul");
    stops.del("S_div");
    if(TypeT.getName()=="S_char")scemanticComplementError(currentF_token,5);
    if(TypeT.getName()=="S_error")scemanticComplementError(currentF_token,3);
    if(TypeT.getName() =="S_real"){
                                      if (TypeF.getName()=="S_integer")
                                          {
                                            emitln("i2f") ;
                                          }
                                      if(OP.getName()=="S_mul")
                                      emitln("fmul");//print: mul ResT,ResF
                                     else  emitln("fdiv");//print: div ResT,ResF
                                     }
     if(TypeT.getName() =="S_integer"){
                                     if (TypeF.getName()=="S_real")
                                      {
                                       emitln("swap");
                                       emitln("i2f");
                                       if(OP.getName()=="S_mul")
                                        emitln("fmul");//print: mul ResT,ResF
                                       else  emitln("idiv");//print: div ResT,ResF
                                      }
                                   else  if(OP.getName()=="S_mul")
                                      emitln("imul");//print: mul ResT,ResF
                                     else  emitln("idiv");//print: div ResT,ResF
                                     }
    isCompatible(TypeT,TypeF,TypeT);

    }
  }
  /*   E -> T { ( + | - ) T }   */
  public void eE(ProgramSymbols TypeE){
  ProgramSymbols OP,TypeT;
  TypeT=new ProgramSymbols(-1,"S_error");
  Token currentE_token;
  stops.add("S_add");
  stops.add("S_sub");
  tT(TypeE);
  stops.del("S_add");
  stops.del("S_sub");
  while ((currentSymbol.getName()=="S_add")||
         (currentSymbol.getName()=="S_sub"))
   {
    OP=currentSymbol;
    nextSymbol();
    currentE_token=currentToken;
    stops.add("S_add");
    stops.add("S_sub");
    tT(TypeT);
    stops.del("S_add");
    stops.del("S_sub");
    if(TypeT.getName()=="S_char")scemanticComplementError(currentE_token,5);
    if(TypeE.getName()=="S_error")scemanticComplementError(currentE_token,3);
    if(TypeE.getName() =="S_real"){
                                  if (TypeT.getName()=="S_integer")
                                  {
                                    emitln("i2f") ;
                                  }
                                     if(OP.getName()=="S_add")
                                      emitln("fadd");//print: fadd ResE,ResT
                                     else  emitln("fsub");//print: fsub ResE,ResT
                                     }
    else                             {
                                      if (TypeT.getName()=="S_real")
                                       {
                                        emitln("swap");
                                        emitln("i2f") ;
                                        if(OP.getName()=="S_add")
                                         emitln("fadd");//print: add ResE,ResT
                                        else  emitln("fsub");//print: sub ResE,ResT
                                         }
                                     else if(OP.getName()=="S_add")
                                      emitln("iadd");//print: add ResE,ResT
                                     else  emitln("isub");//print: sub ResE,ResT
                                     }
     isCompatible(TypeE,TypeT,TypeE);
   }

  }
  /*   AsSt -> id := E   */
  public void asSt(ProgramSymbols TypeA){
  Token currentA_token;
  ProgramSymbols TypeE,temp;
//  TypeA=new program_symbols(-1,"S_error");
  TypeE=new ProgramSymbols(-1,"S_error");
  temp=new ProgramSymbols(-1,"S_error");
  StringBuffer ResA=new StringBuffer();
  StringBuffer ResE=new StringBuffer();
  //Call expect
  ResA.delete(0,ResA.length());
  ResA.append(currentToken.lexeme);
  currentA_token=currentToken;
  temp=symTbl.lookUp(currentA_token);
  scopy(TypeA,temp);
  if(TypeA.getName()=="S_error")
       scemanticComplementError(currentA_token,2);
  else  if(isChangeable(currentA_token)==false)
        scemanticComplementError(currentA_token,4);
  stops.add("S_assign");
  expect("S_identifier");
  stops.del("S_assign");
  //End of call
  //Call expect
  stops.add("S_identifier");
  stops.add("S_number");
  stops.add("S_openpar");
  expect("S_assign");
  stops.del("S_identifier");
  stops.del("S_number");
  stops.del("S_openpar");
  //End of call
  emitln("aload_0");
  eE(TypeE);
  isCompatible(TypeA,TypeE,temp);
  if(temp.getName()=="S_error")scemanticComplementError(currentA_token,4);
  else{
    if(TypeA.getName() =="S_integer")
    { if (TypeE.getName()=="S_real")
          emitln("f2i");
      emitln("putfield /"+ResA.toString()+" I");
    }
    else if(TypeA.getName()=="S_real")
     { if (TypeE.getName()=="S_integer")
       emitln("i2f");
       emitln("putfield /"+ResA.toString()+" F");
      }
    else  emitln("putfield /"+ResA.toString()+" C");
  }
  }

  /*WhileSt-> while Condition do LabeledSt  */
  public void whileSt()
  {
   StringBuffer Lstart = new StringBuffer();
   StringBuffer Lend = new StringBuffer();
   //First Condition
   stops.add("S_not");
   stops.add("S_identifier");
   stops.add("S_number");
   stops.add("S_openpar");
   expect("S_while");
   stops.del("S_not");
   stops.del("S_identifier");
   stops.del("S_number");
   stops.del("S_openpar");
   //End of expect
   Lstart=newLabel();
   emitln(Lstart+":");
   stops.add("S_do");
   condition();
   stops.del("S_do");
   Lend=newLabel();
   emitln("sipush 1 ");
   emitln("if_icmpne "+Lend);
   //first labeledst
   stops.add("S_if");//First IfSt
   stops.add("S_while");//First WhileSt
   stops.add("S_begin");//First CompoundSt
   stops.add("S_identifier");
   expect("S_do");
   stops.del("S_if");//First IfSt
   stops.del("S_while");//First WhileSt
   stops.del("S_begin");//First CompoundSt
   stops.del("S_identifier");
   //End of expect
   labeledSt();
   emitln("goto "+Lstart);
   emitln(Lend+":");
   }
//params-> (id|no){,(id|no)}
public void  params(StringBuffer Vars[],MyInt VarsCount)
{
   VarsCount.set(-1);
   int flag[]=new int[30],i;
   for (i=0;i<30;i++)
     flag[i]=0;
   MyInt FlagCount=new MyInt(-1);
   if((currentSymbol.getName()=="S_identifier")|(currentSymbol.getName()=="S_number"))
  {
    Vars[VarsCount.inc()].append(currentToken.lexeme);
    i=FlagCount.inc();
    if(currentSymbol.getName()=="S_number")
      flag[i]=1;
    nextSymbol();
    while (currentSymbol.getName()=="S_comma")
     {
       nextSymbol();
       Vars[VarsCount.inc()].append(currentToken.lexeme);
       i=FlagCount.inc();
       if(currentSymbol.getName()=="S_number")
         flag[i]=1;

       if((currentSymbol.getName()=="S_identifier")|(currentSymbol.getName()=="S_number"))
       {
        nextSymbol();
       }
       else syntaxError("Identifier or Number Expected");


     }
  }
  else syntaxError("Identifier or Number Expected");

  for(i=0;i<=VarsCount.get();i++)
  {
   emitln("PUSH  "+Vars[i].toString());
  }
  StringBuffer temp=new StringBuffer();
  temp.append(newTemp());
  for( i=0;i<=VarsCount.get();i++)
  {
   if(flag[i]==1) {
     Vars[i].delete(0,Vars[i].length());
     Vars[i].append(temp);
     }
  }
  remTemp();
}

  /* ProcVarDef ->[var]id{,id}:(id|char|real|integer){;[var]id{,id}:(id|char|real|integer)} */
  public void procVarDef(StringBuffer Vars[],MyInt VarsCount){
 //   String VarVars[]=new String[30];
    VarsCount.set(-1);
    if (currentSymbol.getName()=="S_var")
    {
      nextSymbol();
      int test;
   }
   Vars[VarsCount.inc()].append(currentToken.lexeme);
    stops.add("S_comma");
    stops.add("S_colon");
    expect("S_identifier");
    stops.del("S_comma");
    stops.del("S_colon");
   while(currentSymbol.getName()=="S_comma")
    {
     nextSymbol();
     //Call expect id
     Vars[VarsCount.inc()].append(currentToken.lexeme);

     stops.add("S_comma");
     stops.add("S_colon");
     expect("S_identifier");
     stops.del("S_comma");
     stops.del("S_colon");
     //End of expect
     }//End of while
  //Call expect colon
  stops.add("S_identifier");
  stops.add("S_integer");
  stops.add("S_real");
  stops.add("S_char");
  expect("S_colon");
  stops.del("S_identifier");
  stops.del("S_integer");
  stops.del("S_real");
  stops.del("S_char");
  //End of expect
  //Call expect id or integer or real or char
  stops.add("S_semicolon");
  if((currentSymbol.getName()=="S_identifier")||
     (currentSymbol.getName()=="S_integer")||
     (currentSymbol.getName()=="S_real")||
     (currentSymbol.getName()=="S_char"))
     nextSymbol();
     else syntaxError("S_identifier or S_integer or S_real or S_char");
  stops.del("S_semicolon");
  //End of expect
  //End of expect
  while(currentSymbol.getName()=="S_semicolon")
    {
    nextSymbol();
    if (currentSymbol.getName()=="S_var")
    {
      nextSymbol();
   }

     stops.add("S_real");
     Vars[VarsCount.inc()].append(currentToken.lexeme);

     expect("S_identifier");
     stops.del("S_real");
    while(currentSymbol.getName()=="S_comma")
       {nextSymbol();
       //Call expect id
       stops.add("S_comma");
       stops.add("S_colon");
       Vars[VarsCount.inc()].append(currentToken.lexeme);

       expect("S_identifier");
       stops.del("S_comma");
       stops.del("S_colon");
       //End of expect
       }//End of inner while
     //Call expect colon
     stops.add("S_identifier");
     stops.add("S_integer");
     stops.add("S_real");
     stops.add("S_char");
     expect("S_colon");
     stops.del("S_identifier");
     stops.del("S_integer");
     stops.del("S_real");
     stops.del("S_char");
     //End of expect
     //Call expect id or integer or real or char
     stops.add("S_semicolon");
     if((currentSymbol.getName()=="S_identifier")||
        (currentSymbol.getName()=="S_integer")||
        (currentSymbol.getName()=="S_real")||
        (currentSymbol.getName()=="S_char"))
        nextSymbol();
        else syntaxError("S_identifier or S_integer or S_real or S_char");
     stops.del("S_semicolon");
     //End of expect
     //Call expect semicolon
     }//End of outer while
     for (int i=VarsCount.get();i>=0;i--)
       emitln("POP  "+Vars[i].toString());
  }//End of ProcVarDefPart

/*  ProcedureDef->procedure id (ProcDefPart );
    [VarDefPart] CompoundSt
*/
public void procDef()
{
  String procname;
  StringBuffer Vars[]=new StringBuffer[30];
  for (int i=0;i<30;i++)
  {
    Vars[i]=new StringBuffer();
  }
  MyInt varcount=new MyInt(0);

  stops.add("S_identifier");
  expect("S_procedure");
  stops.del("S_identifier");
  procname=currentToken.lexeme;
  emitln(procname+"   PROC FAR");
  stops.add("S_openpar");
  expect("S_identifier");
  stops.del("S_openpar");

  stops.add("S_identifier");
  stops.add("closepar");
  expect("S_openpar");
  stops.del("S_identifier");
  stops.del("closepar");

if(currentSymbol.getName()!="S_closepar")
{
  stops.add("S_closepar");//First params
  procVarDef(Vars,varcount);
  stops.del("S_closepar");
}
    stops.add("S_colon");
    expect("S_closepar");
    stops.del("S_colon");
    stops.add("S_var");
    stops.add("S_begin");
    expect("S_semicolon");
    stops.del("S_var");
    stops.del("S_begin");
    if(currentSymbol.getName()=="S_var")procVarDefPart();
    compoundSt();
    for (int i=varcount.get();i>=0;i--)
      emitln("Push  "+Vars[i].toString());

    expect("S_semicolon");
    emitln("RET");
}// End of ProcDefPart



//---------------------------CASE PART--------------------------
  /*    CaseSt -> Case E of CasePart {CasePart} [elsePart] end   */
  public void caseSt(){
  Token currentE_token;
  StringBuffer LabelEnd = new StringBuffer();
  ProgramSymbols TypeE=new ProgramSymbols(-1,"S_error");
  //add First E
  stops.add("S_identifier");
  stops.add("S_number");
  stops.add("S_openpar");
  stops.add("S_character");
  stops.add("S_of");
  //add first CasePart
  //stops.add("S_identifier");
  //stops.add("S_number");
  stops.add("S_else");
  stops.add("S_end");
  expect("S_case");
  currentE_token=currentToken;
  stops.del("S_openpar");
  stops.del("S_character");
  eE(TypeE);
  stops.del("s_of");
  expect("S_of");
  LabelEnd=newLabel();
  if (TypeE.getName()=="S_real")scemanticComplementError(currentE_token,6);
  do{
     casePart(TypeE,LabelEnd);
    }while((currentSymbol.getName()=="S_identifier")||
           (currentSymbol.getName()=="S_number"));
  stops.del("S_number");
  stops.del("S_identifier");
  emitln("pop");
  if (currentSymbol.getName()=="S_else")
    {
    nextSymbol();
    labeledSt();
    }
  stops.del("S_else");
  emitln(LabelEnd+":");
  stops.del("S_end");
  expect("S_end");
  }
/* CasePart -> (id|No){,(id|No)}:LabeledSt    */
  public void casePart(ProgramSymbols TypeE,StringBuffer LEnd){
  StringBuffer  LNext=new StringBuffer();
  StringBuffer LStart=new StringBuffer();
  ProgramSymbols Temp=new ProgramSymbols(-1,"S_error");
  LStart=newLabel();
  LNext=newLabel();
  stops.add("S_identifier");
  stops.add("S_number");
  stops.add("S_comma");
  stops.add("S_colon");
  //add First LabeledSt
  stops.add("S_if");//First IfSt
  stops.add("S_while");//First WhileSt
  stops.add("S_begin");//First CompoundSt
  //stops.add("S_identifier");
  stops.add("S_case");
  stops.add("S_for");
  stops.add("S_end");
  if(currentSymbol.getName()=="S_identifier")Temp=symTbl.lookUp(currentToken);
  else if(currentSymbol.getName()=="S_number"){Temp.setCode(18);
                                                 Temp.setName("S_integer");
                                                }
                                                else if(currentSymbol.getName()=="S_real"){Temp.setCode(42);
                                                                                               Temp.setName("S_real");
                                                                                              }
                                                                                              else if(currentSymbol.getName()=="S_character"){Temp.setCode(5);
                                                                                                                                               Temp.setName("S_char");
                                                                                                                                               }
  do{
        switch(TypeE.getCode())
          {
           case 18     : emitln("dup");
                           if(currentSymbol.getName()=="S_identifier"){
                             emitln("aload_0");
                             emitln("getfield /"+currentToken.lexeme+" I");
                           }
                           else  emitln("ldc "+currentToken.lexeme);
                            emitln("if_icmpeq "+LStart);
                            break;
           case 42     : emitln("Error:Var in CasePart must not to be float");
                         break;
           case 5      : emitln("dup");
                         if(currentSymbol.getName()=="S_identifier")emitln("getfield /"+currentToken.lexeme+" C");
                         else  emitln("ldc "+currentToken.lexeme);
                         emitln("if_icmpeq "+LStart);
                         break;
          }//end of switch
        if (currentSymbol.getName()=="S_identifier")
          nextSymbol();
        else
          expect("S_number");
        if (currentSymbol.getName()=="S_colon")
          break;
        expect("S_comma");
     }while(true);
    emitln("goto "+LNext);
    stops.del("S_colon");
    expect("S_colon");
    emit(LStart+":");
    stops.del("S_number");
    stops.del("S_comma");
    //del First LabeledSt
    stops.del("S_if");//First IfSt
    stops.del("S_while");//First WhileSt
    stops.del("S_begin");//First CompoundSt
    stops.del("S_identifier");
    stops.del("S_case");
    stops.del("S_for");
    stops.del("S_end");
    labeledSt();
    emitln("goto "+LEnd);
    emit(LNext+":");
  }
private void isCompatible(ProgramSymbols T1,ProgramSymbols T2,ProgramSymbols T3){
 ProgramSymbols TempOut=new ProgramSymbols(-1,"S_error");
if(T1.getName()=="S_error" && T2.getName()!="S_error") scopy(T3,T2);
else if(T1.getName()!="S_error" && T2.getName()=="S_error") scopy(T3,T1);
else if(T1.getName()=="S_error" && T2.getName()=="S_error")scopy(T3,TempOut);
else if(T1.getName()=="S_integer" && T2.getName()=="S_real")scopy(T3,T2);
else if(T1.getName()=="S_real" && T2.getName()=="S_integer")scopy(T3,T1);
else scopy(T3,T1);
//if(T1.get_name() !=T2.get_name() )return(TempOut);
}
private boolean isChangeable(Token T){
ProgramSymbols Temp=new ProgramSymbols(-1,"S_error");
Temp=symTbl.getGroup(T);
if(Temp.getName()=="S_var")return(true);
else return(false);
}
private void scopy(ProgramSymbols T1,ProgramSymbols T2)
{
  T1.setCode(T2.getCode());
  T1.setName(T2.getName());
}
}//End of class



