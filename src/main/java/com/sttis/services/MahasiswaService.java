package com.sttis.services;

import com.sttis.dto.BiodataUpdateDTO;
import com.sttis.dto.MahasiswaDTO;
import com.sttis.models.entities.BiodataMahasiswa;
import com.sttis.models.entities.Mahasiswa;
import com.sttis.models.repos.BiodataMahasiswaRepository;
import com.sttis.models.repos.MahasiswaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MahasiswaService {

    private final MahasiswaRepository mahasiswaRepository;
    private final BiodataMahasiswaRepository biodataMahasiswaRepository;

    // Constructor untuk dependency injection
    public MahasiswaService(MahasiswaRepository mahasiswaRepository, BiodataMahasiswaRepository biodataMahasiswaRepository) {
        this.mahasiswaRepository = mahasiswaRepository;
        this.biodataMahasiswaRepository = biodataMahasiswaRepository;
    }

    /**
     * Mengambil daftar semua mahasiswa dan mengubahnya menjadi DTO.
     * @return List dari MahasiswaDTO.
     */
    public List<MahasiswaDTO> getAllMahasiswa() {
        return mahasiswaRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil detail satu mahasiswa berdasarkan ID.
     * @param id ID mahasiswa yang dicari.
     * @return MahasiswaDTO yang sesuai.
     * @throws RuntimeException jika mahasiswa tidak ditemukan.
     */
    public MahasiswaDTO getMahasiswaById(Integer id) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mahasiswa dengan ID " + id + " tidak ditemukan."));
        return convertToDTO(mahasiswa);
    }

    /**
     * Mengubah atau membuat biodata untuk seorang mahasiswa.
     * @param mahasiswaId ID mahasiswa yang biodatanya akan diubah.
     * @param biodataDTO Data baru untuk biodata.
     * @return Entitas BiodataMahasiswa yang sudah disimpan.
     */
    @Transactional
    public BiodataMahasiswa updateBiodata(Integer mahasiswaId, BiodataUpdateDTO biodataDTO) {
        // 1. Cari mahasiswa berdasarkan ID
        Mahasiswa mahasiswa = mahasiswaRepository.findById(mahasiswaId)
                .orElseThrow(() -> new RuntimeException("Mahasiswa dengan ID " + mahasiswaId + " tidak ditemukan."));

        // 2. Cek apakah biodata sudah ada, jika tidak, buat baru
        BiodataMahasiswa biodata = mahasiswa.getBiodata();
        if (biodata == null) {
            biodata = new BiodataMahasiswa();
            biodata.setMahasiswa(mahasiswa);
        }

        // 3. Perbarui field dari DTO
        biodata.setAlamat(biodataDTO.getAlamat());
        biodata.setNomorTelepon(biodataDTO.getNomorTelepon());
        biodata.setEmailPribadi(biodataDTO.getEmailPribadi());
        biodata.setKontakDarurat(biodataDTO.getKontakDarurat());

        // 4. Simpan perubahan ke database
        return biodataMahasiswaRepository.save(biodata);
    }

    /**
     * Helper method privat untuk mengubah entitas Mahasiswa menjadi MahasiswaDTO.
     * @param mahasiswa Entitas Mahasiswa dari database.
     * @return Objek MahasiswaDTO.
     */
    private MahasiswaDTO convertToDTO(Mahasiswa mahasiswa) {
        MahasiswaDTO dto = new MahasiswaDTO();
        dto.setMahasiswaId(mahasiswa.getMahasiswaId());
        dto.setNim(mahasiswa.getNim());
        dto.setNamaLengkap(mahasiswa.getNamaLengkap());
        
        if (mahasiswa.getStatus() != null) {
            dto.setStatus(mahasiswa.getStatus().name());
        }
        
        if (mahasiswa.getJurusan() != null) {
            dto.setNamaJurusan(mahasiswa.getJurusan().getNamaJurusan());
        }

        if (mahasiswa.getPembimbingAkademik() != null) {
            dto.setNamaDosenPA(mahasiswa.getPembimbingAkademik().getNamaLengkap());
        }

        return dto;
    }
}