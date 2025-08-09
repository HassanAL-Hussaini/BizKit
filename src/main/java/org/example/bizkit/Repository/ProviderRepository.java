package org.example.bizkit.Repository;

import org.example.bizkit.Model.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProviderRepository extends JpaRepository<Provider,Integer> {


    // JPA
    Provider findProviderById(Integer id);
    List<Provider> findProvidersByIsActiveIsFalse();
    List<Provider> findProvidersByIsActiveIsTrue();

    // Query JPQL
    @Query("select p from Provider p where p.isActive = false")
    List<Provider> retrieveProviderByIsActive();


}
