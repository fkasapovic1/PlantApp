package ba.unsa.etf.rma.projekat

fun biljkice():List<Biljka> {
    return listOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            jela = listOf("Jogurt sa voćem", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Ružmarin (Rosmarinus officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Grah", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Lavanda (Lavandula angustifolia)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Jogurt sa voćem"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Persun (Petroselinum crispum)",
            porodica = "Apiaceae (štitarka)",
            medicinskoUpozorenje = "Osobe alergične na peršun mogu doživjeti kožne iritacije.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA
            ),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Supa"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
            zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.PJESKOVITO)
        ),
        Biljka(
            naziv = "Djumbir (Zingiber officinale)",
            porodica = "Zingiberaceae (đumbirovke)",
            medicinskoUpozorenje = "Osobe koje koriste lijekove za razrjeđivanje krvi trebaju izbjegavati prekomjernu konzumaciju đumbira.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.KORIJENASTO,
            jela = listOf("Čaj od đumbira", "Kolač", "Marinirano Meso"),
            klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Origano (Origanum vulgare)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Ne preporučuje se tijekom trudnoće.",
            medicinskeKoristi = listOf(MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pizza", "Pečeno pile"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kurkuma (Curcuma longa)",
            porodica = "Zingiberaceae (đumbirovke)",
            medicinskoUpozorenje = "Osobe koje uzimaju lijekove za razrjeđivanje krvi trebaju konzumirati kurkumu s oprezom.",
            medicinskeKoristi = listOf(
                MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPROBAVE
            ),
            profilOkusa = ProfilOkusaBiljke.GORKO,
            jela = listOf("Smoothie", "Omlet", "Supa", "Rižoto"),
            klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Majcina dušica (Thymus vulgaris)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Osobe alergične na biljke iz porodice Lamiaceae trebaju izbjegavati konzumaciju čaja od majčine dušice.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Napitak"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        )
    )
}


