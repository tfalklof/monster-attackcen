package se.tfalklof.android.monsterattacken;

import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

class Banor {
    private final Resources _res;

    private final Map<Integer, Värld> världar;
    private double scaleFactor;

    public Banor(Resources res) {
        _res = res;
        scaleFactor = 1.0;
        världar = new HashMap<>();
        skapaVärldar();
    }

    public void skapaVärldar() {
        skapaVärld(1);
        skapaVärld(2);
    }

    public void skapaVärld(int världNummer) {
        världar.put(världNummer, new Värld(världNummer));
        if (världNummer == 1) {
            skapaBana(1, 1);
            skapaBana(1, 2);
            skapaBana(1, 3);
            skapaBana(1, 4);
            skapaBana(1, 5);
            skapaBana(1, 6);
            skapaBana(1, 7);
        } else if (världNummer == 2) {
            skapaBana(2, 1);
        }
    }

    // Spelsaker är: Spelare, Monster, Hinder, Diamanter och Stjärnor
    public void skapaBana(int världNummer, int banNummer) {

        if (världNummer == 1 && banNummer == 1) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 2800, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 800, 1000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 600, 1100, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 600, 1400, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 700, 1500, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 800, 1900, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 2000, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 800, 2300, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 800, 2400, scaleFactor, 3.0, 0.0));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 2) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3000, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 200, 1000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 800, 1200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 800, 1400, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 400, 1700, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 1500, 1800, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 800, 2100, scaleFactor, 3.0, 0.0));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 500, 2600, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 3) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3500, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 920, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 250, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1400, 1200, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 350, 1250, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 1000, 1650, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 500, 1650, 2, 0.0, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 1400, 1650, 2, 0.0, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 200, 2000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 850, 2300, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 500, 2600, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 400, 2900, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1300, 3000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 350, 3150, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 1300, 3150, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 4) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3600, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1400, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 900, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 400, 1000, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 300, 1100, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 500, 1500, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 980, 1500, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 1460, 1500, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 500, 1850, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1400, 2200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 900, 2200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 400, 2200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 500, 2500, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 980, 2500, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 1460, 2500, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 500, 2800, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 800, 3250, 5.0, 0.0, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 5) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3600, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 350, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 1250, 1000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 350, 1250, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 1250, 1250, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 400, 1600, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 1700, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 400, 1750, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1200, 1850, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 800, 2200, 3.0, 0.0, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 300, 2600, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 800, 2800, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 800, 2900, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 800, 3000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 800, 3100, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 800, 3200, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 800, 3300, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 800, 3400, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 6) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3400, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 200, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 300, 1300, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1400, 1400, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 2000, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 300, 2000, 3.0, 0.0, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1000, 2200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 2400, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 900, 2700, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 300, 2600, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1000, 3100, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 1 && banNummer == 7) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 3200, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 300, 1000, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1200, 1100, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 400, 1400, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 200, 1600, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 500, 2100, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 980, 2100, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.vattenpoolen), 1460, 2100, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 400, 2350, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.tegelmur), 1400, 2500, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1100, 2700, scaleFactor));
            bana.läggTillMonster(new RörligSpelsak(_res.getDrawable(R.drawable.laki_monstret), 300, 2800, 4.0, 0.0, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }

        if (världNummer == 2 && banNummer == 1) {
            Bana bana = new Bana(världNummer, banNummer, new Spelsak(_res.getDrawable(R.drawable.malgang), 800, 2900, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.jorden), 800, 1000, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 200, 1100, scaleFactor));
            bana.läggTillHinder(new Spelsak(_res.getDrawable(R.drawable.jorden), 200, 1300, scaleFactor));
            bana.läggTillDiamant(new Spelsak(_res.getDrawable(R.drawable.diamant), 200, 2300, scaleFactor));
            bana.läggTillStjärna(new Spelsak(_res.getDrawable(R.drawable.stjarna), 1300, 2700, scaleFactor));
            hämtaVärld(världNummer).läggTillBana(bana);
        }
    }

    public Värld hämtaVärld(int i) {
        return världar.get(i);
    }

    public int antalVärldar() {
        return världar.size();
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
