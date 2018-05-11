package com.siasisten.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PengajuanModelDTO {
	private int id;
	private String namaMatkul;
	private String kodeMatkul;
	private String namaDosen;
	private int statusLowongan;
	private int statusPengajuan;
	private int jumlahLowongan;
	private int jumlahPengajuan;
	private int jumlahDiterima;
}