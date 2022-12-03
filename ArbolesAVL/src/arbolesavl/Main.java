/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package arbolesavl;

/**
 *
 * @author Grupo 6
 */
public class Main {

    public static void main(String[] args) {

        ArbolesAVL<Integer> arbol = new ArbolesAVL<Integer>();

        arbol.agregar(110);
        arbol.agregar(170);
        arbol.agregar(200);
        arbol.agregar(210);
        arbol.agregar(140);
        arbol.agregar(90);
        arbol.agregar(20);
        arbol.agregar(7);
        arbol.agregar(130);
        arbol.agregar(3);

        System.out.println("Creacion arbol AVL");
        System.out.print("InOrden ");
        System.out.println(arbol.inOrden());

        System.out.println("\n\nEliminar 200, 3 y 110");

        arbol.eliminar(200);
        arbol.eliminar(3);
        arbol.eliminar(110);

        System.out.print("InOrden ");
        System.out.println(arbol.inOrden());

    }
}
