/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package iacalls;

import java.io.IOException;
import static java.lang.System.exit;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 *
 * Programa que realiza una busqueda por medio de grafos A que es una busqueda informada y no solo es eso un una busqueda que contiene un extra
 * para calcular con mayor facilidad y en caso de existir ambiguedad se realiza una distincion de optimizacion de acuerdo a esta condicion con la
 * que se realiza una comparación con la caracteristica f(q)=g(q)+h(q) en donde g(q)=Sumatoria de los pesos desde el inicio hasta el nodo actual, y
 * h(q)= es la condicion de heuristica. Cabe mencionar que la heuristica usada fue la suma de los pesos de los todos los nodos hijos para la elección
 * temprana de un algoritmo correcto. Se diferencia de A porque aqui la condición de objetividad se realiza en los padres.
 * Usa una lista de cerrados y otra de abiertos, esta ultima se reordena de acuerdo a f(q) y tambien se realiza rectificación de acuerdo a g(q)
 */
public class GraphsAst {
    CRUD crud = new CRUD();
    DecimalFormat df = new DecimalFormat("#.00");
    private List<Node<Object>> parentList = new ArrayList<Node<Object>>();
    private List<Node<Object>> openedTList = new ArrayList<Node<Object>>();
    private List<Node<Object>> openedList = new ArrayList<Node<Object>>();
    private List<Node<Object>> closedList = new ArrayList<Node<Object>>();
    private List<Double> positionList = new ArrayList<Double>();
    private List<Node<Object>> recoveredList = new ArrayList<Node<Object>>();
    private List<Node<Object>> comparisonsList = new ArrayList<Node<Object>>();
    private List<Node<Object>> TemporalList = new ArrayList<Node<Object>>();
    Node nodo = null;
    Node parentNode =  null;
    Node childNode = null;
    String key,key_fk;
    String path="\nArbol A estrella:\n";
    double costs,fq,gq,hq;
    char parent_char,child_char;
    boolean condition;
    /**
     * En este metodo pide por teclado el nodo raiz y el nodo
     * @throws IOException
     */
    public void graphsAst() throws IOException{
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
        //padresT.add(parentNode);
        //padre = key.charAt(0);
        if(key.equals(key_fk)){
            System.out.println("\033[33mHAS LLEGADO AL DESTINO\n\033[33m [ "+key+" < "+key+" COSTO: "+0+" ]");
        }else{
            //impAbiertos();
            //impCerrados();
            checkExpansion();
        }
    }
    /**
     * En este metodo se encuentra la logica principal del metodo aqui se reinicia el booleano condition para evitar problemas
     Este metodo revisa si la lista de openedList no esta vacia, si se cumple entra a la condition que es agregar el primer nodo de la lista
     openedList a la lista closedList y el primer nodo de la lista openedList lo quita, despues al metodo de busqueda se le envia la key del
     nodo parent_char actual y este metodo se encarga de buscar en al archivo indice y pasar a buscar de manera secuencial indexada al archivo
     maestro y al finalizar realiza recursividad para probar la lista openedList otra vez, en caso de que se encuentre la lista openedList vacia
     se manda un mensaje con Fin con fallo y termina ejecucion
     * @throws IOException
     */
    public void checkExpansion() throws IOException{
        if(!openedList.isEmpty()){
            parentNode=openedList.get(0);
            //impAbiertos();
            //impCerrados();
            closedList.add(openedList.get(0));
            openedList.remove(0);
            parentList.add(parentNode);
            //JOptionPane.showMessageDialog(null, "parentList: "+impParents());
            if(key_fk.equals(parentNode.getData().toString())){
                end();
            }
            /**
             * Recupera la lista de los hijos de este padre en especifico le aplica condicion de entrada a circuitos pero con verificación
             * la lista de no perminitidos (parentsList) solo guardará los padres ya explorados
             */
            recoveredList.clear();
            TemporalList=crud.recoverList(parentNode);
            fillrecoveredList();
            //JOptionPane.showMessageDialog(null, "recoveredtList: "+imprecoveredList());
            hq++;//Heuristica de calculo
            for(int x=0;x<recoveredList.size();x++){
                setCondition(false);
                findParents(recoveredList.get(x).getData().toString());
                //JOptionPane.showMessageDialog(null, "esta en parentList: "+isCondition()+" child_char: "+child_char+" parent_char: "+parent_char);
                if(isCondition()){
                    setCondition(false);
                }else{
                    /**
                     * En esta sección de codigo se analiza con dos metodos buscarAbiertos() y buscarCerrados() con los que revisa
                     * tanto en abiertos como en cerrados que no se encuentre el nodo actual y si se cumple que no este se agrega abiertos
                     * si no se cumple y si esta en cualquiera de las dos listas y el nuevo nodo es menor en coste al nodo ya registrado
                     * con el mismo nombre, se cambiara a su padre antiguo por el nuevo padre y el costo del recorrido tambien se actualizara
                     * y se manda a un metodo en donde si estaba en cerrados se cambia a abiertos.
                     * {{ Esta condicion dice que si no existe un hijo que se llame asi lo agrega si si solo le cambia la información }}
                     */
                    setCondition(false);
                    setNodo(null);
                    findOpened(recoveredList.get(x).getData().toString());
                    findClosed(recoveredList.get(x).getData().toString());
                    //JOptionPane.showMessageDialog(null, "Lo encontro en abierta o cerrada: "+isCondition()+" child_char: "+child_char+" parent_char: "+parent_char);
                    if(!isCondition()){
                        gq=recoveredList.get(x).getCost()+parentNode.getCost();
                        hq = 0;
                        comparisonsList.clear();
                        TemporalList=crud.recoverList(recoveredList.get(x));
                        fillcomparisonsList();
                        //JOptionPane.showMessageDialog(null, "comparisonList: "+impcomparisonsList());
                        for (int y=0;y<comparisonsList.size();y++){
                            //JOptionPane.showMessageDialog(null,"comparisonsList.get(y): "+comparisonsList.get(y).getData());
                            hq=hq+comparisonsList.get(y).getCost();
                        }
                        fq=gq+hq;
                        childNode = new Node(recoveredList.get(x).getData().toString(),parentNode,gq,fq);
                        parentNode.addChild(childNode);
                        openedList.add(childNode);
                        path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+" coste "+df.format(childNode.getCost())+" \033[33mf(q)= "+df.format(fq)+"\n";
                    }else{
                        rectify(recoveredList.get(x),parentNode,recoveredList.get(x).getCost());
                    }
                }
                //path+="  \033[32mPadre: "+parentNode.getData()+ " hijos: "+parentNode.getChildrenData()+" coste "+df.format(childNode.getCost())+"\n";
            }
            reorderOpened();
            //JOptionPane.showMessageDialog(null, "padre1: "+parentNode.getData());
            checkExpansion();
        }else{
            System.out.println("\n\n\033[33mFin con fallos: Lista 'abiertos' vacia antes de tiempo");
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
        System.out.println("\n\n\033[32m----------------------RESULTADO------------------------------------");
        System.out.println("\033[32mHas encontrado el objetivo: "+key_fk+"\n\033[33mRECORRIDO: [ "+recorrido+" ] COSTE: [ "+df.format(coste)+" ]");
        System.out.println(path);
        exit(0);
    }
    public void rectify(Node q,Node n,double costenq){
        path+="Condicion rectificacion Padre: "+n.getData()+" Nodo: "+q.getData()+" { Antes = "+df.format(getNodo().getCost())+" > Ahora = "+df.format(childNode.getCost())+" }\n";
        //JOptionPane.showMessageDialog(null, "Rectificacion Nodo: "+childNode.getData()+" Antes = "+getNodo().getCost()+" > Ahora = "+childNode.getCost());
        if(n.getCost()+costenq<getNodo().getCost()){
            path+="Si cumplio\n";
            path+="Nodo actual: "+getNodo().getData()+" Padre actual: "+getNodo().getParent().getData()+" Costo actual: "+df.format(getNodo().getCost())+"\n";
            //JOptionPane.showMessageDialog(null, "condition cumplida :)");
            //JOptionPane.showMessageDialog(null,"Nodo actual: "+getNodo().getData()+" Padre actual: "+getNodo().getParent().getData()+" Costo actual: "+getNodo().getCost());
            for(int x=0;x<getNodo().getParent().getNumberOfChildren();x++){
                if(getNodo().getParent().getChildAt(x).getData().toString().equals(q.getData().toString())){
                    getNodo().getParent().removeChildAt(x);
                }
            }
            getNodo().setParent(parentNode);
            getNodo().setCost(gq);
            removeClosed(q);
            //JOptionPane.showMessageDialog(null,"Nodo cambiado: "+getNodo().getData()+" Nuevo parent_char: "+getNodo().getParent().getData()+" Nuevo costs: "+getNodo().getCost());
            path+="Nodo cambiado: "+getNodo().getData()+" Nuevo padre: "+getNodo().getParent().getData()+" Nuevo costo: "+df.format(getNodo().getCost())+"\n";
        }else{
            path+="No cumplio\n";
        }
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
    public void findOpened(String nodo){
        for(int i=0;i<openedList.size();i++){
            if(nodo.equals(openedList.get(i).getData().toString())){
                setCondition(true);
                setNodo(openedList.get(i));
            }
        }
    }
    /**
     * Metodo para buscar una key en la lista de closedList
     * @param nodo
     */
    public void findClosed(String nodo){
        for(int i=0;i<closedList.size();i++){
            if(nodo.equals(closedList.get(i).getData().toString())){
                setCondition(true);
                setNodo(closedList.get(i));
            }
        }
    }
    /**
     * Metodo para elimina un registro en la lista closedList
     * @param nodo
     */
    public void removeClosed(Node nodo){
        for(int i=0;i<closedList.size();i++){
            if(nodo.equals(closedList.get(i))){
                openedList.add(nodo);
                closedList.remove(i);
            }
        }
    }
    /**
     * Metodo para reordenar la lista de openedList, se crea la lista positionList y abiertaT quienes actuaran como listas temporales.
     * En la lista posiciones se llena con los costes de los cada nodo, se crea la lista abirtosT que crea na copia de los nodos de openedList
     se usa la clase Collections.sort(positionList); la cual ordena listas Int de menor a mayor, se aprovecha esto para asi mismo vaciar y volver
     a llenar la lista abirtas.
     */
    public void reorderOpened(){
        //impAbiertos();
        //impCerrados();
        positionList.clear();
        for(int i=0;i<openedList.size();i++){
            positionList.add(openedList.get(i).getHeuristic());
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
                if(positionList.get(i)==openedTList.get(j).getHeuristic() && !isCondition()){
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
     * Este metodo solo sirven para pruebas imprime la lista hijosT
     */
    public void impClosed(){
        String acum="cerrados: ";
        for(int i=0;i<closedList.size();i++){
            acum+=closedList.get(i).getData().toString()+", ";
        }
        JOptionPane.showMessageDialog(null, acum);
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
     * Metodo para llenar recoveredList()
     */
    public void fillrecoveredList(){
        for (int i=0;i<TemporalList.size();i++){
            recoveredList.add(TemporalList.get(i));
        }
    }
    /**
     * Metodo para llenar comparisonList()
     */
    public void fillcomparisonsList(){
        for (int i=0;i<TemporalList.size();i++){
            comparisonsList.add(TemporalList.get(i));
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
