package ilestegor.lab4.controller;

import ilestegor.lab4.dto.DotsRequestDto;
import ilestegor.lab4.dto.DotsResponseDto;
import ilestegor.lab4.service.DotsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dots")
public class DotController {
    private final DotsService dotsService;

    public DotController(DotsService dotsService) {
        this.dotsService = dotsService;
    }

    @PostMapping("add")
    public ResponseEntity<DotsResponseDto> addDots(@RequestBody DotsRequestDto dotsRequestDto, Authentication user) {
        DotsResponseDto responseDto = dotsService.mapDotsEntityToResponseDotsDto(dotsService.addDot(dotsRequestDto, user));
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("getDots")
    public ResponseEntity<List<DotsResponseDto>> getAllDotsByUser(Authentication authentication) {
        return new ResponseEntity<>(dotsService.getAllUserDots(authentication), HttpStatus.OK);
    }

    @DeleteMapping("deleteDots")
    public ResponseEntity<String> deleteDots(Authentication authentication) {
        dotsService.deleteDotsByUserId(authentication);
        return new ResponseEntity<>("Dots were removed successfully", HttpStatus.OK);
    }
}
