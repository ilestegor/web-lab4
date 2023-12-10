package ilestegor.lab4.service;

import ilestegor.lab4.dto.DotsRequestDto;
import ilestegor.lab4.dto.DotsResponseDto;
import ilestegor.lab4.entity.DotsEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface DotsService {
    DotsEntity addDot(DotsRequestDto dotsRequestDto, Authentication authentication);

    List<DotsResponseDto> getAllUserDots(Authentication authentication);

    DotsResponseDto mapDotsEntityToResponseDotsDto(DotsEntity dotsEntity);

    int deleteDotsByUserId(Authentication authentication);
}
