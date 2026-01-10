package pt.ipleiria.estg.dei.amsi.homepantry.data;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

public class LocalRepository {

    private static final ArrayList<Local> listaLocais = new ArrayList<>();

    public static ArrayList<Local> getLocais() {
        return listaLocais;
    }

    public static void adicionarLocal(Local local) {
        listaLocais.add(local);
    }
}
