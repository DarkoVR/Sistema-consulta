/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iacalls;
import java.io.*;
import java.util.*;

public class IACalls {
    static CRUD crud = new CRUD();
    static int n=0;
    static Scanner entrada = new Scanner(System.in);
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        Menu();
    }
    public static void Menu() throws IOException{
        do{
            System.out.println("\n\033[33mIngresa la operacion que desees realizar dentro del archivo:\n"
                    + "\033[33m1.-Escribir en el archivo maestro e indice\n"
                    + "\033[33m2.-Leer secuencial el archivo maestro\n"
                    + "\033[33m3.-Leer secuencial el archivo indice\n"
                    + "\033[33m4.-Buscar aleatorio en archivo maestro\n"
                    + "\033[33m5.-Actualizar en archivo maestro\n"
                    + "\033[33m6.-Eliminar de archivo maestro\n"
                    + "\033[33m7.-Busquedas especializadas\n"
                    + "\033[33m8.-Salir\n");
            n=entrada.nextInt();
            switch(n){
                case 1:
                    System.out.println("\033[32mESCRITURA DEL ARCHIVO MAESTRO\n");
                    crud.write_master();
                    break;
                case 2:
                    System.out.println("\033[32mLECTURA DEL ARCHIVO MAESTRO\n");
                    crud.read_seq_master();
                    break;
                case 3:
                    System.out.println("\n\033[32mLECTURA DEL ARCHIVO INDICE\n");
                    crud.read_seq_index();
                    break;
                case 4:
                    System.out.println("\n\033[32mBUSQUEDA ALEATORIO EN ARCHIVO MAESTRO");
                    crud.read_alt_index(0);
                    break;
                case 5:
                    System.out.println("\n\033[32mACTUALIZAR EN ARCHIVO MAESTRO");
                    crud.read_alt_index(1);
                    break;
                case 6:
                    System.out.println("\n\033[32mELIMINAR EN ARCHIVO MAESTRO");
                    crud.read_alt_index(2);
                case 7:
                    System.out.println("\n\033[32mBUSQUEDAS ESPECIALIZADAS");
                    Search();
                    break;
                case 8:
                    break;
                default:
                    System.out.println("Esa opcion no existe!!");
            }
        }while(n!=8);
    }

    public static void Search() throws IOException{
        do{
            System.out.println("\n\033[33mIngresa la busqueda que desees realizar dentro del archivo:\n"
                    + "\033[33m1.-Busqueda por Anchura\n"
                    + "\033[33m2.-Busqueda por Profundidad\n"
                    + "\033[33m3.-Busqueda por Primero el mejor\n"
                    + "\033[33m4.-Busqueda por Grafos O \n"
                    + "\033[33m5.-Busqueda por Algoritmo A \n"
                    + "\033[33m6.-Busqueda por Algotitmo A* \n"
                    + "\033[33m7.-Salir\n");
            n=entrada.nextInt();
            switch(n){
                case 1:
                    System.out.println("\033[32mBUSQUEDA POR ANCHURA\n");
                    Breadthfirst anchura = new Breadthfirst();
                    anchura.breadthfirst();
                    break;
                case 2:
                    System.out.println("\033[32mBUSQUEDA POR PROFUNDIDAD\n");
                    Depthfirst profundidad = new Depthfirst();
                    profundidad.depthfirst();
                    break;
                case 3:
                    System.out.println("\033[32mBUSQUEDA POR PRIMERO EL MEJOR\n");
                    GraphsA primeromejor = new GraphsA();
                    primeromejor.GraphsA();
                    break;
                case 4:
                    System.out.println("\033[32mBUSQUEDA POR GRAFOS O\n");
                    GraphsO grafoso = new GraphsO();
                    grafoso.graphsO();
                    break;
                case 5:
                    System.out.println("\033[32mBUSQUEDA POR ALGORITMO A\n");
                    GraphsA grafosa = new GraphsA();
                    grafosa.GraphsA();
                    break;
                case 6:
                    System.out.println("\033[32mBUSQUEDA POR ALGORITMO A*\n");
                    GraphsAst grafosaest = new GraphsAst();
                    grafosaest.graphsAst();
                    break;
                case 7:
                    break;
            }
        }while(n!=7);
    }
}
