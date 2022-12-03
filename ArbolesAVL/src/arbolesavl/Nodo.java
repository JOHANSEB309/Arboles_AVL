/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arbolesavl;

/**
 * Clase que contiene los principales atributos de un nodo del Arbol AVL.
 * 
 * @author Grupo 6
 * @param <T> 
 */
public class Nodo<T> {

    //Dato contenido en el nodo.
    private T dato;

    //Nodo hijo de la izquierda.
    private Nodo<T> izquierda;

    //Nodo hijo de la derecha.
    private Nodo<T> derecha;

    private int factorE;

    //Constructor.
    public Nodo() {
        dato = null;
        izquierda = null;
        derecha = null;
        factorE = 0;
    }

    //Constructor con dato.
    public Nodo(T dato) {
        this.dato = dato;
        izquierda = null;
        derecha = null;
        factorE = 0;
    }

    //Getters y Setters.
    public Nodo<T> getIzquierda() {
        return izquierda;
    }

    public Nodo<T> getDerecha() {
        return derecha;
    }

    public T getDato() {
        return dato;
    }

    public void setDerecha(Nodo<T> derecha) {
        this.derecha = derecha;
    }

    public void setIzquierda(Nodo<T> izquierda) {
        this.izquierda = izquierda;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    //función para encontrar el factor de equilibrio
    public int getFactorEq() {
        int altDer = 0;
        int altIzq = 0;
        if (this.getDerecha() != null) {
            altDer = this.getDerecha().getAltura();
        }
        if (this.getIzquierda() != null) {
            altIzq = this.getIzquierda().getAltura();
        }
        return (altDer - altIzq);
    }

    public void setFactorEq(int fe) {
        this.factorE = fe;
    }
    public int getAltura() {
        int hIzq = 0;
        int hDer = 0;

        if (this.getDato() == null) {
            return 0;
        }

        if (this.getIzquierda() != null) {
            hIzq = this.getIzquierda().getAltura();
        } else {
            hIzq = 0;
        }

        if (this.getDerecha() != null) {
            hDer = this.getDerecha().getAltura();
        } else {
            hDer = 0;
        }
        return Math.max(hIzq, hDer) + 1;
    }
}
