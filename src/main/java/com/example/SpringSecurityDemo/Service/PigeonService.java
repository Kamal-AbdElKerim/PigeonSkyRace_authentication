package com.example.SpringSecurityDemo.Service;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Repository.PigeonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PigeonService {

    @Autowired
    private PigeonRepository pigeonRepository;


    @Autowired
    private BreederRepository breederRepository;

    // Add a new pigeon
    public Pigeon addPigeon(Long breederId, Pigeon pigeon) {
        // Find the breeder by ID
        Breeder breeder = breederRepository.findById(breederId)
                .orElseThrow(() -> new RuntimeException("Breeder not found"));

        if (pigeonRepository.existsById(pigeon.getRingNumber())) {
            throw new RuntimeException("Pigeon already used");
        }

        pigeon.setBreeder(breeder);

        return pigeonRepository.save(pigeon);
    }


    // Get all pigeons
    public List<Pigeon> getAllPigeons() {
        return pigeonRepository.findAll();
    }

    // Find pigeon by ring number
    public Optional<Pigeon> getPigeonByRingNumber(Long ringNumber) {
        return pigeonRepository.findById(ringNumber);
    }

    // Delete a pigeon by ring number
    public void deletePigeon(Long ringNumber) {
        pigeonRepository.deleteById(ringNumber);
    }

    public List<Pigeon> findByBreederId(Long breederId) {
        return pigeonRepository.findByBreederId(breederId);
    }

    public  Pigeon findById(Long id) {
        return pigeonRepository.findById(id).orElse(null);
    }

}

