package pt.ipleiria.estg.dei.amsi.homepantry.listeners;

import java.util.ArrayList;

import pt.ipleiria.estg.dei.amsi.homepantry.modelos.Local;

public interface LocalListener {

    void onGetLocais(ArrayList<Local> locais);

    void onError(String erro);

    void onLocalClick(int localId, String nomeLocal);

}







