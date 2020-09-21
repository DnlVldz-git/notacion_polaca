
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.pow;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author macbook
 */
public class notacion_polaca {

    public static final Integer EOF = -1;

    public static void main(String[] args){
        Scanner leer = new Scanner(System.in);
        lista colita = new lista();

        System.out.println("Introduzca la dirección del archivo: ");
        String direccion = leer.nextLine();

        try{
            File file = new File(direccion);
            FileReader reader = new FileReader(file);
            int ascii;
            while((ascii =  reader.read()) != EOF){
                colita.insertar_1_c((char) ascii);
            }

            reader.close();

        }catch (IOException ex) {
            System.out.println("Error al abrir archivo");
        }
        System.out.println("Introduzca su opción:");
        System.out.println("Fase 1: Mostrar la expresión en notación polaca inversa [1] \t Fase 2: Resolver la expresión [2]");
        char resp = leer.next().charAt(0);
        switch(resp){
            case '1':
                System.out.println("\nLa expresión es:");
                colita.recorrer_1_c();
                System.out.println("\nLa expresión en notación polaca es:");
                colita.pasar_polaca_inv_c();
                colita.recorrer_c();
                System.out.println("\n");
            break;
            case '2':
                System.out.println("La expresión es:");
                colita.recorrer_1_c();
                colita.pasar_polaca_inv_c();
                colita.resolver_polaca_inv();
                System.out.println("\n");
                System.out.println("El resultado es: ");
                colita.recorrer_p();
                System.out.println("\n");
            break;
        }






    }



}

class lista{
    nodo_c inicio = null;
    nodo_c inicio_1 = null, inicio_2 = null, fin_1 = null, fin_2 = null;


    public void insertar_p(char dato){
        nodo_c nuevo = new nodo_c();
        nuevo.dato = dato;
        nuevo.prev = null;
        nuevo.sig = null;

        if(inicio == null){
            inicio = nuevo;
        }else {
            nuevo.sig = inicio;
            inicio.prev=nuevo;
            inicio = nuevo;
        }
    }
    public boolean checar_vacia_p(){
        return(inicio ==null);
    }

    public char sacar_p(){
        char op = inicio.dato;
        inicio = inicio.sig;

        return op;
    }
    public int asignar_valor_p(char dato){
        int valor_op =0;
        switch(dato){
            case '^':
                valor_op=3;
            break;
            case '*':
                valor_op=2;
            break;
            case '/':
                valor_op=2;
            break;
            case '+':
                valor_op=1;
            break;
            case '-':
                valor_op=1;
            break;
        }
        return valor_op;
    }
    public void metodo_operador_p(char operador){
        int valor_op = asignar_valor_p(operador);
        int valor_pila = asignar_valor_p(inicio.dato);

        if(operador=='('){
            insertar_p(operador);
        }else if(inicio.dato=='('){
            insertar_p(operador);
        }else if(valor_op==valor_pila){
            insertar_2_c(inicio.dato);
            inicio = inicio.sig;
            insertar_p(operador);
        }else if(valor_op<valor_pila){
            insertar_2_c(inicio.dato);
            inicio = inicio.sig;
            if(inicio!=null&&valor_op<=asignar_valor_p(inicio.dato)){
                metodo_operador_p(operador);
            }else{
                insertar_p(operador);
            }
        } else if(valor_op>valor_pila){
            insertar_p(operador);
        }
    }
    public void sacar_parentesis_p(){
        while(inicio.dato!='('){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
        inicio=inicio.sig;
    }
    public void sacar_todo_p(){
        while(inicio!=null){
            insertar_2_c(inicio.dato);
            inicio=inicio.sig;
        }
    }
    public void insertar_1_c(char dato){
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_1 == null){
            inicio_1 = nuevo;
            fin_1  = nuevo;
        }else{
            fin_1.sig = nuevo;
            nuevo.prev = fin_1;
            fin_1 = nuevo;
        }
    }
    public void insertar_2_c(char dato){
        nodo_c nuevo = new nodo_c();
        nuevo.sig = null;
        nuevo.prev = null;
        nuevo.dato = dato;
        if(inicio_2 == null){
            inicio_2 = nuevo;
            fin_2  = nuevo;
        }else{
            fin_2.sig = nuevo;
            nuevo.prev = fin_2;
            fin_2 = nuevo;
            fin_2.sig = null;
        }
    }
    public void recorrer_p(){
        if(inicio!=null){
            nodo_c aux = inicio;
            while(aux!=null){
                System.out.println(aux.dato);
                aux = aux.sig;
            }
        }
    }
    public void recorrer_1_c(){
        nodo_c aux = inicio_1;
        while(aux!=null){
            System.out.print(aux.dato);
            aux = aux.sig;
        }
    }
    public void recorrer_c(){
        nodo_c aux = inicio_2;
        while(aux!=null){
            System.out.print(aux.dato);
            aux = aux.sig;
        }
    }
    public void pasar_polaca_inv_c(){
        if(inicio_1!=null){
            nodo_c aux = inicio_1;
            while(aux!=null){
                if(si_operador_c(aux.dato)){
                    if(checar_vacia_p()){
                        insertar_p(aux.dato);
                    }else if(aux.dato==')'){
                        sacar_parentesis_p();
                    }else {
                        metodo_operador_p(aux.dato);
                    }
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux=aux.sig;

                }else{
                    insertar_2_c(aux.dato);
                    if(aux.sig ==null){
                        sacar_todo_p();
                    }
                    aux = aux.sig;
                }
            }
        }
    }
    public void resolver_polaca_inv(){
        if(inicio_2==null){
            return;
        }
        nodo_c aux = inicio_2;
        inicio_1 = null;
        inicio = null;
        while(aux!=null){
            if(si_operador_c(aux.dato)){
                char num_iz = sacar_p();
                char num_de = sacar_p();
                operar_num(num_iz, num_de, aux.dato);
                aux=aux.sig;
            }else{
                insertar_p(aux.dato);
                aux=aux.sig;
            }
        }

    }
    public void operar_num(char num_iz, char num_de, char op ){
        int num_i=Character.getNumericValue(num_iz);
        int num_d=Character.getNumericValue(num_de);
        int REDIX=10;
        char c;
        int res;
        switch(op){
            case '^':
                res=(int) pow(num_i, num_d);
                c=Character.forDigit(res,REDIX);
                insertar_p(c);
            break;
            case '*':
                res = num_i*num_d;
                c=Character.forDigit(res,REDIX);
                insertar_p(c);
            break;
            case '/':
                res = num_i/num_d;
                c=Character.forDigit(res,REDIX);
                insertar_p(c);
            break;
            case '+':
                res = num_i+num_d;
                c=Character.forDigit(res,REDIX);
                insertar_p(c);
            break;
            case '-':
                res = num_d-num_i;
                c=Character.forDigit(res,REDIX);
                insertar_p(c);
            break;
        }


    }
    public boolean si_operador_c(char dato){
        return (dato == '+'||dato == '*'||dato == '-'||dato == '^'||dato == '/'||dato=='('||dato==')');
    }


}

class nodo_c{
    char dato;
    nodo_c sig;
    nodo_c prev;
}
# notacion_polaca
