package com.example.SpringSecurityDemo.Controller;


import com.example.SpringSecurityDemo.Entity.User.Breeder;
import com.example.SpringSecurityDemo.Entity.model.BreederDto;
import com.example.SpringSecurityDemo.Entity.model.Pigeon;
import com.example.SpringSecurityDemo.Entity.model.PigeonResponseDto;
import com.example.SpringSecurityDemo.Repository.BreederRepository;
import com.example.SpringSecurityDemo.Service.PigeonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/Api/pigeons")
public class PigeonController {

    @Autowired
    private PigeonService pigeonService;


    private final BreederRepository userRepository;

    @Autowired
    private PigeonResponseDto pigeonDto ;

    // Add a new pigeon
    @PostMapping()
    public ResponseEntity<Pigeon> addPigeon(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody Pigeon pigeon) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Optional<Breeder> otherUser = Optional.ofNullable(userRepository.findByNomColombie(authentication.getName()));

        if (otherUser.isPresent()) {

        Pigeon savedPigeon = pigeonService.addPigeon(otherUser.get().getId(), pigeon);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPigeon);
        }

       return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }



    @GetMapping("/{pigeonId}")
    public ResponseEntity<PigeonResponseDto> getPigeonWithBreeder(@PathVariable Long pigeonId) {
        Optional<Pigeon> pigeonOpt = pigeonService.getPigeonByRingNumber(pigeonId);

        if (pigeonOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Pigeon pigeon = pigeonOpt.get();

        BeanUtils.copyProperties(pigeon, pigeonDto);

        Breeder breeder = pigeon.getBreeder();
        if (breeder != null) {
            BreederDto breederDto = new BreederDto();
            BeanUtils.copyProperties(breeder, breederDto);
            pigeonDto.setBreeder(breederDto);
        }

        return ResponseEntity.ok(pigeonDto);
    }



    // Get all pigeons
    @GetMapping
    public List<PigeonResponseDto> getAllPigeons() {
        List<Pigeon> pigeons = pigeonService.getAllPigeons();
        List<PigeonResponseDto> pigeonDtos = new ArrayList<>();

        System.out.println(pigeonDtos);
        for (Pigeon pigeon : pigeons) {


            BeanUtils.copyProperties(pigeon, pigeonDto);
            System.out.println("pigeonDto" + pigeon);
            Breeder breeder = pigeon.getBreeder();
            if (breeder != null) {
                BreederDto breederDto = new BreederDto();
                BeanUtils.copyProperties(breeder, breederDto);
                pigeonDto.setBreeder(breederDto);
            }

            pigeonDtos.add(pigeonDto);
        }

        return pigeonDtos;
    }

    // Delete a pigeon by ring number
    @DeleteMapping("/{ringNumber}")
    public void deletePigeon(@PathVariable Long ringNumber) {
        pigeonService.deletePigeon(ringNumber);
    }
}
