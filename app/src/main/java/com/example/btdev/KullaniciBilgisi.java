package com.example.btdev;

public class KullaniciBilgisi {
    private String email;
    private String parola;
    private String isim;
    private String soyisim;
    private String dogumTarihi;
    private int boy;
    private int kilo;

    public KullaniciBilgisi() {
    }

    public KullaniciBilgisi(String email, String parola, String isim, String soyisim, String dogumTarihi, int boy, int kilo) {
        this.email = email;
        this.parola = parola;
        this.isim = isim;
        this.soyisim = soyisim;
        this.dogumTarihi = dogumTarihi;
        this.boy = boy;
        this.kilo = kilo;
    }

    public String getEmail() {
        return email;
    }

    public String getParola() {
        return parola;
    }

    public String getIsim() {
        return isim;
    }

    public String getSoyisim() {
        return soyisim;
    }

    public String getDogumTarihi() {
        return dogumTarihi;
    }

    public int getBoy() {
        return boy;
    }

    public int getKilo() {
        return kilo;
    }
}
