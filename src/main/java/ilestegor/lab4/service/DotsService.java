package ilestegor.lab4.service;

import ilestegor.lab4.dto.DotsRequestDto;
import ilestegor.lab4.dto.DotsResponseDto;
import ilestegor.lab4.entity.DotsEntity;
import ilestegor.lab4.repository.DotsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

public interface DotsService {
    DotsEntity addDot(DotsRequestDto dotsRequestDto, Authentication authentication);
}
