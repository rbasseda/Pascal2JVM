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

public class SetOfSymbols {
  public ProgramSymbols s[]=new ProgramSymbols[49];

  public SetOfSymbols() {
    s[0] = new  ProgramSymbols(-1 , "S_error");
    s[1] = new  ProgramSymbols(0 , "S_nocode");
    s[2] = new  ProgramSymbols(1 , "S_add");
    s[3] = new  ProgramSymbols(2 , "S_and");
    s[4] = new  ProgramSymbols(3 , "S_begin");
    s[5] = new  ProgramSymbols(4 , "S_case");
    s[6] = new  ProgramSymbols(5 , "S_char");
    s[7] = new  ProgramSymbols(6 , "S_closepar");
    s[8] = new  ProgramSymbols(7 , "S_colon");
    s[9] = new  ProgramSymbols(8 , "S_comma");
    s[10] = new  ProgramSymbols(9 , "S_div");
    s[11] = new  ProgramSymbols(10 , "S_do");
    s[12] = new  ProgramSymbols(11 , "S_dot");
    s[13] = new  ProgramSymbols(12 , "S_end");
    s[14] = new  ProgramSymbols(13 , "S_float");
    s[15] = new  ProgramSymbols(14 , "S_for");
    s[16] = new  ProgramSymbols(15 , "S_function");
    s[17] = new  ProgramSymbols(16 , "S_identifier");
    s[18] = new  ProgramSymbols(17 , "S_if");
    s[19] = new  ProgramSymbols(18 , "S_integer");
    s[20] = new  ProgramSymbols(19 , "S_mul");
    s[21] = new  ProgramSymbols(20 , "S_number");
    s[22] = new  ProgramSymbols(21 , "S_openpar");
    s[23] = new  ProgramSymbols(22 , "S_or");
    s[24] = new  ProgramSymbols(23 , "S_procedure");
    s[25] = new  ProgramSymbols(24 , "S_program");
    s[26] = new  ProgramSymbols(25 , "S_repeat");
    s[27] = new  ProgramSymbols(26 , "S_semicolon");
    s[28] = new  ProgramSymbols(27 , "S_character");
    s[29] = new  ProgramSymbols(28 , "S_sub");
    s[30] = new  ProgramSymbols(29 , "S_then");
    s[31] = new  ProgramSymbols(30 , "S_until");
    s[32] = new  ProgramSymbols(31 , "S_var");
    s[33] = new  ProgramSymbols(32 , "S_while");
    s[34] = new  ProgramSymbols(33 , "S_assign");
    s[35] = new  ProgramSymbols(34 , "S_equal");
    s[36] = new  ProgramSymbols(35 , "S_GT");
    s[37] = new  ProgramSymbols(36 , "S_GTE");
    s[38] = new  ProgramSymbols(37 , "S_LT");
    s[39] = new  ProgramSymbols(38 , "S_LTE");
    s[40] = new  ProgramSymbols(39 , "S_NE");
    s[41] = new  ProgramSymbols(40 , "S_eof");
    s[42] = new  ProgramSymbols(41 , "S_type");
    s[43] = new  ProgramSymbols(42 , "S_real");
    s[44] = new  ProgramSymbols(43 , "S_not");
    s[45] = new  ProgramSymbols(44 , "S_const");
    s[46] = new  ProgramSymbols(45 , "S_of");
    s[47] = new  ProgramSymbols(46 , "S_to");
    s[48] = new  ProgramSymbols(47 , "S_else");
  }
  public void add(String str){
    int i;
    for (i=0;i<49;i++)
      if ( str.compareTo(s[i].name)==0)
        {
         s[i].exist = true;
         break;
         }
    }
  public boolean inSet(String str){
  int i;
    for (i=0;i<49;i++)
      if ( str.compareTo(s[i].name)==0)
         if ( s[i].exist == true) return true;
     return false;
    }
  public void del(String str)  {
  int i;
    for (i=0;i<49;i++)
      if ( str.compareTo(s[i].name)==0)
         if ( s[i].exist == true)
           {
            s[i].exist = false;
            break;
            }
    }

}