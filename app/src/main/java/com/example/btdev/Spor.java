package com.example.btdev;

public class Spor {
    private String isim;
    private String kullanici;
    private String tarih;
    private int saatlikYakilanKalori;
    private int yapilanDakika;
    private int yakilanKalori;

    public Spor() {

    }

    public Spor(String isim, String kullanici, String tarih, int saatlikYakilanKalori, int yapilanDakika) {
        this.isim = isim;
        this.kullanici = kullanici;
        this.tarih = tarih;
        this.saatlikYakilanKalori = saatlikYakilanKalori;
        this.yapilanDakika = yapilanDakika;
        setYakilanKalori();
    }

    private void setYakilanKalori() {
        yakilanKalori = (yapilanDakika * getSaatlikYakilanKalori()) / 60;
    }

    public String getKullanici() {
        return kullanici;
    }

    public String getTarih() {
        return tarih;
    }

    public String getIsim() {
        return isim;
    }

    public int getSaatlikYakilanKalori() {
        return saatlikYakilanKalori;
    }

    public int getYapilanDakika() {
        return yapilanDakika;
    }

    public int getYakilanKalori() {
        return yakilanKalori;
    }
}
