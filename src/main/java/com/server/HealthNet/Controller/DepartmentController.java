package com.server.HealthNet.Controller;

import com.server.HealthNet.Model.Department;
import com.server.HealthNet.Service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public ResponseEntity<String> createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department)>0? new ResponseEntity<>("Department Inserted successfully", HttpStatus.OK):new ResponseEntity<>("Department Insertion failed", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        department.setDepartment_id(id);
        return departmentService.updateDepartment(department)>0?new ResponseEntity<>("Department Updated successfully", HttpStatus.OK):new ResponseEntity<>("Department Update failed", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id)>0?new ResponseEntity<>("Department Deleted successfully", HttpStatus.OK):new ResponseEntity<>("Department Deletion failed", HttpStatus.NOT_FOUND);
    }
}
