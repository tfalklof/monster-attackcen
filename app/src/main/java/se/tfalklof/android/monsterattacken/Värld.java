package se.tfalklof.android.monsterattacken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Värld {
    private final int _världNummer;
    private final Map<Integer, Bana> _banor;

    public Värld(int världNummer) {
        _världNummer = världNummer;
        _banor = new HashMap<>();
    }

    public void läggTillBana(Bana bana) {
        if(bana == null) {
            return;
        }
        _banor.put(bana.banNummer(), bana);
    }

    public Bana hämtaBana(int banNummer) {
        return _banor.get(banNummer);
    }

    @Override
    public String toString() {
        return "Värld{" +
                "_världNummer=" + _världNummer +
                ", _banor=" + _banor +
                '}';
    }

    public int antalBanor() {
        return _banor.size();
    }
}
