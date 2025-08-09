package org.example.bizkit.Repository;

import org.example.bizkit.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findAdminById(Integer id);
}
