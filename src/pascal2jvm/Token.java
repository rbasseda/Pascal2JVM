package pascal2jvm;
import java.*;

/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */
public class Token {

  public int row;
  public int col;
  private int NestedBlockNo;
  private int length;
  public String lexeme;
  public ProgramSymbols symbol;

  public Token(int cd,int l,int ro,int co,int nbn,String lex ) {
  row = ro;
  col = co;
  NestedBlockNo = nbn;
  symbol = new ProgramSymbols(cd,"s");
  symbol.setName(getcode());
  length=l;
  lexeme = lex;
  }

  public String getcode()
  {
   switch(symbol.code)
   {
   case -1: return("S_error");
   case 0: return("S_nocode");
   case 1: return("S_add");
   case 2: return("S_and");
   case 3: return("S_begin");
   case 4: return("S_case");
   case 5: return("S_char");
   case 6: return("S_closepar");
   case 7: return("S_colon");
   case 8: return("S_comma");
   case 9: return("S_div");
   case 10: return("S_do");
   case 11: return("S_dot");
   case 12: return("S_end");
   case 13: return("S_float");
   case 14: return("S_for");
   case 15: return("S_function");
   case 16: return("S_identifier");
   case 17: return("S_if");
   case 18: return("S_integer");
   case 19: return("S_mul");
   case 20: return("S_number");
   case 21: return("S_openpar");
   case 22: return("S_or");
   case 23: return("S_procedure");
   case 24: return("S_program");
   case 25: return("S_repeat");
   case 26: return("S_semicolon");
   case 27: return("S_character");
   case 28: return("S_sub");
   case 29: return("S_then");
   case 30: return("S_until");
   case 31: return("S_var");
   case 32: return("S_while");
   case 33: return("S_assign");
   case 34: return("S_equal");
   case 35: return("S_GT");
   case 36: return("S_GTE");
   case 37: return("S_LT");
   case 38: return("S_LTE");
   case 39: return("S_NE");
   case 40: return("S_eof");
   case 41: return("S_type");
   case 42: return("S_real");
   case 43: return("S_not");
   case 44: return("S_const");
   case 45: return("S_of");
   case 46: return("S_to");
   case 47: return("S_else");
   }
   return("S_error");
 }
public String gerrow()
 {
   String temp="";
   return   temp.valueOf(row);
 }
public String getcol()
 {
   String temp="";
   return   temp.valueOf(col);
 }
}