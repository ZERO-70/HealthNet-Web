package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Avalibility;
import com.server.HealthNet.Service.AvalibilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/avalibilities")
public class AvalibilityController {

    @Autowired
    private AvalibilityService service;

    @PostMapping
    public ResponseEntity<String> addAvalibility(@RequestBody Avalibility avalibility) {
        int result = service.addAvalibility(avalibility);
        return result > 0 ? new ResponseEntity<>("Avalibility Inserted successfully", HttpStatus.OK):new ResponseEntity<>("Avalibility Insertion failed", HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public List<Avalibility> getAllAvalibilities() {
        return service.getAllAvalibilities();
    }

    @GetMapping("/{id}")
    public Optional<Avalibility> getAvalibilityById(@PathVariable Long id) {
        return service.getAvalibilityById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAvalibilityById(@PathVariable Long id) {
        int result = service.deleteAvalibilityById(id);
        return result > 0 ? new ResponseEntity<>("Avalibility Deleted successfully", HttpStatus.OK):new ResponseEntity<>("Avalibility Deletion failed", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAvalibility(@PathVariable Long id, @RequestBody Avalibility avalibility) {
        int result = service.updateAvalibility(id, avalibility);
        return result > 0 ?new ResponseEntity<>("Avalibility Updated successfully", HttpStatus.OK):new ResponseEntity<>("Avalibility Update failed", HttpStatus.NOT_FOUND);
    }
}
