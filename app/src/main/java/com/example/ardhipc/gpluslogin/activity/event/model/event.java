package com.example.ardhipc.gpluslogin.activity.event.model;

/**
 * Created by Ardhipc on 5/28/2015.
 */
public class Event {
    private String nama,waktu, foto = "http://eventhere.belibun.com/", cp,deskripsi, kategori,id,verivikasi;

//    private String title, thumbnailUrl;
//    private int year;
//    private double rating;
//    private ArrayList<String> genre;

    public Event() {
    }

    public Event(String nama, String waktu,String cp, String deskripsi, String kategori, String id, String verivikasi) {
        this.nama = nama;
        this.waktu= waktu ;
        this.cp= cp ;
        this.deskripsi= deskripsi ;
        this.kategori= kategori ;
        this.id= id ;
        this.verivikasi= verivikasi ;
    }

//    public Event(String name, String thumbnailUrl, int year, double rating,
//                 ArrayList<String> genre) {
//        this.title = name;
//        this.thumbnailUrl = thumbnailUrl;
//        this.year = year;
//        this.rating = rating;
//        this.genre = genre;
//    }

//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String name) {
//        this.title = name;
//    }
//
//    public String getThumbnailUrl() {
//        return thumbnailUrl;
//    }
//
//    public void setThumbnailUrl(String thumbnailUrl) {
//        this.thumbnailUrl = thumbnailUrl;
//    }
//
//    public int getYear() {
//        return year;
//    }
//
//    public void setYear(int year) {
//        this.year = year;
//    }
//
//    public double getRating() {
//        return rating;
//    }
//
//    public void setRating(double rating) {
//        this.rating = rating;
//    }
//
//    public ArrayList<String> getGenre() {
//        return genre;
//    }
//
//    public void setGenre(ArrayList<String> genre) {
//        this.genre = genre;
//    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getWaktu() {
        return waktu;
    }

    public void setWaktu(String waktu) {
        this.waktu = waktu;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto += foto;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVerivikasi() {
        return verivikasi;
    }

    public void setVerivikasi(String verivikasi) {
        this.verivikasi = verivikasi;
    }
}
