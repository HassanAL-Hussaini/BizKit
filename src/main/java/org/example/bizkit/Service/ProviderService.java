package org.example.bizkit.Service;

import lombok.RequiredArgsConstructor;
import org.example.bizkit.Api.ApiException;
import org.example.bizkit.Model.Admin;
import org.example.bizkit.Model.Provider;
import org.example.bizkit.Repository.AdminRepository;
import org.example.bizkit.Repository.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final AdminRepository adminRepository;
    private final AdminService adminService;

    //=====================GET Services =====================
    public List<?> getAllProviders(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);//this will never back if the admin id is not exist because it's contain throw statement on it.
        return providerRepository.findAll();
    }

    public List<?> getUnactiveProviders(Integer adminId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        return providerRepository.findProvidersByIsActiveIsFalse();
    }

    public List<?> getActiveProviders() {
        //any user can see the Providers
        return providerRepository.findProvidersByIsActiveIsTrue();
    }

    //=====================POST Services =====================
    public void addProvider(Provider provider) {
        //the idea here is : the provider to show his products in website the admin should give him access so,
        //if the provider register in our website he will go to the waitlist then the admin will give him the access until this moment he will not be able to show his product.
        //Done: make Admin Accept this provider by changing the state of isActive to true.
        providerRepository.save(provider);//now the provider is in my website but his product will never show up until the admin change his active state.
        // cuz it's by default false.
    }



    //======================Update Services=====================
    public void updateProvider(Integer providerIdUpdated, Provider newProvider) {
        //any provider can change his data either it's active or not.
        Provider oldProvider = getProviderByIdAndCheckIfExist(providerIdUpdated);
        oldProvider.setName(newProvider.getName());
        oldProvider.setEmail(newProvider.getEmail());
        oldProvider.setPassword(newProvider.getPassword());
        oldProvider.setPhone(newProvider.getPhone());
        oldProvider.setCompanyName(newProvider.getCompanyName());
        oldProvider.setAddress(newProvider.getAddress());
        //oldProvider.setStatus(newProvider.getStatus()); only Admin edite this variable
        providerRepository.save(oldProvider);
    }

    public void UpdateProviderStateByAdmin(Integer adminId,Integer providerId) {
        adminService.getAdminByIdAndCheckIfExist(adminId);
        Provider provider = getProviderByIdAndCheckIfExist(providerId);
        if(provider.getIsActive() == false) {
            provider.setIsActive(true);
            providerRepository.save(provider);
        }
    }

    //=====================Delete Services=====================
    public void deleteProvider(Integer providerIdDeleted) {
        //you can't delete provider if it still has order in progress
        Provider providerDeleted = getProviderByIdAndCheckIfExist(providerIdDeleted);
        //TODO if it has not order in progress delete it.
        providerRepository.delete(providerDeleted);
    }



    //Extra Services that i need in my Services
    protected Provider getProviderByIdAndCheckIfExist(Integer id) {
        Provider provider = providerRepository.findProviderById(id);
        if (provider == null) {
            throw new ApiException("the Provider Not Found");
        }
        return provider;
    }
}
