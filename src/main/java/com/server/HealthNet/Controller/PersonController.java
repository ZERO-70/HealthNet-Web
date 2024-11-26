package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Person;
import com.server.HealthNet.Service.PersonService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Person person = personService.getPersonById(id);
        return person != null ? ResponseEntity.ok(person) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createPerson(@RequestBody Person person) {
        System.out.println(person);
        return personService.createPerson(person) > 0
                ? new ResponseEntity<>("Person Inserted successfully", HttpStatus.OK)
                : new ResponseEntity<>("Person Insertion failed", HttpStatus.NOT_FOUND);
    }

    // dont want anyone to acess this
    //@PutMapping("/{id}")
    public ResponseEntity<String> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        // Assuming the service method handles the logic to check if the person exists
        person.setId(id); // Set the ID of the person to be updated
        return personService.updatePerson(person) > 0
                ? new ResponseEntity<>("Person updated successfully", HttpStatus.OK)
                : new ResponseEntity<>("Person Update failed", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        return personService.deletePerson(id) > 0
                ? new ResponseEntity<>("Person deleted successfully", HttpStatus.OK)
                : new ResponseEntity<>("Person Deletion failed", HttpStatus.NOT_FOUND);
    }
}
