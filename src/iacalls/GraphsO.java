/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iacalls;

import java.io.IOException;
import java.io.RandomAccessFile;
import static java.lang.System.exit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * Programa que calcula la mejor ruta usando el algoritmo Grafos O que usa una lista de abiertos y una Tabla A en donde se almacenan todos los nodos
 * explorados y se realiza rectificación en caso de que ser necesarios que se encuentre el mismo hijo por parte de dos padres en este caso se reaiza una
 * verificacion que compara el menor y con ese se queda.
 */
public class GraphsO {
    CRUD crud = new CRUD();
    DecimalFormat df = new DecimalFormat("#.00");
    private List<Node<Object>> parentList = new ArrayList<Node<Object>>();
    private List<Node<Object>> openedTList = new ArrayList<Node<Object>>();
    private List<Node<Object>> openedList = new ArrayList<Node<Object>>();
    private List<Node<Object>> Table_A = new ArrayList<Node<Object>>();
    private List<Double> positionList = new ArrayList<Double>();
    private List<Node<Object>> recoveredList = new ArrayList<Node<Object>>();
    Node nodo = null;
    Node parentNode =  null;
    Node childNode = null;
    String key,key_fk;
    String path="\n\nArbol de Grafos O:\n",table_a="n  |  S  |  anterior  |  coste  | \n";
    double costs,fq;
    char parent_char,child_char;
    boolean condition;
    /**
     * En este metodo pide por teclado el nodo raiz y el nodo
     * @throws IOException
     */
    public void graphsO() throws IOException{
        Scanner entrada=new Scanner(System.in);

        System.out.println("\nIntroduce la llave inicial de la busqueda: ");
        key=entrada.next();

        System.out.println("\nIntroduce la llave destino: ");
        key_fk=entrada.next();

        /**
         * Se ingresa el primer nodo y se designa como raiz
         */
        parentNode = new Node(key,null,0);
        //padres.add(parentNode);
        openedList.add(parentNode);
        Table_A.add(parentNode);
        //padresT.add(parentNode);
        //padre = key.charAt(0);

        checkExpansion();
    }
    /**
     * En este metodo se encuentra la logica principal del metodo aqui se reinicia el booleano condition para evitar problemas
     * Este metodo revisa si la lista de openedList no esta vacia, si se cumple entra a la condition que es agregar el primer nodo de la lista
     * openedList a la lista closedList y el primer nodo de la lista openedList lo quita, despues al metodo de busqueda se le envia la key del
     * nodo parent_char actual y este metodo se encarga de buscar en al archivo indice y pasar a buscar de manera secuencial indexada al archivo
     * maestro y al finalizar realiza recursividad para probar la lista openedList otra vez, en caso de que se encuentre la lista openedList vacia
     * se manda un mensaje con Fin con fallo y termina ejecucion
     * @throws IOException
     */
    public void checkExpansion() throws IOException{
        if(!openedList.isEmpty()){
            parentNode=openedList.get(0);
            //JOptionPane.showMessageDialog(null, "parentNode: "+parentNode.getData());
            if(key_fk.equals(parentNode.getData().toString())){
                end();
            }
            //JOptionPane.showMessageDialog(null, "openedlist: "+impOpened());
            openedList.remove(0);
            parentList.add(parentNode);
            //JOptionPane.showMessageDialog(null, "parentList: "+impParents());
            /**
             * Recupera la lista de los hijos de este padre en especifico le aplica condicion de entrada a circuitos pero con verificación
             * la lista de no perminitidos (parentsList) solo guardará los padres ya explorados
             */
            recoveredList.clear();
            recoveredList=crud.recoverList(parentNode);
            //JOptionPane.showMessageDialog(null, "recoveredtList: "+imprecoveredList());
            //JOptionPane.showMessageDialog(null, "openedlist: "+impOpened());
            for(int x=0;x<recoveredList.size();x++){
                setCondition(false);
                findParents(recoveredList.get(x).getData().toString());
                //JOptionPane.showMessageDialog(null, impTable_A());
                //JOptionPane.showMessageDialog(null, "esta en parentList: "+isCondition()+" child_char: "+recoveredList.get(x).getData()+" parent_char: "+parentNode.getData());
                if(isCondition()){
                    setCondition(false);
                }else{
                    setCondition(false);
                    setNodo(null);
                    findTable_A(recoveredList.get(x).getData().toString());
                    //findClosed(childNode.getData().toString());
                    //JOptionPane.showMessageDialog(null, "Lo encontro en Table A: "+isCondition()+" child_char: "+recoveredList.get(x).getData()+" parent_char: "+parentNode.getData());
                    if(!isCondition()){
                        fq=recoveredList.get(x).getCost()+parentNode.getCost();
                        childNode = new Node(recoveredList.get(x).getData().toString(),parentNode,fq);
                        parentNode.addChild(childNode);
                        Table_A.add(childNode);
                        openedList.add(childNode);
                        path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+" coste "+df.format(childNode.getCost())+"\n";
                    }else{
                        //JOptionPane.showMessageDialog(null, "Rectificacion Nodo: "+childNode.getData()+" Antes = "+getNodo().getCost()+" > Ahora = "+childNode.getCost());
                        rectify(recoveredList.get(x),parentNode,recoveredList.get(x).getCost());
                        reorderOpened();
                    }
                }
                //path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+" coste "+df.format(childNode.getCost())+"\n";
            }
            //JOptionPane.showMessageDialog(null, "padre1: "+parentNode.getData());
            checkExpansion();
        }else{
            System.out.println("\n\n\033[33mNo hay solucion. Se acabo el grafo");
            System.out.println(path);
            exit(0);
        }
    }
    /**
     * Metodo para terminar y mostrar el resultado, se imprime yendo desde el nodo objetivo buscando e imprimiendo sus parentList hasta
     llegar al nodo raiz (parent_char==null)
     */
    public void end(){
        //JOptionPane.showMessageDialog(null, "Datos child_char objetivo: "+childNode.getData()+" Padre child_char objetivo: "+childNode.getParentData());
        double coste=0;
        String recorrido=""+parentNode.getData();
        coste=parentNode.getCost();
        while(parentNode.getParent()!=null){
            recorrido+=" < "+parentNode.getParent().getData();
            parentNode=parentNode.getParent();
        }
        //JOptionPane.showMessageDialog(null, "Has encontrado el objetivo\nRecorrido: "+recorrido);
        System.out.println(path);
        System.out.println("\n\n\033[32m----------------------RESULTADO------------------------------------");
        System.out.println("\033[32mHas encontrado el objetivo: "+key_fk+"\n\033[33mRECORRIDO: [ "+recorrido+" ] COSTE: [ "+df.format(coste)+" ]");
        System.out.println("\n\n----------------------TABLA A------------------------------------");
        System.out.printf("\033[33m%-20s%-20s%-20s%-20s\n","n","S","Anterior","Coste");
        for(int i=0;i<parentList.size();i++){
            if(parentList.get(i).getParent()!=null){
                System.out.printf("%-20s%-20s%-20s%-20s\n",parentList.get(i).getData(),parentList.get(i).getChildrenData(),parentList.get(i).getParent().getData(),df.format(parentList.get(i).getCost()));
            }else{
                System.out.printf("%-20s%-20s%-20s%-20s\n",parentList.get(i).getData().toString(),parentList.get(i).getChildrenData(),"null",df.format(parentList.get(i).getCost()));
            }
        }
        //System.out.println(table_a);
        exit(0);
    }
    /**
     * Metodos para buscar una key dentro de la lista de parentList
     * @param llave
     */
    public void findParents(String llave){
        for(int i=0;i<parentList.size();i++){
            if(llave.equals(parentList.get(i).getData().toString()))
                setCondition(true);
        }
    }
    /**
     * Metodos para buscar una key dentro de la lista de openedList
     * @param nodo
     */
    public void findTable_A(String nodo){
        for(int i=0;i<Table_A.size();i++){
            if(nodo.equals(Table_A.get(i).getData().toString())){
                setCondition(true);
                setNodo(Table_A.get(i));
            }
        }
    }

    /**
     * Metodo para rectificar un nodo cambiando el padre y el costo cuando el costo cumple
     * @param q
     * @param n
     * @param costenq
     */
    public void rectify(Node q,Node n,double costenq){
        //JOptionPane.showMessageDialog(null, "Entro a rectificar: String: "+q+" Padre: "+n.getData().toString()+" costenq: "+costenq);
        path+="Condicion rectificacion Padre: "+parentNode.getData()+" Nodo: "+q.getData()+" { Antes = "+df.format(getNodo().getCost())+" > Ahora = "+df.format(n.getCost()+costenq)+" }\n";
        for(int i=0;i<Table_A.size();i++){
            if(q.getData().toString().equals(Table_A.get(i).getData().toString())){
                if(n.getCost()+costenq<getNodo().getCost()){
                    path+="Si cumplio\n";
                    path+="Nodo actual: "+Table_A.get(i).getData()+" Padre actual: "+Table_A.get(i).getParent().getData()+" Costo actual: "+df.format(Table_A.get(i).getCost())+"\n";
                    for(int x=0;x<getNodo().getParent().getNumberOfChildren();x++){
                        if(getNodo().getParent().getChildAt(x).getData().toString().equals(q.getData().toString())){
                            getNodo().getParent().removeChildAt(x);
                        }
                    }
                    Table_A.get(i).setParent(n);
                    Table_A.get(i).setCost(n.getCost()+costenq);
                    rectifyList();
                    path+="Nodo cambiado: "+Table_A.get(i).getData()+" Nuevo padre: "+Table_A.get(i).getParent().getData()+" Nuevo costo: "+df.format(Table_A.get(i).getCost())+"\n";
                }else{
                    path+="No cumplio\n";
                }
            }
        }
    }
    public void rectifyList(){
        //vacio
    }
    /**
     * Metodo para reordenar la lista de openedList, se crea la lista positionList y abiertaT quienes actuaran como listas temporales.
     * En la lista posiciones se llena con los costes de los cada nodo, se crea la lista abirtosT que crea na copia de los nodos de openedList
     se usa la clase Collections.sort(positionList); la cual ordena listas Int de menor a mayor, se aprovecha esto para asi mismo vaciar y volver
     a llenar la lista abirtas,
     */
    public void reorderOpened(){
        //impAbiertos();
        //impCerrados();
        positionList.clear();
        for(int i=0;i<openedList.size();i++){
            positionList.add(openedList.get(i).getCost());
        }
        openedTList.clear();
        for(int i=0;i<openedList.size();i++){
            openedTList.add(openedList.get(i));
        }
        //JOptionPane.showMessageDialog(null, "posicion1: "+positionList);
        Collections.sort(positionList);
        //JOptionPane.showMessageDialog(null, "posicion2: "+positionList);
        openedList.clear();
        for(int i=0;i<positionList.size();i++){
            setCondition(false);
            for(int j=0;j<openedTList.size();j++){
                if(positionList.get(i)==openedTList.get(j).getCost() && !isCondition()){
                    //JOptionPane.showMessageDialog(null, "fue igual el: "+openedTList.get(i).getData()+" con "+openedTList.get(i).getCost());
                    openedList.add(openedTList.get(j));
                    openedTList.remove(j);
                    setCondition(true);
                }
                //posicion.remove(i);
            }
        }
        //impAbiertos();
        //impCerrados();
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista padresT
     */
    public String impOpened(){
        String acum="abiertos: ";
        for(int i=0;i<openedList.size();i++){
            acum+=openedList.get(i).getData().toString()+" peso: "+openedList.get(i).getCost()+", ";
        }
        //JOptionPane.showMessageDialog(null, acum);
        return acum;
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista padresT
     */
    public String imprecoveredList(){
        String acum="recoveredList: ";
        for(int i=0;i<recoveredList.size();i++){
            acum+=recoveredList.get(i).getData().toString();
        }
        return acum;
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista padresT
     */
    public String impParents(){
        String acum="Padres: ";
        for(int i=0;i<parentList.size();i++){
            acum+=parentList.get(i).getData().toString()+", ";
        }
        //JOptionPane.showMessageDialog(null, acum);
        return acum;
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista padresT
     */
    public String impTable_A(){
        String acum="Table_A: ";
        for(int i=0;i<Table_A.size();i++){
            acum+=Table_A.get(i).getData().toString()+", ";
        }
        //JOptionPane.showMessageDialog(null, acum);
        return acum;
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
    /**
     * Getter y Setter de nodo
     */
    public Node getNodo() {
        return nodo;
    }

    public void setNodo(Node nodo) {
        this.nodo = nodo;
    }
}
