/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iacalls;

import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 *  Programa que usa una clase generica para crear arboles llamada Node.java, ahi estan todos los metos usados.
 *  Usa 4 listas exploredList (para expandir todos los nodos hijos de uno por uno),expandedList (evitar circuitos y repetir nodos en los padres)
 *  parentTList (guarda los objetos nodo padres) e childTList (guarda los objetos nodo hijos)
 */
public class Breadthfirst {
    CRUD crud = new CRUD();
    private List<Node<Object>> exploredList = new ArrayList<Node<Object>>();
    private List<Node<Object>> parentTList = new ArrayList<Node<Object>>();
    private List<Node<Object>> childTList = new ArrayList<Node<Object>>();
    private List<Node<Object>> expandedList = new ArrayList<Node<Object>>();
    private List<Node<Object>> recoveredList = new ArrayList<Node<Object>>();
    Node parentNode =  null;
    Node childNode = null;
    String key,key_fk;
    String path="Arbol por anchura:\n";
    double costs;
    char parent_char,child_char;
    boolean condition;
    /**
     * En este metodo pide por teclado el nodo raiz y el nodo
     * @throws IOException
     */
    public void breadthfirst() throws IOException{
        Scanner entrada=new Scanner(System.in);

        System.out.println("\nIntroduce la llave inicial de la busqueda: ");
        key=entrada.next();

        System.out.println("\nIntroduce la llave destino: ");
        key_fk=entrada.next();

        /**
         * Se ingresa el primer nodo y se designa como raiz
         */
        setCondition(false);
        parentNode = new Node(key);
        exploredList.add(parentNode);
        parentTList.add(parentNode);
        parent_char = key.charAt(0);
        if(key.equals(key_fk)){
            System.out.println("\033[32mHAS LLEGADO AL DESTINO\n\033[32m"+key+" < "+key);
            exit(0);
        }else{
            checkExpansion();
        }
    }
    /**
     * En este metodo se encuentra la logica principal del metodo aqui se reinicia el booleano condition para evitar problemas
     Se realiza un ciclo donde repasa todos los nodos de la lista parentTList, se manda al metodo buscandoExpandir(nodo actual de la lista parentTList)
     Si ya esta en la lista expandedList se omite y se lee el siguiente nodo, Si no esta en la lista se agrega a la lista expandedList
     (La lista expandedList guarda solo los padres a los que ya se le expandieron sus nodos hijos, se usa para evitar olvidar expandir algun nodo)
     Se cambia el nodo parentNode como el nodo "hermano" y se envia a los metodos read_index_finder() en donde lee el indice y obtiene
     la direccion logica y con esto se obtiene el acceso al dato en el metodo read_master_finder(), devuelve la lista de nodos hijos
     este es una lista de todos los nodos hijos de todos los nodos "hermanos" por nivel, asi hasta que revisa todo el ciclo for.
     * @throws IOException
     */
    public void checkExpansion() throws IOException{
        for(int i=0;i<parentTList.size();i++){
            //JOptionPane.showMessageDialog(null,i+" Padre: "+parentTList.get(i).getData() + " No.Hermanos: "+parentTList.size());
            setCondition(false);
            findExpanded(parentTList.get(i));
            if(isCondition()){
                setCondition(false);
            }else{
                expandedList.add(parentTList.get(i));
                parentNode=parentTList.get(i);
                //JOptionPane.showMessageDialog(null, "Lista expandedList: "+expandedList);
                /**
                 * En el for siguiente junto con la lista que recupera de todos los nodos hijos realiza la condicion si ya se tiene en la lista
                 * de explored si no se agrega, si si se omite
                 */
                recoveredList=crud.recoverList(parentNode);
                for(int x=0;x<recoveredList.size();x++){
                    findExplored(recoveredList.get(x).getData().toString());
                    if(isCondition()){
                        setCondition(false);
                    }else{
                        childNode = new Node(recoveredList.get(x).getData().toString(),parentNode);
                        parentNode.addChild(childNode);
                        exploredList.add(childNode);
                        childTList.add(childNode);

                        path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+"\n";
                        if(key_fk.equals(recoveredList.get(x).getData().toString())){
                            end();
                        }
                    }
                }
            }
        }
        //JOptionPane.showMessageDialog(null, "salio del ciclo");
        /**
         * En este siguiente for solo determina de la lista de padres si tiene no es hoja, es decir que tenga hijos esto para poder moverse a un
         * hijo, si el nodo fuera hoja y se mueve ahí = NullPointerException
         */
        for(int i=0;i<parentTList.size();i++){
            if(!parentTList.get(i).isLeaf()){
                parentNode=parentTList.get(i).getChildAt(0);
            }else{
                path+="Nodo: "+parentTList.get(i).getData()+" es hoja, omite nodo\n";
            }
        }
        /**
         * En la siguiente condicion se comprueba que de la lista padres tenga almenos un elemento para proseguir, en el momento que la lista
         * padres no tenga elementos significa que ya no hay ningun nodo en donde buscar, es decir, el arbol se acabo y no se encontro el nodo
         * objetivo, ya que el resultado acertado sale de el metodo leer_aleatorio_maestro (Es la condicon que lleva al metodo terminado())
         * Cuando entra a la condicion la lista de padres se limpia y se sustituye por todos los elementos de la lista hijos, hijos se limpia
         * y se realiza recursividad
         */
        if(!parentTList.isEmpty()){
            parentTList.clear();
            for(int i=0;i<childTList.size();i++){
                parentTList.add(childTList.get(i));
            }
            childTList.clear();
            checkExpansion();
        }else{
            //JOptionPane.showMessageDialog(null, "No se encontro el nodo\nSe acabo el path");
            System.out.println("\n\n\033[33m----------------------RESULTADO------------------------------------");
            System.out.println("\033[33mNo se encontro el nodo\n\033[33mSe acabo el arbol");
            System.out.println(path);
            exit(0);
        }
    }
    /**
     * Metodo para terminar y mostrar el resultado, se imprime yendo desde el nodo objetivo buscando e imprimiendo sus padres hasta
     * llegar al nodo raiz (parent_char==null)
     */
    public void end(){
        //JOptionPane.showMessageDialog(null, "Datos child_char objetivo: "+childNode.getData()+" Padre child_char objetivo: "+childNode.getParentData());
        String finalpath=""+childNode.getData();
        while(childNode.getParent()!=null){
            finalpath+=" < "+childNode.getParent().getData();
            childNode=childNode.getParent();
        }
        //JOptionPane.showMessageDialog(null, "Has encontrado el objetivo\nRecorrido: "+finalpath);
        System.out.println("\n\n\033[32m----------------------RESULTADO------------------------------------");
        System.out.println("\033[32mHas encontrado el objetivo: "+key_fk+"\n\033[33mRecorrido: [ "+finalpath+" ] ");
        System.out.println(path);
        exit(0);
    }
    /**
     * Metodos para buscar una key dentro de la lista de exploredList
     * @param llave
     */
    public void findExplored(String llave){
        for(int i=0;i<exploredList.size();i++){
            if(llave.equals(exploredList.get(i).getData().toString()))
                setCondition(true);
        }
    }
    /**
     * Metodo para buscar una key en la lista de expandedList
     * @param llave
     */
    public void findExpanded(Node llave){
        for(int i=0;i<expandedList.size();i++){
            if(llave.equals(expandedList.get(i)))
                setCondition(true);
        }
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista parentTList
     */
    public void impParents(){
        String acum="PadresT: ";
        for(int i=0;i<parentTList.size();i++){
            acum+=parentTList.get(i).getData().toString()+", ";
        }
        JOptionPane.showMessageDialog(null, acum);
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista childTList
     */
    public void impChildrends(){
        String acum="hijosT: ";
        for(int i=0;i<childTList.size();i++){
            acum+=childTList.get(i).getData().toString()+", ";
        }
        JOptionPane.showMessageDialog(null, acum);
    }
    /**
     * Getter y Setter de boolean condition
     * @return
     */
    public boolean isCondition() {
        return condition;
    }

    public void setCondition(boolean condition) {
        this.condition = condition;
    }
}
