package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.DTO.AdminInfoDto;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Repository.AdminRepository;

import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<?>  getAllAdmins() {
        //no one cant see the admins except the super admin.
//        Admin superAdmin = getAdminByIdAndCheckIfExist(superAdminId);
//        if(!(superAdmin.getRole().equals("super admin"))) {
//            throw new ApiException("super admin id is incorrect");
//        } >> Canceled recording to the instructor advice
        List<Admin> admins = adminRepository.findAll();
        ArrayList<AdminInfoDto> adminsDtoList = new ArrayList<>();
        for (Admin admin : admins) {
            adminsDtoList.add(new AdminInfoDto(admin.getName(),admin.getEmail(),admin.getPhone()));
        }
        return adminsDtoList;
    }

    public void addAdmin( Admin admin) {
//        Admin superAdmin = getAdminByIdAndCheckIfExist(superAdminId);
        //there is 2 Admin super and regular admin
        //the idea here is : the super admin can add regular admin but not vice versa
        //the super admin have been added to the database manually , in the system creation.
//        if(!(superAdmin.getRole().equals("super admin"))) {
//            throw new ApiException("Super Admin Not Found");
//        } >> Canceled recording to the instructor advice
        adminRepository.save(admin);
    }

    public void updateAdmin(Integer adminIdUpdated,Admin newAdmin ) {
        Admin oldAdmin = getAdminByIdAndCheckIfExist(adminIdUpdated); //if the admin is not found this line will never been completed because the function {getAdminByIdAndCheckIfExist} will throw Exception
        oldAdmin.setName(newAdmin.getName());
        oldAdmin.setEmail(newAdmin.getEmail());
        oldAdmin.setPassword(newAdmin.getPassword());
        oldAdmin.setPhone(newAdmin.getPhone());
        adminRepository.save(oldAdmin);
    }

    public void deleteAdmin( Integer adminIdDeleted) {
        Admin adminDeleted = adminRepository.findAdminById(adminIdDeleted);
        if(adminDeleted==null){
            throw new ApiException("the Admin Not Found");
        }
        adminRepository.deleteById(adminIdDeleted);
//        Admin superAdmin = adminRepository.findAdminById(superAdminId);
//        if(superAdmin==null){
//            throw new ApiException("the Admin Not Found");
//        }
//        if(superAdmin.getRole().equals("super admin")) {
//            adminRepository.delete(adminDeleted);
//        } >> Canceled recording to the instructor advice
    }

        protected Admin getAdminByIdAndCheckIfExist(Integer id) {
        Admin admin = adminRepository.findAdminById(id);
        if(admin==null){
            throw new ApiException("the Admin Not Found");
        }
        return admin;
    }
}
