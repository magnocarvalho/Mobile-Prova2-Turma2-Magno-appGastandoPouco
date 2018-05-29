package br.edu.utfpr.magneira.gastandopouco.gastandopoucoapp.util;

public class UtilString {
    public static boolean stringVazia(String texto){

        if (texto == null || texto.trim().length() == 0){
            return true;
        }else{
            return false;
        }
    }
}
