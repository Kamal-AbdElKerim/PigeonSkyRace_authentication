package com.example.SpringSecurityDemo.Service;

import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BreederService  {

    @Autowired
    BreederRepository breederRepository;




    public Breeder findByNomColombie(String nomColombie){
        return breederRepository.findByNomColombie(nomColombie);
    }

    public Optional<Breeder> findByID(Long id){
        return breederRepository.findById(id);
    }

    public List<Breeder> findAll(){
        return breederRepository.findAll();
    }


}
