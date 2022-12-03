/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package arbolesavl;

import java.util.*;

/**
 * Clase que contiene todas las operaciones realizables para agregar, eliminar,
 * balancear y recorrer arboles AVL.
 *
 * @author Grupo 6
 * @param <T>
 */
public class ArbolesAVL<T> extends java.util.AbstractSet<T> {

    //Nodo raiz del arbol.
    private Nodo<T> raiz;

// Comparador.
    Comparator<T> comparador;

    //Constructor.
    public ArbolesAVL() {
    }

    //Constructor con argumento Comparador.
    public ArbolesAVL(Comparator<T> cmp) {
        this.comparador = cmp;
    }

    //Función para agregar un nodo al arbol
    public boolean agregar(T e) throws ClassCastException, NullPointerException, IllegalStateException {

        Nodo<T> nodo = new Nodo<T>(e);
        boolean salir = false;
        boolean der = false;
        Nodo<T> raizTmp = this.getRaiz();

        int altIzq, altDer;

        //no existía arbol
        if (raizTmp == null) {
            this.raiz = nodo;
            return true;
        } else //estaba ya en el arbol?
        if (this.contains(nodo.getDato())) {
            return false;
        } //no estaba antes en el arbol
        else {
            while (!salir) {

                //es mayor el nodo a insertar que la raiz?
                if (this.compararDato(nodo.getDato(), raizTmp.getDato()) > 0) {
                    if (raizTmp.getDerecha() != null) {
                        raizTmp = raizTmp.getDerecha();
                    } else {
                        salir = true;
                        der = true;
                    }

                } //el nodo es menor que la raiz
                else {
                    if (raizTmp.getIzquierda() != null) {
                        raizTmp = raizTmp.getIzquierda();
                    } else {
                        salir = true;
                    }
                }
            }

            if (der) {
                raizTmp.setDerecha(nodo);
            } //lo inserto a la izquierda
            else {
                raizTmp.setIzquierda(nodo);
            }

            //mientras no este equilibrado el arbol
            while (equilibrado(this.getRaiz()) < 0) {
                raizTmp = padre(raizTmp);

                if (raizTmp.getDerecha() == null) {
                    altDer = 0;
                } else {
                    altDer = raizTmp.getDerecha().getAltura();
                }

                if (raizTmp.getIzquierda() == null) {
                    altIzq = 0;
                } else {
                    altIzq = raizTmp.getIzquierda().getAltura();
                }

                Nodo<T> cambiar = balancear(raizTmp, altIzq, altDer);
                Nodo<T> superior = padre(raizTmp);

                //si los nodos modificados tenian un padre anteriormente
                if (compararDato(superior.getDato(), raizTmp.getDato()) != 0) {
                    if (superior.getIzquierda() != null && compararDato(superior.getIzquierda().getDato(), raizTmp.getDato()) == 0) {
                        superior.setIzquierda(cambiar);
                    } else if (superior.getDerecha() != null && compararDato(superior.getDerecha().getDato(), raizTmp.getDato()) == 0) {
                        superior.setDerecha(cambiar);
                    }
                } else {
                    this.raiz = cambiar;
                }
            }
            return true;
        }
    }

    //Accion de remover un nodo del arbol
    public boolean eliminar(Object o) throws ClassCastException, NullPointerException {
        Nodo<T> borrar = null, mirar = null, cambiar = null, nPadre = null;
        Nodo<T> raizTmp = this.getRaiz();
        T c_aux, d_aux;
        boolean salir = false;
        int altDer = 0;
        int altIzq = 0;
        int a = 0;

        if (this.isEmpty()) {
            return false;
        }

        //el nodo a borrar es la raiz?
        if (this.compararDato((T) o, raizTmp.getDato()) == 0) {
            salir = true;
            borrar = raizTmp;
        }

        //si no es la raiz, lo buscamos
        while (!salir && (raizTmp.getDerecha() != null || raizTmp.getIzquierda() != null)) {

            if (this.compararDato((T) o, raizTmp.getDato()) > 0) {
                if (raizTmp.getDerecha() != null) {
                    raizTmp = raizTmp.getDerecha();
                } else {
                    return false;
                }
            } else if (this.compararDato((T) o, raizTmp.getDato()) < 0) {

                if (raizTmp.getIzquierda() != null) {
                    raizTmp = raizTmp.getIzquierda();
                } else {
                    return false;
                }
            }

            if (this.compararDato((T) o, raizTmp.getDato()) == 0) {
                salir = true;
                borrar = raizTmp;
            }
        }

        //existe el nodo a borrar?
        if (salir) {
            mirar = borrar;

            //es una hoja?
            if (borrar.getIzquierda() == null && borrar.getDerecha() == null) {
                mirar = padre(borrar);
                nPadre = padre(borrar);

                //es un arbol raiz con solo un nodo raiz?
                if (this.size() == 1) {
                    this.raiz = null;
                }

                if (nPadre.getIzquierda() != null && compararDato(nPadre.getIzquierda().getDato(), borrar.getDato()) == 0) {
                    nPadre.setIzquierda(null);
                } else if (nPadre.getDerecha() != null && compararDato(nPadre.getDerecha().getDato(), borrar.getDato()) == 0) {
                    nPadre.setDerecha(null);
                }
                //nos lo cargamos
                borrar.setDato(null);
            } //solo tiene un hijo? (o 2 pero en la misma altura) entonces la altura de ese subarbol será 1 o 2 (altura raiz = 1)
            else if (borrar.getAltura() <= 2) {

                if (borrar.getIzquierda() != null) {
                    borrar.setDato(borrar.getIzquierda().getDato());
                    borrar.setIzquierda(null);
                } else if (borrar.getDerecha() != null) {
                    borrar.setDato(borrar.getDerecha().getDato());
                    borrar.setDerecha(null);
                }
            } //cuando no es ni un hoja ni su padre. Es decir, está por medio del arbol.
            else {

                //buscamos el mayor de la izquierda
                if (borrar.getIzquierda() != null) {
                    cambiar = borrar.getIzquierda();

                    while (cambiar.getDerecha() != null) {
                        cambiar = cambiar.getDerecha();
                    }
                } //buscamos el menor de la derecha
                else if (borrar.getDerecha() != null) {
                    cambiar = cambiar.getDerecha();

                    while (cambiar.getIzquierda() != null) {
                        cambiar = cambiar.getIzquierda();
                    }
                }

                c_aux = cambiar.getDato();
                Nodo<T> papa = padre(cambiar);

                //si el nodo que hemos cambiado se ha quedado con algún hijo...
                if (cambiar.getIzquierda() != null || cambiar.getDerecha() != null) {
                    if (cambiar.getIzquierda() != null) {
                        cambiar.setDato(cambiar.getIzquierda().getDato());
                        cambiar.setIzquierda(null);
                    } else if (cambiar.getDerecha() != null) {
                        cambiar.setDato(cambiar.getDerecha().getDato());
                        cambiar.setDerecha(null);
                    }
                } //si no tiene hijos ya, lo eliminamos sin más
                else {
                    if (papa.getIzquierda() != null && compararDato(papa.getIzquierda().getDato(), cambiar.getDato()) == 0) {
                        papa.setIzquierda(null);
                    } else {
                        papa.setDerecha(null);
                    }
                    cambiar.setDato(borrar.getDato());
                    borrar.setDato(c_aux);
                }
            }

            while (equilibrado(this.getRaiz()) < 0) {
                if (mirar.getDerecha() == null) {
                    altDer = 0;
                } else {
                    altDer = mirar.getDerecha().getAltura();
                }

                if (mirar.getIzquierda() == null) {
                    altIzq = 0;
                } else {
                    altIzq = mirar.getIzquierda().getAltura();
                }

                Nodo<T> cambiar2 = balancear(mirar, altIzq, altDer);
                Nodo<T> superior = padre(mirar);

                //si los nodos modificados tenian un padre anteriormente
                if (compararDato(superior.getDato(), mirar.getDato()) != 0) {
                    if (superior.getIzquierda() != null && compararDato(superior.getIzquierda().getDato(), mirar.getDato()) == 0) {
                        superior.setIzquierda(cambiar2);
                    } else if (superior.getDerecha() != null && compararDato(superior.getDerecha().getDato(), mirar.getDato()) == 0) {
                        superior.setDerecha(cambiar2);
                    }
                } else {
                    this.raiz = cambiar2;
                }
                mirar = padre(mirar);
            }
            return true;
        }
        return false;
    }

    /*
     *************Rotaciones para balancear el arbol*************
     */
    private Nodo<T> balancear(Nodo<T> nodo, int altIzq, int altDer) {
        if (extraeFactorE(nodo) == 2) {
            if (extraeFactorE(nodo.getDerecha()) == 1 || extraeFactorE(nodo.getDerecha()) == 0) {
                nodo = rotacionSimpleIzquierda(nodo); //rotación simple a la izquierda
            } else if (extraeFactorE(nodo.getDerecha()) == -1) {
                nodo = rotacionDobleDerecha(nodo);//rotación simple a la izquierda
            }
        } else if (extraeFactorE(nodo) == -2) {
            if (extraeFactorE(nodo.getIzquierda()) == -1 || extraeFactorE(nodo.getDerecha()) == 0) {
                nodo = rotacionSimpleDerecha(nodo);
            } else if (extraeFactorE(nodo.getIzquierda()) == 1) {
                nodo = rotacionDobleIzquierda(nodo);
            }
        }

        return nodo;
    }

    public int extraeFactorE(Nodo<T> nodo) {
        if (nodo != null) {
            return nodo.getFactorEq();
        } else {
            return 0;
        }
    }

    public Nodo<T> rotacionSimpleIzquierda(Nodo<T> nodo) {
        Nodo<T> nodoTmp = nodo;

        nodo = nodoTmp.getDerecha(); //clone??
        nodoTmp.setDerecha(nodo.getIzquierda());

        nodo.setIzquierda(nodoTmp);

        return nodo;
    }

    public Nodo<T> rotacionSimpleDerecha(Nodo<T> nodo) {
        Nodo<T> nodoTmp = nodo;
        nodo = nodoTmp.getIzquierda();

        nodoTmp.setIzquierda(nodo.getDerecha());
        nodo.setDerecha(nodoTmp);

        return nodo;
    }

    public Nodo<T> rotacionDobleIzquierda(Nodo<T> nodo) {
        Nodo<T> nodoTmp = nodo; //57

        nodoTmp = rotacionSimpleIzquierda(nodoTmp.getIzquierda()); //param 42 | sale: 54

        nodo.setIzquierda(nodoTmp); //param 54

        nodoTmp = rotacionSimpleDerecha(nodo); //param 54  | sale: 54

        return nodoTmp;
    }

    public Nodo<T> rotacionDobleDerecha(Nodo<T> nodo) {
        Nodo<T> nodoTmp = nodo;

        nodoTmp = rotacionSimpleDerecha(nodoTmp.getDerecha());

        nodo.setDerecha(nodoTmp);

        nodoTmp = rotacionSimpleIzquierda(nodo);

        return nodoTmp;
    }

    public int equilibrado(Nodo<T> n) {
        int hIzq = 0;
        int hDer = 0;

        if (n == null) {
            return 0;
        }

        hIzq = equilibrado(n.getIzquierda());

        if (hIzq < 0) {
            return hIzq;
        }

        hDer = equilibrado(n.getDerecha());

        if (hDer < 0) {
            return hDer;
        }

        //si no es equilibrado
        if (Math.abs(hIzq - hDer) > 1) {
            return -1;
        }

        return Math.max(hIzq, hDer) + 1;
    }

    public Nodo<T> padre(Nodo<T> nodo) {
        Nodo<T> raizTmp = this.getRaiz();
        Stack<Nodo<T>> pila = new Stack<Nodo<T>>();
        pila.push(raizTmp);
        while (raizTmp.getDerecha() != null || raizTmp.getIzquierda() != null) {
            if (this.compararDato(nodo.getDato(), raizTmp.getDato()) > 0) {
                if (raizTmp.getDerecha() != null) {
                    raizTmp = raizTmp.getDerecha();
                }
            } else if (this.compararDato(nodo.getDato(), raizTmp.getDato()) < 0) {
                if (raizTmp.getIzquierda() != null) {
                    raizTmp = raizTmp.getIzquierda();
                }
            }
            if (this.compararDato(nodo.getDato(), raizTmp.getDato()) == 0) {
                return pila.pop();
            }

            pila.push(raizTmp);
        }
        return pila.pop();
    }

    /**
     *****************Recorridos para el arbol AVL.*****************
     */
    public List<T> inOrden() {
        List<T> lista = new ArrayList<T>();
        Nodo<T> nodo = this.getRaiz();
        Stack<Nodo<T>> pila = new Stack<Nodo<T>>();

        while ((nodo != null && nodo.getDato() != null) || !pila.empty()) {
            if (nodo != null) {
                pila.push(nodo);
                nodo = nodo.getIzquierda();
            } else {
                nodo = pila.pop();
                lista.add(nodo.getDato());
                nodo = nodo.getDerecha();
            }
        }

        return lista;
    }

    public List<T> preOrden() {
        List<T> lista = new ArrayList<T>();
        Nodo<T> nodo = this.getRaiz();
        Stack<Nodo<T>> pila = new Stack<Nodo<T>>();

        while ((nodo != null && nodo.getDato() != null) || !pila.empty()) {
            if (nodo != null) {
                lista.add(nodo.getDato());
                pila.push(nodo);
                nodo = nodo.getIzquierda();
            } else {
                nodo = pila.pop();
                nodo = nodo.getDerecha();
            }
        }

        return lista;
    }

    public List<T> postOrden() {
        List<T> lista = new ArrayList<T>();
        Nodo<T> nodo = this.getRaiz();
        Stack<Nodo<T>> pila1 = new Stack<Nodo<T>>();
        Stack<Boolean> pila2 = new Stack<Boolean>();

        while ((nodo != null && nodo.getDato() != null) || !pila1.empty()) {

            if (nodo != null) {
                pila1.push(nodo);
                pila2.push(true);
                nodo = nodo.getIzquierda();
            } else {
                nodo = pila1.pop();
                if (pila2.pop()) {
                    pila1.push(nodo);
                    pila2.push(false);
                    nodo = nodo.getDerecha();
                } else {
                    lista.add(nodo.getDato());
                    nodo = null;
                }
            }
        }

        return lista;
    }

    //Altura del nodo
    public int altura(T dato) {
        Nodo<T> nodo = this.getNodo(dato);
        if (!this.contains(dato)) {
            return -1;
        }

        return nodo.getAltura();
    }

    //Altura del Arbol
    public int profundidad(T dato) {
        Nodo<T> nodo = new Nodo<T>(dato);
        int profundidad = 0;
        while (compararDato(nodo.getDato(), this.getRaiz().getDato()) != 0) {
            profundidad++;
            nodo = padre(nodo);
        }

        return profundidad;
    }

    /*
    **********************Getters y Setters**********************
     */
    public Nodo<T> getRaiz() {
        return this.raiz;
    }

    public Nodo<T> getNodo(T dato) {
        Nodo<T> raizTmp = this.getRaiz();

        if (this.isEmpty()) {
            return null;
        }

        while (raizTmp.getDerecha() != null || raizTmp.getIzquierda() != null) {

            if (this.compararDato(dato, raizTmp.getDato()) > 0) {
                if (raizTmp.getDerecha() != null) {
                    raizTmp = raizTmp.getDerecha();
                } else {
                    return null;
                }
            } else if (this.compararDato(dato, raizTmp.getDato()) < 0) {
                if (raizTmp.getIzquierda() != null) {
                    raizTmp = raizTmp.getIzquierda();
                } else {
                    return null;
                }
            }

            if (this.compararDato(dato, raizTmp.getDato()) == 0) {
                return raizTmp;
            }
        }

        return raizTmp;
    }

    private Comparator<T> getComparator() {
        return this.comparador;
    }

    public T extraeDato(Nodo<T> nodo) {
        return nodo.getDato();
    }

    private int compararDato(T t1, T t2) {
        if (this.comparador == null) {
            return ((Comparable) t1).compareTo(t2);
        } else {
            return this.comparador.compare(t1, t2);
        }
    }

    /**
     * ******************Partes escenciales de la Libreria: Implementaciones de
     * los atributos abstractos de la libreria*****************************
     */
    // parte escencial de la libreria
    public boolean addAll(Collection<? extends T> c) throws ClassCastException, NullPointerException, IllegalStateException {
        Iterator<? extends T> iter = c.iterator();
        Iterator<? extends T> iter2 = c.iterator();
        T dato, primero;
        boolean insertado = false;

        //si el arbol no existe
        if (this.isEmpty()) {
            //comprobamos que sean comparables entre si. Sino, salta excepcion.
            primero = iter.next();
            while (iter.hasNext()) {
                this.compararDato(primero, iter.next());
            }
        } //el arbol ya existía
        else {
            //comprobamos que los datos sean comparables con los que ya están dentro del arbol
            primero = this.getRaiz().getDato();
            while (iter.hasNext()) {
                this.compararDato(primero, iter.next());
            }
        }

        //solo llega hasta aqui si los elementos son comparables entre si o con los que ya había
        while (iter2.hasNext()) {
            dato = iter2.next();
            if (dato != null) {
                if (this.add(dato)) {
                    insertado = true;
                }
            }
        }
        return insertado;
    }

    // parte escencial de la libreria
    public void clear() {
        Iterator<T> iter = this.iterator();

        while (iter.hasNext()) {
            remove(iter.next());
        }
    }

    // parte escencial de la libreria
    public boolean contains(Object o) throws ClassCastException, NullPointerException {
        Nodo<T> raizTmp = this.getRaiz();
        if (this.isEmpty()) {
            return false;
        }

        //si es la raiz el buscado
        if (this.compararDato((T) o, raizTmp.getDato()) == 0) {
            return true;
        }

        while (raizTmp.getDerecha() != null || raizTmp.getIzquierda() != null) {

            if (this.compararDato((T) o, raizTmp.getDato()) > 0) {
                if (raizTmp.getDerecha() != null) {
                    raizTmp = raizTmp.getDerecha();
                } else {
                    return false;
                }
            } else if (this.compararDato((T) o, raizTmp.getDato()) < 0) {
                if (raizTmp.getIzquierda() != null) {
                    raizTmp = raizTmp.getIzquierda();
                } else {
                    return false;
                }
            }

            if (this.compararDato((T) o, raizTmp.getDato()) == 0) {
                return true;
            }
        }
        return false;
    }

    // parte escencial de la libreria
    public boolean containsAll(Collection<?> c) throws ClassCastException, NullPointerException {
        Iterator<?> iter = c.iterator();
        List<?> listaArbol = this.inOrden();
        T dato = null;

        if (this.isEmpty()) {
            return false;
        }

        while (iter.hasNext()) {
            dato = (T) iter.next();

            if (!listaArbol.contains(dato)) {
                return false;
            }
        }
        return true;
    }

    // parte escencial de la libreria
    public boolean isEmpty() {
        return this.size() == 0;
        //?? tal vez this.getRaiz()==null?
    }

    // parte escencial de la libreria
    public Iterator<T> iterator() {
        List<T> lista = this.inOrden();
        Iterator<T> iter = lista.iterator();

        return iter;
    }

    // parte escencial de la libreria
    public boolean removeAll(Collection<?> c) throws ClassCastException, NullPointerException {
        T dato;
        boolean noBorrado = false;

        Iterator<?> iter = c.iterator();
        while (iter.hasNext()) {
            dato = (T) iter.next();

            if (remove(dato)) {
                noBorrado = true;
            }
        }

        return noBorrado;
    }

    // parte escencial de la libreria
    public boolean retainAll(Collection<?> c) throws ClassCastException, NullPointerException {
        List<T> listaArbol = this.preOrden();
        List<T> listaBorrar = new ArrayList<T>();
        T dato;
        boolean modificada = false;

        for (int i = 0; i < listaArbol.size(); i++) {
            if (!c.contains(listaArbol.get(i))) {
                listaBorrar.add(listaArbol.get(i));
            }
        }

        Iterator<?> iter = listaBorrar.iterator();
        while (iter.hasNext()) {
            modificada = true;
            dato = (T) iter.next();

            remove(dato);
        }

        return modificada;
    }

    // parte escencial de la libreria
    public int size() {
        return this.preOrden().size();
    }
}
