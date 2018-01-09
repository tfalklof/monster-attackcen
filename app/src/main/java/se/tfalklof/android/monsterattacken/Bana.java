package se.tfalklof.android.monsterattacken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Bana {
    /* Spelsaker */
    private final List<Spelsak> _allaHinder = new ArrayList<>();
    private final List<RörligSpelsak> _allaMonster = new ArrayList<>();
    private final List<Spelsak> _allaDiamanter= new ArrayList<>();
    private final List<Spelsak> _allaStjärnor = new ArrayList<>();
    private final int _världNummer;
    private final int _banNummer;
    private final Spelsak _målgång;

    public Bana(int världNummer, int banNummer, Spelsak målgång) {
        _världNummer = världNummer;
        _banNummer = banNummer;
        _målgång = målgång;
    }

    public void läggTillHinder(Spelsak spelsak) {
        if(spelsak == null) {
            return;
        }
        _allaHinder.add(spelsak);
    }

    public void läggTillMonster(RörligSpelsak monster) {
        if(monster == null) {
            return;
        }
        _allaMonster.add(monster);
    }

    public void läggTillDiamant(Spelsak spelsak) {
        if(spelsak == null) {
            return;
        }
        _allaDiamanter.add(spelsak);
    }

    public void läggTillStjärna(Spelsak spelsak) {
        if(spelsak == null) {
            return;
        }
        _allaStjärnor.add(spelsak);
    }

    public int banNummer() {
        return _banNummer;
    }

    public List<Spelsak> allaHinder() {
        return Collections.unmodifiableList(_allaHinder);
    }

    public List<Spelsak> allaDiamanter() {
        return Collections.unmodifiableList(_allaDiamanter);
    }

    public List<Spelsak> allaStjärnor() {
        return Collections.unmodifiableList(_allaStjärnor);
    }

    public List<RörligSpelsak> allaMonster() {
        return Collections.unmodifiableList(_allaMonster);
    }

    public Spelsak målgång() {
        return _målgång;
    }

    @Override
    public String toString() {
        return "Bana{" +
                "_allaHinder=" + _allaHinder +
                ", _allaMonster=" + _allaMonster +
                ", _allaDiamanter=" + _allaDiamanter +
                ", _allaStjärnor=" + _allaStjärnor +
                ", _världNummer=" + _världNummer +
                ", _banNummer=" + _banNummer +
                ", _målgång=" + _målgång +
                '}';
    }

    public void plockaDiamant(Spelsak diamant) {
        _allaDiamanter.remove(diamant);
    }

    public void plockaStjärna(Spelsak stjärna) {
        _allaStjärnor.remove(stjärna);
    }
}
