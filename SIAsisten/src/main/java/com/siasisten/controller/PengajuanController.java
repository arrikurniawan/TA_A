package com.siasisten.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.siasisten.dao.MatkulDAO;
import com.siasisten.model.LowonganModel;
import com.siasisten.model.LowonganModelDTO;
import com.siasisten.model.MahasiswaModel;
import com.siasisten.model.MatkulModel;
import com.siasisten.model.PengajuanModel;
import com.siasisten.service.LowonganService;
import com.siasisten.service.MahasiswaService;
import com.siasisten.service.PengajuanService;
import com.siasisten.service.RuanganMatkulService;

@Controller
public class PengajuanController {
	
	@Autowired
	MahasiswaService mahasiswaService;
	
	@Autowired
	PengajuanService pengajuanService;
	
	@Autowired
	LowonganService lowonganService;
	
	@Autowired
	MatkulDAO matkulDAO;
	
	@Autowired
	LowonganService lowonganDAO;
	
	@Autowired
	MatkulDAO matkulDao;
	
	@Autowired
	RuanganMatkulService ruanganMatkulDAO;
	
	@RequestMapping("/pengajuan/view/{id}")
    public String lihatPengajuan(Model model, @PathVariable(value = "id") int id)
    {
		PengajuanModel pengajuan = pengajuanService.selectPengajuanById(id);
		if(pengajuan != null) {
			LowonganModel lowongan = lowonganService.selectLowonganbyID(pengajuan.getIdLowongan());
			MatkulModel matkul = matkulDAO.selectMatkulbyId(lowongan.getIdMatkul());	
			MahasiswaModel mahasiswa = mahasiswaService.selectMahasiswabyNPM(pengajuan.getUsernameMhs());
			
		    model.addAttribute("pengajuan", pengajuan);
		   	model.addAttribute("matkul", matkul);
		   	model.addAttribute("mahasiswa", mahasiswa);
		   	System.out.println(pengajuan.getIsAccepted());
		   	model.addAttribute("pageTitle", "Lihat Pengajuan");
		    return "view-pengajuan";
		}
		else {
			model.addAttribute("idlowongan", id);
			return "not-found";
		}
    }
	
	@PostMapping("/pengajuan/hapus/{id_lowongan}")
	public String hapusPengajuan(@PathVariable(value = "id_lowongan") int id_lowongan) 
	{
		pengajuanService.deletePengajuan(id_lowongan);
		return "success-delete-pengajuan";
	}
	@RequestMapping("/pengajuan/review/{id_pengajuan}")
	public String pengajuanUbah (Model model, @PathVariable(value = "id_pengajuan", required = false) int id_pengajuan) 
	{
		PengajuanModel pengajuan = pengajuanService.selectPengajuanById(id_pengajuan);
		LowonganModel lowongan = lowonganService.selectLowonganbyID(pengajuan.getIdLowongan());
		MatkulModel matkul = matkulDAO.selectMatkulbyId(lowongan.getIdMatkul());	
		MahasiswaModel mahasiswa = mahasiswaService.selectMahasiswabyNPM(pengajuan.getUsernameMhs());
		model.addAttribute("pengajuan", pengajuan);
	   	model.addAttribute("matkul", matkul);
	   	model.addAttribute("mahasiswa", mahasiswa);
	   	System.out.println(pengajuan.getIsAccepted());
	   	model.addAttribute("pageTitle", "Review Pengajuan");
		return "ubah-pengajuan";
	}
	
	@PostMapping("/pengajuan/review/submit")
	public String ubahSubmit (Model model, @RequestParam(value = "id", required = false) int id,
										   @RequestParam(value = "idLowongan", required = false) int idLowongan,
										   @RequestParam(value = "usernameMhs", required = false) String usernameMhs,
										   @RequestParam(value = "isAccepted", required = false) int isAccepted) 
	{
		
		PengajuanModel pengajuan = new PengajuanModel(id, idLowongan, usernameMhs, isAccepted);
		pengajuanService.updatePengajuan(pengajuan);
		model.addAttribute("message", "Lowongan dengan id " + idLowongan + " , NPM" + usernameMhs + " berhasil diubah");
		return "success-update";
	}
	
	@RequestMapping("/pengajuan/tambah")
	public String tambahPengajuan(Model model, @ModelAttribute("pengajuan") PengajuanModel pengajuan, Authentication auth)
	{
		List<LowonganModelDTO> allLowonganDTO = new ArrayList<>();
		List<LowonganModel> allLowongan = lowonganDAO.selectAllLowongan();
		model.addAttribute("allLowongan", allLowongan);
		for(LowonganModel lMod : allLowongan) {
			LowonganModelDTO lDto = new LowonganModelDTO();
			lDto.setId(lMod.getId());
			MatkulModel mMod = matkulDao.selectMatkulbyId(lMod.getIdMatkul());
			mMod.getId();
			lDto.setNamaMatkul(mMod.getNamaMatkul());
			lDto.setJmlLowongan(lMod.getJmlLowongan());
			if(lMod.getIsOpen()==1) {
				lDto.setIsOpen(lMod.getIsOpen());
				allLowonganDTO.add(lDto);
			}	
		}
		model.addAttribute("allLowonganDTO", allLowonganDTO);
		model.addAttribute("pageTitle", "Ajukan Pengajuan");
		return "form-addPengajuan";
	}
	
}
