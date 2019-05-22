package com.team.hcdh.albumphoto.model;

import java.util.ArrayList;

public class Note {
    public String Id, TieuDe, NoiDung, ThoiGian;
    public ArrayList<byte[]> arrImage = new ArrayList<>();

    public Note(){}

    public Note(String id, String tieuDe, String noiDung, String thoiGian) {
        this.Id = id;
        TieuDe = tieuDe;
        NoiDung = noiDung;
        ThoiGian = thoiGian;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getTieuDe() {
        return TieuDe;
    }

    public void setTieuDe(String tieuDe) {
        TieuDe = tieuDe;
    }

    public String getNoiDung() {
        return NoiDung;
    }

    public void setNoiDung(String noiDung) {
        NoiDung = noiDung;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }

}
