package com.example.btdev;

public class HaftalikBilgi {
    private String tarih;
    private String gun;
    private int netKalori;
    private int alinanKalori;
    private int verilenKalori;
    private int protein, yag, karbonhidrat;

    public HaftalikBilgi(String tarih, String gun, int netKalori, int alinanKalori, int verilenKalori, int protein, int yag, int karbonhidrat) {
        this.tarih = tarih;
        this.gun = gun;
        this.netKalori = netKalori;
        this.alinanKalori = alinanKalori;
        this.verilenKalori = verilenKalori;
        this.protein = protein;
        this.yag = yag;
        this.karbonhidrat = karbonhidrat;
    }

    public String getTarih() {
        return tarih;
    }

    public String getGun() {
        return gun;
    }

    public int getNetKalori() {
        return netKalori;
    }

    public int getAlinanKalori() {
        return alinanKalori;
    }

    public int getVerilenKalori() {
        return verilenKalori;
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
}
