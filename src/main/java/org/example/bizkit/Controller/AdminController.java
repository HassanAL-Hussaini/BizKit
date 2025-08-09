package org.example.bizkit.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiResponse;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Service.AdminService;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/get-all/{superAdminId}")
    public ResponseEntity<?> getAllAdmins(@PathVariable Integer superAdminId) {
        return ResponseEntity.ok(adminService.getAllAdmins(superAdminId));
    }
    @PostMapping("/add/{superAdminId}")
    public ResponseEntity<?> addAdmin(@PathVariable Integer superAdminId,@Valid @RequestBody Admin admin , Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        adminService.addAdmin(superAdminId, admin);
        return ResponseEntity.ok(new ApiResponse("added successfully"));
    }
    @DeleteMapping("/delete/{superAdminId}/{adminId}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Integer superAdminId,@PathVariable Integer adminId) {
        adminService.deleteAdmin(superAdminId,adminId);
        return ResponseEntity.ok(new ApiResponse("delete successfully"));
    }
    @PutMapping("/update/{adminId}")
    public ResponseEntity<?> updateAdmin(@PathVariable Integer adminId, @Valid @RequestBody Admin admin ,   Errors errors ) {
        if(errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors.getFieldError().getDefaultMessage());
        }
        adminService.updateAdmin(adminId,admin);
        return ResponseEntity.ok(new ApiResponse("updated successfully"));
    }

}
