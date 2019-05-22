package com.team.hcdh.albumphoto.model;

public class Image {
    public int Id;
    public String IdRef;
    public byte[] HinhAnh;

    public Image() {
    }

    public Image(int id, String idRef, byte[] hinhAnh) {
        Id = id;
        IdRef = idRef;
        HinhAnh = hinhAnh;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getIdRef() {
        return IdRef;
    }

    public void setIdRef(String idRef) {
        IdRef = idRef;
    }

    public byte[] getHinhAnh() {
        return HinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        HinhAnh = hinhAnh;
    }
}
