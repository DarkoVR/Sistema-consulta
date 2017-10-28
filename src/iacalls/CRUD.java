/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iacalls;
import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;

/**
 *
 *  Este programa crea y lee registros de 142 bytes, se componen de 3 campos (key,key asociada y costs), esto se puede repetir
 *  hasta 14 veces ya que el maximo nodo establece 10 enlaces, se le da un 40% de desbordamiento es decir 4 espacios extra, cabe mencionar que
 *  cuando se requiere ingresar nuevos datos el programa los escribe al final del archivo. (No sobreescribe).
 */
public class CRUD {
    private List<Node<Object>> NodeList = new ArrayList<Node<Object>>();
    Node node = null;
    String key,key_fk,connection;
    double costs;
    char temp,parent_char,child_char;
    long Ireg;

    /**
     * Escribe en ek archivo maestro al final del archivo
     * @throws IOException
     */
    public void write_master() throws IOException{
        String n;
        System.out.println("Archivo maestro");
        RandomAccessFile archi = new RandomAccessFile("CDMX","rw");
        RandomAccessFile index = new RandomAccessFile("INDICE","rw");
        Scanner entrada = new Scanner(System.in);
        /**
         * El siguiente metodo posiciona cada vez que se abra el archivo en el desbordamiento del archivo, así que los nuevos datos escritos
         * se escribiran al final sin sobreescribir el archivo.
         */
        archi.seek(archi.length());
        index.seek(index.length());
        int i = 0;
        long posicion;
        /**
         * En el siguiente bloque do, se ingresa la llave unica y despues el patron de conexión (llave,llave asociada,distancia y tiempo)
         * se repite hasta 14 veces en un solo registro.
         */
        do{
            System.out.println("Clave de Colonia");
            key=entrada.next();
            archi.writeChars(key);
            /**
             * El siguiente bloque do, lee el patron de conexión y lo repite mientras el usuario quiera uno más pero la
             * totalidad de los bloques debe ser de 14 o menos. En donde i es el contador que lleva este registro de 14 o menos.
             */
            do{
                System.out.println("Conexion");
                key_fk=entrada.next();
                archi.writeChars(key_fk);
                System.out.println("Distancia");
                costs=entrada.nextDouble();
                archi.writeDouble(costs);
                System.out.println("¿Ingresar otra Conexion?: (y,n) ");
                n = entrada.next();
                i++;
            }while(n.equalsIgnoreCase("y") && i<14);
            /**
             * La siguiente condicion if es por si no se llenaron los 14 espacios del registro con información, esto llenara esos espacios
             * con datos genericos sin relevancia. Es decir la llava asociada con *, el tiempo y distancia con 0.0, cuando el programa lea
             * estos datos no se imprimiran, solo la informacion relevante.
             */
            if(i<14){
                for(int x=i;x<14;x++){
                    key_fk="*";
                    archi.writeChars(key_fk);
                    costs=0.0;
                    archi.writeDouble(costs);
                }
            }
            i=0;
            System.out.println("¿Ingresar otra Colonia?: (y,n) ");
            n = entrada.next();
            /**
             * En el siguiente bloque y hasta que lee while otra vez, es para la creación del archivo Indice en donde
             * se posiciona en el inicio del archivo maestro con seek(0) y se realiza la logica para conocer el tamaño de un registro esto
             * leyendo todos los tipos de datos que contiene es decir el temp y el ciclo for que esta abajo, se guarda en Ireg e
             * inmediatamente despues se determina la dirección logica del registro en "posicion" y se escribe en el archivo indice
             * así como la llave correspondiente a ese mismo registro, al terminar se posiciona al final del archivo maestro con
             * seek(archi.length()) esto para no interrumpir la escritura de nuevos datos al ciclar en do while otra vez.
             */
            archi.seek(0);
            temp=archi.readChar();
            for(int j=0;j<14;j++){
                temp=archi.readChar();
                costs=archi.readDouble();
            }
            Ireg=archi.getFilePointer();
            posicion=(archi.length()/Ireg);
            index.writeLong(posicion);
            index.writeChars(key);
            archi.seek(archi.length());
        }while(n.equalsIgnoreCase("y"));
        archi.close();
    }

    /**
     * Lee el archivo maestro de manera secuencial
     * @throws IOException
     */
    public void read_seq_master() throws IOException{
        long ap_actual, ap_final;
        RandomAccessFile read_arch = new RandomAccessFile("CDMX","r");
        while((ap_actual=read_arch.getFilePointer())!=(ap_final=read_arch.length())){
            /**
             * En el siguiente bloque antes del if se lee la llave para comprobar que la llave no se haya eliminado
             * cuando un registro se elimina se cambia su llave por '#' y si encuentra esta llave todo el registro se pasa por alto
             */
            temp=read_arch.readChar();
            if(temp!='#'){
                System.out.println("--------------------REGISTRO--------------------------");
                System.out.println("Llave: "+temp);
                for(int i=0;i<14;i++){
                    temp=read_arch.readChar();
                    costs=read_arch.readDouble();
                    /**
                     * En las siguientes condiciones, solo se imprime la información diferente a la información generica.
                     */
                    if(temp!='*'){
                        System.out.println("Conexión: "+temp);
                    }
                    //new String(nombre).replace("\0","");
                    if(costs!=0.0){
                        System.out.println("Distancia: "+costs);
                    }
                }
            }else{
                for(int i=0;i<14;i++){
                    temp=read_arch.readChar();
                    costs=read_arch.readDouble();
                }
            }
        }//Fin while
        read_arch.close();
    }

    /**
     * Lee el archivo indice de manera secuancial
     * @throws IOException
     */
    public void read_seq_index() throws IOException{
        long pos_indice;
        char llave_indice;
        long ap_actual, ap_final;
        RandomAccessFile leer_archi = new RandomAccessFile("INDICE","r");
        while((ap_actual=leer_archi.getFilePointer())!=(ap_final=leer_archi.length())){
            System.out.println("--------------------INDICE--------------------------");
            pos_indice=leer_archi.readLong();
            System.out.println("Posicion: "+pos_indice);
            llave_indice=leer_archi.readChar();
            System.out.println("Llave indice: "+llave_indice);
        }//Fin while
        leer_archi.close();
    }
    /**
     * Lee secuancialmente el archivo indice usanso una llave que encuentra direccion logica y busca en archivo maestro
     * @throws IOException
     * @param x usado como: 1 leer, 2 actualizar, 3 eliminar
     */
    public void read_alt_index(int x) throws IOException{
        String n;
        long desplaza;
        long pos_indice;
        char llave_indice;
        String llave;
        long ap_actual, ap_final;
        RandomAccessFile leer_archi = new RandomAccessFile("INDICE","rw");
        Scanner entrada=new Scanner(System.in);

        leer_archi.readLong();
        leer_archi.readChar();
        Ireg=leer_archi.getFilePointer();
        int cont = 0;
        do{
            leer_archi.seek(0);
            System.out.println("\nIntroduce la llave del registro que deseas buscar: ");
            llave=entrada.next();
            while((ap_actual=leer_archi.getFilePointer())!=(ap_final=leer_archi.length())){
                pos_indice=leer_archi.readLong();
                llave_indice=leer_archi.readChar();
                if(llave_indice==llave.charAt(0)){
                    System.out.println("-------------INDICE-ALEATORIO-------------------------");
                    System.out.println("Posicion indice: "+pos_indice);
                    System.out.println("Llave indice: "+llave_indice);
                    /**
                     * En el siguiente bloque de codigo cuando es 1 lee aleatorio el archivo maestro, 2 actualiza el archivo maestro y
                     * 3 elimina cambiando la llave tanto del archivo indice y el maestro. Aunque el archivo indice no se omite la lectura
                     * de las llaves "eliminadas" para ver con claridad y en totalidad los registros que existen tanto reales como "eliminados".
                     */
                    if(x==0){
                        read_alt_master(pos_indice);
                    }else if(x==1){
                        update_master(pos_indice);
                    }else{
                        /**
                         * Esta seccion de codigo va para el archivo indice, simplemente se cambia la llave por un gato (#)
                         * se posiciona en el registro actual y se cambia.
                         */
                        desplaza=Ireg*cont;
                        leer_archi.seek(desplaza);
                        leer_archi.readLong();
                        leer_archi.writeChars("*");
                        delete_master(pos_indice);
                    }
                }
                cont++;
            }//Fin while
            System.out.println("¿Leer otra Colonia?: (y,n) ");
            n = entrada.next();
        }while(n.equalsIgnoreCase("y"));
        leer_archi.close();
    }

    /**
     * Usa el metodo read_alt_index() para buscar en archivo maestro
     * @param pos_indice
     * @throws IOException
     */
    public void read_alt_master(long pos_indice) throws IOException{
        long desplaza;
        RandomAccessFile archi=new RandomAccessFile("CDMX","r");
        /**
         * En el siguiente bloque hasta "Ireg" lee la totalidad del registro solo para saber el tamaño del registro que se almacenará
         * en Ireg, este tiene un tamaño estatico de 142.
         */
        temp=archi.readChar();
        for(int i=0;i<14;i++){
            temp=archi.readChar();
            costs=archi.readDouble();
        }
        Ireg=archi.getFilePointer();
        /**
         * En el siguente bloque do, posiciona el apuntador usando el tamaño de registro Ireg=142 y la posición que se le asigne numerico.
         * Una vez posicionado se lee lo que tiene inmediatamente adelante este seria el registro deseado usando la lectura secuancial.
         * Tal como se vio arriba.
         */
        desplaza=(pos_indice-1)*Ireg;
        archi.seek(desplaza);

        System.out.println("\nLos datos del registro maestro ("+Ireg+" bytes) son: ");
        System.out.print("\n");
        temp=archi.readChar();
        System.out.println("Llave: "+temp);
        for(int i=0;i<14;i++){
            temp=archi.readChar();
            costs=archi.readDouble();
            if(temp!='*'){
                System.out.println("Conexión: "+temp);
            }
            //new String(nombre).replace("\0","");
            if(costs!=0.0){
                System.out.println("Distancia: "+costs);
            }
        }
        archi.close();
    }

    /**
     * Actualiza un registro del archivo maestro
     * @param pos_index
     * @throws IOException
     */
    public void update_master(long pos_index) throws IOException{
        try{
            //JOptionPane.showMessageDialog(null, "Indice: "+pos_index);
            String n;
            long position;
            RandomAccessFile archi=new RandomAccessFile("CDMX","rw");
            Scanner entrada = new Scanner(System.in);
            /**
             * En el siguiente bloque hasta "Ireg" lee la totalidad del registro solo para saber el tamaño del registro que se almacenará
             * en Ireg, este tiene un tamaño estatico de 142.
             */
            temp=archi.readChar();
            for(int i=0;i<14;i++){
                temp=archi.readChar();
                costs=archi.readDouble();
            }
            Ireg=archi.getFilePointer();
            /**
             * En el siguiente bloque posiciona el apuntador al inicio del registro que se quiere sobreescribir, se salta la llave
             * ya que como es actualizacion no se cambia ésta, solo todo lo demas.
             */
            position=(pos_index-1)*Ireg;
            archi.seek(position);
            archi.readChar();
            int i = 0;
            /**
             * En el siguiente bloque se cambian todas y cada una de las conexiones usando los dos parametros de cada una
             * que llave de conexion y distancia.
             */
            do{
                System.out.println("Conexion");
                key_fk=entrada.next();
                archi.writeChars(key_fk);
                System.out.println("Distancia");
                costs=entrada.nextDouble();
                archi.writeDouble(costs);
                System.out.println("¿Ingresar otra Conexion?: (y,n) ");
                n = entrada.next();
                i++;
            }while(n.equalsIgnoreCase("y") && i<14);
            /**
             * La siguiente condicion if es por si no se llenaron los 14 espacios del registro con información, esto llenara esos espacios
             * con datos genericos sin relevancia. Es decir la llava asociada con *, el tiempo y distancia con 0.0, cuando el programa lea
             * estos datos no se imprimiran, solo la informacion relevante.
             */
            if(i<14){
                for(int x=i;x<14;x++){
                    key_fk="*";
                    archi.writeChars(key_fk);
                    costs=0.0;
                    archi.writeDouble(costs);
                }
            }
            archi.close();
        }catch(java.io.IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Elimina un registro del archivo maestro
     * @param pos_indice
     * @throws IOException
     */
    public void delete_master(long pos_indice) throws IOException{
        try{
            String n;
            long desplaza;
            RandomAccessFile archi=new RandomAccessFile("CDMX","rw");
            Scanner entrada = new Scanner(System.in);
            /**
             * En el siguiente bloque hasta "Ireg" lee la totalidad del registro solo para saber el tamaño del registro que se almacenará
             * en Ireg, este tiene un tamaño estatico de 142.
             */
            temp=archi.readChar();
            for(int i=0;i<14;i++){
                temp=archi.readChar();
                costs=archi.readDouble();
            }
            Ireg=archi.getFilePointer();
            /**
             * En el siguiente bloque posiciona el apuntador al inicio del registro que se quiere sobreescribir, en este caso
             * para cambiar la llave y simular que se ha eliminado el registro, cabe mencionar que los registro que contengan
             * la llave '#' al inicio se omiten.
             */
            desplaza=(pos_indice-1)*Ireg;
            archi.seek(desplaza);
            archi.writeChars("#");

            archi.close();
        }catch(java.io.IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    /**
     * Metodos para recuperar una lista de hijos para uso en las clases
     * @param parentNode
     * @return
     * @throws IOException
     */
    public List<Node<Object>> recoverList(Node parentNode) throws IOException{
        NodeList.clear();
        read_index_finder(parentNode);
        return NodeList;
    }

    /**
     * Lee un registro en especifico que crea un nodo padre
     * @param parentNode
     * @throws IOException
     */
    public void read_index_finder(Node parentNode) throws IOException{
        long position;
        char key;
        RandomAccessFile leer_archi = new RandomAccessFile("INDICE","rw");
        leer_archi.seek(0);
        while((leer_archi.getFilePointer())!=(leer_archi.length())){
            position=leer_archi.readLong();
            key=leer_archi.readChar();
            if(key==parentNode.getData().toString().charAt(0)){
                System.out.println("-------------INDICE-ALEATORIO-------------------------");
                System.out.println("Posicion indice: "+position);
                System.out.println("Llave indice: "+key);

                read_master_finder(position,parentNode);
            }
        }//Fin while
        leer_archi.close();
    }

    /**
     * Usa el metodo de read_index_finder() que llena la lista de recoverList() usando el nodo padre que envia read_index_finder()
     * @param position
     * @param parentNode
     * @throws IOException
     */
    public void read_master_finder(long position,Node parentNode) throws IOException{
        long displace;
        RandomAccessFile archi=new RandomAccessFile("CDMX","r");

        parent_char=archi.readChar();
        for(int i=0;i<14;i++){
            child_char=archi.readChar();
            costs=archi.readDouble();
        }
        Ireg=archi.getFilePointer();
        displace=(position-1)*Ireg;
        archi.seek(displace);

        archi.readChar();
        for(int i=0;i<14;i++){
            child_char=archi.readChar();
            costs=archi.readDouble();
            if(child_char!='*'){
                node = new Node(child_char,parentNode,costs);
                NodeList.add(node);

                System.out.println("Conexión: "+child_char);
                System.out.println("Distancia: "+costs);
            }
        }
        //JOptionPane.showMessageDialog(null,"parent_char: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData());
        //JOptionPane.showMessageDialog(null, "exploredList: "+exploredList);

        archi.close();
    }
}
