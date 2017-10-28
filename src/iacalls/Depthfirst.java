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
 * Programa que busca por profundidad, utiliza 3 listas, exploredList para evitar circuitos, expandedList para ver que nodos ya fueron expandedList
 * y pendingList con los nodos que se exploraron pero no se expandieron, es decir, se quedaron pendingList.
 */
public class Depthfirst {
    CRUD crud = new CRUD();
    private List<Node<Object>> exploredList = new ArrayList<Node<Object>>();
    private List<Node<Object>> expandedList = new ArrayList<Node<Object>>();
    private List<Node<Object>> pendingList = new ArrayList<Node<Object>>();
    private List<Node<Object>> recoveredList = new ArrayList<Node<Object>>();
    Node parentNode =  null;
    Node childNode = null;
    String key,key_fk;
    String path="Arbol por profundidad:\n";
    double costs;
    char parent_char,child_char;
    boolean condition;
    /**
     * En este metodo pide por teclado el nodo raiz y el nodo
     * @throws IOException
     */
    public void depthfirst() throws IOException{
        Scanner entrada=new Scanner(System.in);
        System.out.println("\nIntroduce la llave inicial de la busqueda: ");
        key=entrada.next();
        System.out.println("\nIntroduce la llave destino: ");
        key_fk=entrada.next();

        /**
         * Se ingresa el primer nodo y se designa como raiz
         */
        parentNode = new Node(key,null);
        exploredList.add(parentNode);
        //padresT.add(parentNode);
        if(key.equals(key_fk)){
            System.out.println("\033[32mHAS LLEGADO AL DESTINO\n\033[32m"+key+" < "+key);
        }else{
            checkExpansion();
        }
    }
    /**
     * Metodo dode se esncuentra toda la logica principal
     Si esta en la lista expandedList
     si el padre no es null
     el nodo actual se volver el padre del nodo actual (subira un nivel)
     se realiza un ciclo para comprobar hijo por hijo si alguno se encuentra en la lista de pendientes
     si si se cambia el nodo actual a ese nodo hijo y se quita de la lista de pendientes
     se aplica recursividad e inicia el metodo otra vez
     si el padre es null
     se termina ya que esto significa que el nodo no se encontro ya que su padre esta en lista expandidos y es la raiz
     si no esta en la lista ezpandidos
     se agrega a la lista expandidos
     se manda al metodo crud.recoverList() que expande todos sus hijos y agrega cada uno a la lista de expandidos
     si el nodo actual no es hoja
     si el numero de hijos es mas de 1
     se agrega cada uno del segundo en adelante a la lista de pendientes
     y se toma el primer nodo como el camino a seguir (es decir, el primer nodo se vuelve el nuevo nodo actual)
     si el numero de hijos es solo 1
     solo se toma el primer nodo como camino a seguir
     si el nodo actual es hoja
     el nodo actual se vuelve el padre del nodo actual (subira un nivel)
     se realiza un ciclo para comprobar hijo por hijo si alguno se encuentra en la lista de pendientes
     si si se cambia el nodo actual a ese nodo hijo y se quita de la lista de pendientes
     se aplica recursividad e inicia el metodo otra vez (esto significa que se tiene nodos hijo pendientes)
     si no igual se aplica recursividad
     (esto significa que ya no se tienen hijos en pendientes y tiene que subir otro nivel)
     (como ya esta en expandidos entrará a la primer condition 'si esta en expandidos' se subira un nivel mas)
     * @throws IOException
     */
    public void checkExpansion() throws IOException{
        setCondition(false);
        findExplored(parentNode);
        if(isCondition()){
            setCondition(false);
            if(parentNode.getParent()!=null){
                path+="Nodo sin hijos pendientes: "+parentNode.getData()+" regresa con nodo padre -> "+parentNode.getParent().getData()+"\n";
                parentNode=parentNode.getParent();
                for(int i=0;i<parentNode.getNumberOfChildren();i++){
                    setCondition(false);
                    findPending(parentNode.getChildAt(i));
                    if(isCondition()){
                        parentNode=parentNode.getChildAt(i);
                        removePending(parentNode);
                        //JOptionPane.showMessageDialog(null, "nodo: "+parentNode.getData()+" hijos: "+parentNode.getChildrenData()+"\n"+impPending());
                        checkExpansion();
                    }
                }
                checkExpansion();
            }else{
                System.out.println("\n\n\033[33m----------------------RESULTADO------------------------------------");
                System.out.println("\033[33mNo se encontro el nodo\n\033[33mSe acabo el arbol");
                //System.out.println(path);
                exit(0);
            }
        }else{
            expandedList.add(parentNode);
            /**
             * El siguiente for junto con la lista de los nodos hijos recuperados realiza la condicion si es que ese hijo ya se agrego si no
             * se agrega como hijo de este padre
             */
            recoveredList=crud.recoverList(parentNode);
            if(recoveredList.size()==0){
                System.out.println("\n\n\033[33m----------------------RESULTADO------------------------------------");
                System.out.println("\033[33mNo existe el nodo en el arbol");
                //System.out.println(path);
                exit(0);
            }
            for(int x=0;x<recoveredList.size();x++){
                setCondition(false);
                buscarExplorar(recoveredList.get(x).getData().toString());
                if(isCondition()){
                    setCondition(false);
                }else{
                    childNode = new Node(recoveredList.get(x).getData().toString(),recoveredList.get(x).getParent());
                    parentNode.addChild(childNode);
                    exploredList.add(childNode);
                    //hijosT.add(childNode);

                    path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+"\n";
                    if(key_fk.equals(recoveredList.get(x).getData().toString())){
                        end();
                    }
                }
            }
            if(!parentNode.isLeaf()){
                if(parentNode.getNumberOfChildren()>1){
                    for(int i=1;i<parentNode.getNumberOfChildren();i++){ //inicia en 1 porque se omite el primero donde va a cambiar a parentNode
                        pendingList.add(parentNode.getChildAt(i));
                    }
                    //JOptionPane.showMessageDialog(null, "nodo: "+parentNode.getData()+" hijos: "+parentNode.getChildrenData()+"\n"+impPending());
                    parentNode=parentNode.getChildAt(0);
                }else{
                    parentNode=parentNode.getChildAt(0);
                }
                checkExpansion();
            }else{
                path+="Nodo hoja: "+parentNode.getData()+" regresa con nodo padre -> "+parentNode.getParent().getData()+"\n";
                parentNode=parentNode.getParent();
                for(int i=0;i<parentNode.getNumberOfChildren();i++){
                    setCondition(false);
                    findPending(parentNode.getChildAt(i));
                    if(isCondition()){
                        parentNode=parentNode.getChildAt(i);
                        removePending(parentNode);
                        //JOptionPane.showMessageDialog(null, "nodo: "+parentNode.getData()+" hijos: "+parentNode.getChildrenData()+"\n"+impPending());
                        checkExpansion();
                    }
                }
                //arbol+="Nodo hoja: regresa con nodo parent_char -> "+parentNode.getParent().getData()+"\n";
                checkExpansion();
            }
        }
    }

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
     * Metodos para buscar una key dentro de la lista de pendingList
     * @param llave
     */
    public void findPending(Node nodo){
        for(int i=0;i<pendingList.size();i++){
            if(nodo.equals(pendingList.get(i)))
                setCondition(true);
        }
    }
    /**
     * Metodos para buscar una key dentro de la lista de exploredList
     * @param llave
     */
    public void buscarExplorar(String nodo){
        for(int i=0;i<exploredList.size();i++){
            //JOptionPane.showMessageDialog(null, "nodo = "+nodo+" nodo explorado = "+exploredList.get(i).getData().toString());
            if(nodo.equals(exploredList.get(i).getData().toString()))
                setCondition(true);
        }
    }
    /**
     * Metodo para buscar una key en la lista de expandedList
     * @param llave
     */
    public void findExplored(Node nodo){
        for(int i=0;i<expandedList.size();i++){
            if(nodo.equals(expandedList.get(i)))
                setCondition(true);
        }
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista exploredList
     */
    public void impExplored(){
        String acum="Expandir: ";
        for(int i=0;i<exploredList.size();i++){
            acum+=exploredList.get(i).getData().toString()+", ";
        }
        JOptionPane.showMessageDialog(null, acum);
    }
    /**
     * Este metodo solo sirven para pruebas imprime la lista pendingList
     */
    public String impPending(){
        String acum="Pendientes: ";
        for(int i=0;i<pendingList.size();i++){
            acum+=pendingList.get(i).getData().toString()+", ";
        }
        return acum;
        //JOptionPane.showMessageDialog(null, acum);
    }
    /**
     * Este metodo se usa para eliminar un nodo especifico de la lista de pendingList
     * @param nodo
     */
    public void removePending(Node nodo){
        for(int i=0;i<pendingList.size();i++){
            if(pendingList.get(i).equals(nodo)){
                pendingList.remove(i);
            }
        }
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
