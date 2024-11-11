package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.MedicalRecord;
import com.server.HealthNet.Service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medical_records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @GetMapping("/{id}")
    public MedicalRecord getMedicalRecordById(@PathVariable Long id) {
        return medicalRecordService.getMedicalRecordById(id);
    }

    @PostMapping
    public ResponseEntity<String> createMedicalRecord(@RequestBody MedicalRecord record) {
        return medicalRecordService.createMedicalRecord(record)>0 ? new ResponseEntity<>("Medical Record Insertion successfully", HttpStatus.OK):new ResponseEntity<>("Medical Record Insertion failed", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecord record) {
        record.setRecord_id(id);
        return medicalRecordService.updateMedicalRecord(record)>0 ? new ResponseEntity<>("Medical Record Updated successfully", HttpStatus.OK): new ResponseEntity<>("Medical Record Update failed", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMedicalRecord(@PathVariable Long id) {
        return medicalRecordService.deleteMedicalRecord(id)>0 ? new ResponseEntity<>("Medical Record Deleted successfully", HttpStatus.OK):new ResponseEntity<>("Medical Record Deletion failed", HttpStatus.NOT_FOUND);
    }
}
