package com.example.btdev;

public class Yemek {
    private String isim;
    private String ogunTuru;
    private String kullanici;
    private String tarih;
    private String resimUrl;
    private int kalori;
    private int protein;
    private int yag;
    private int karbonhidrat;

    public Yemek() { }

    public Yemek(String tarih, String kullanici, String isim, String ogunTuru, int kalori, int protein, int yag, int karbonhidrat,String resimUrl) {
        this.tarih = tarih;
        this.kullanici = kullanici;
        this.isim = isim;
        this.ogunTuru = ogunTuru;
        this.kalori = kalori;
        this.protein = protein;
        this.yag = yag;
        this.karbonhidrat = karbonhidrat;
        this.resimUrl=resimUrl;
    }

    public String getTarih() {
        return tarih;
    }

    public String getKullanici() {
        return kullanici;
    }

    public String getIsim() {
        return isim;
    }

    public String getOgunTuru() {
        return ogunTuru;
    }

    public int getKalori() {
        return kalori;
    }

    public int getProtein() {
        return protein;
    }

    public int getYag() {
        return yag;
    }

    public int getKarbonhidrat() {
        return karbonhidrat;
    }

    public String getResimUrl() {
        return resimUrl;
    }
}
