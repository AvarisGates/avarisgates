package com.avaris.averisgates.core.player.attribute;

public enum AttributeType {
   Strength,
   Vitality,
   Dexterity,
   Agility,
   Intelligence,
   Will,
   Faith;

    public static AttributeType fromInt(int i){
       switch (i){
           case 0 -> {
               return Strength;
           }
           case 1 -> {
               return Vitality;
           }
           case 2 -> {
               return Dexterity;
           }
           case 3 -> {
               return Agility;
           }
           case 4 -> {
               return Intelligence;
           }
           case 5 -> {
               return Will;
           }
           case 6 -> {
               return Faith;
           }
           default -> throw new IllegalStateException("Invalid Attribute Type: "+i);
       }
    }
}
