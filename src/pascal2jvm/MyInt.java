package pascal2jvm;

/**
 * Title:        LexerView
 * Description:  Viewing the first part of compiler project
 * Copyright:    Copyright (c) 2002
 * Company:      I.U.S.T
 * @author Armin-Reza-Roozbeh
 * @version 1.0
 */

public class MyInt {

   private int v;
   public int get(){return v;}
   public void set(int i){v=i;}
   public int inc(){v++;return v;}
   public MyInt(int i) {
   v=i;
   }

}