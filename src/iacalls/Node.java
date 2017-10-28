package iacalls;
import java.util.ArrayList;
import java.util.List;
/**
 * @param <T>
 */
public class Node<T> {
    private List<Node<T>> children = new ArrayList<Node<T>>();
    private Node<T> parent = null;
    private Node<T> root = null;
    private T data = null;
    private double cost = 0.0;
    private double heuristic = 0.0;
    /**
     * Constructor que sirve para cuando se crea un nodo con un valor objeto
     * @param data
     */
    public Node(T data) {
        this.data = data;
    }
    /**
     * Constructor que sirve cuando se crea un nodo con un objeto y un nodo padre
     * @param data
     * @param parent
     */
    public Node(T data, Node<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    /**
     * Constructor que sirve cuando se crea un nodo con objeto, padre y costo
     * @param data
     * @param parent
     * @param cost
     */
    public Node(T data, Node<T> parent,double cost) {
        this.data = data;
        this.parent = parent;
        this.cost = cost;
    }

    /**
     * Constructor que sirve cuando se crea un nodo con objeto, padre, costo y heuristica
     * @param data
     * @param parent
     * @param cost
     * @param heuristic
     */
    public Node(T data, Node<T> parent,double cost,double heuristic) {
        this.data = data;
        this.parent = parent;
        this.cost = cost;
        this.heuristic = heuristic;
    }
    /**
     * Devuelve el nombre los hijos del nodo
     * @return
     */
    public List<T> getChildrenData() {
        List<T> lista = new ArrayList<T>();
        for(int i=0;i<children.size();i++){
            lista.add(children.get(i).data);
        }
        return lista;
    }
    /**
     * Devuelve la direccion temporal de los hijos del nodo
     * @return
     */
    public List<Node<T>> getChildren() {
        return children;
    }
    /**
     * Devuelve el numero de hijos que posee un padre
     * @return
     */
    public int getNumberOfChildren() {
        if (children == null) {
            return 0;
        }
        return children.size();
    }
    /**
     * Devuelve el nodo hijo en la posicion especificada
     * @param index
     * @return
     */
    public Node<T> getChildAt(int index) {
        return children.get(index);
    }
    /**
     * Devuelve el nombre del nodo hijo en la posicion especificada
     * @param index
     * @return
     */
    public T getChildDataAt(int index) {
        return children.get(index).data;
    }
    /**
     * Agrega el padre del nodo
     * @param parent
     */
    public void setParent(Node<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }
    /**
     * Devuelve la informacion el padre del nodo
     * @return
     */
    public T getParentData() {
        return parent.data;
    }
    /**
     * Devuelve el nodo padre
     * @return
     */
    public Node<T> getParent() {
        return parent;
    }
    /**
     * Define el nodo root
     * @param parent
     */
    public void setRoot(Node<T> root) {
        this.root = root;
    }
    /**
     * Devuelve el nodo root
     * @return
     */
    public T getRoot() {
        return root.data;
    }
    /**
     * Agrega un nuevo hijo al nodo
     * @param child
     */
    public void addChild(Node<T> child) {
        //child.setParent(this);
        this.children.add(child);
    }
    /**
     * Obtiene los datos inmediatos de un nodo
     * @return
     */
    public T getData() {
        return this.data;
    }
    /**
     * Este no se pa que existe, ni hace nada
     * @param data
     */
    public void setData(T data) {
        this.data = data;
    }
    /**
     * Obtiene la distancia de un nodo
     * @return
     */
    public double getCost() {
        return this.cost;
    }
    /**
     * Establece el costo del nodo
     * @param data
     */
    public void setCost(double cost) {
        this.cost = cost;
    }
    /**
     * Obtiene la heristica del nodo
     * @return
     */
    public double getHeuristic() {
        return this.heuristic;
    }
    /**
     * Esablece la heristica del nodo
     * @param data
     */
    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }
    /**
     * Comprueba si el nodo es raiz
     * @return boolean
     */
    public boolean isRoot() {
        return (this.parent == null);
    }
    /**
     * Comprueba si el nodo es hoja (nodo sin hijos)
     * @return
     */
    public boolean isLeaf() {
        if(this.children.size() == 0)
            return true;
        else
            return false;
    }
    /**
     * Elimina padre de nodo
     */
    public void removeParent() {
        this.parent = null;
    }
    /**
     * Quita a un nodo hijo de su padre
     */
    public void removeChildAt(int index) {
        this.children.remove(index);
    }
}
