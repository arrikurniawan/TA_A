package com.siasisten.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PengajuanModel {
	private int id;
	private int idlowongan;
	private String usernameMhs;
	private int isAccepted;
}
