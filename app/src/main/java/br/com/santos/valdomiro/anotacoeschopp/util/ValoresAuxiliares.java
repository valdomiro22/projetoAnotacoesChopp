package br.com.santos.valdomiro.anotacoeschopp.util;

import java.util.ArrayList;
import java.util.List;

import br.com.santos.valdomiro.anotacoeschopp.model.Nota;

public class ValoresAuxiliares {
    public static List<Nota> listaDeNotas = new ArrayList<>();


    public static void main(String[] args) {
        for (int i = 0; i < 260; i++) {
            Nota nota = new Nota();
            nota.setId(i);
            nota.setCodigo( String.valueOf (i + " - " + 323));
            nota.setHora(String.valueOf(i) + ":32:3");

            listaDeNotas.add(nota);
        }
    }
}
