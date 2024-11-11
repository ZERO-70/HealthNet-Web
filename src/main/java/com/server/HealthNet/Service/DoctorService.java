package com.server.HealthNet.Service;

import com.server.HealthNet.Model.Doctor;
import com.server.HealthNet.Repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public int saveDoctor(Doctor doctor) {
        return doctorRepository.saveDoctor(doctor);
    }

    public Doctor getDoctorById(Long id) {
        Optional<Doctor> doc = doctorRepository.findDoctorById(id);
        if (doc.isPresent()){
            return doc.get();
        }
        else{
            return null;
        }
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAllDoctors();
    }

    public int updateDoctor(Doctor doctor) {
        return doctorRepository.updateDoctor(doctor);
    }

    public int deleteDoctorById(Long id) {
        return doctorRepository.deleteDoctorById(id);
    }
}
