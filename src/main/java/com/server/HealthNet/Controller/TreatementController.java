package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Treatement;
import com.server.HealthNet.Service.TreatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/treatements")
public class TreatementController {

    @Autowired
    private TreatementService treatmentService;

    @GetMapping
    public List<Treatement> getAllTreatments() {
        return treatmentService.getAllTreatments();
    }

    @GetMapping("/{id}")
    public Treatement getTreatmentById(@PathVariable Long id) {
        return treatmentService.getTreatmentById(id);
    }

    @PostMapping
    public ResponseEntity<String> createTreatment(@RequestBody Treatement treatment) {
        return treatmentService.createTreatment(treatment)>0?new ResponseEntity<>("Treatement Inserted successfully", HttpStatus.OK):new ResponseEntity<>("Treatement Insertion failed", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateTreatment(@PathVariable Long id, @RequestBody Treatement treatment) {
        treatment.setTreatement_id(id);
        return treatmentService.updateTreatment(treatment)>0?new ResponseEntity<>("Treatement Updated successfully", HttpStatus.OK):new ResponseEntity<>("Treatement Update failed", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTreatment(@PathVariable Long id) {
        return treatmentService.deleteTreatment(id)>0?new ResponseEntity<>("Treatement deleted successfully", HttpStatus.OK):new ResponseEntity<>("Treatement Deletion failed", HttpStatus.NOT_FOUND);
    }
}
