package ilestegor.lab4.service.serviceImpl;

import ilestegor.lab4.dto.DotsRequestDto;
import ilestegor.lab4.dto.DotsResponseDto;
import ilestegor.lab4.entity.DotsEntity;
import ilestegor.lab4.entity.HitType;
import ilestegor.lab4.exceptions.CoordinateValuesException;
import ilestegor.lab4.exceptions.DotsDeleteException;
import ilestegor.lab4.repository.DotsRepository;
import ilestegor.lab4.service.DotsService;
import ilestegor.lab4.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DotsServiceImpl implements DotsService {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    private static final short COUNT_OF_NUMBERS_AFTER_DECIMAL_POINT = 3;
    private static final double DIVIDER = Math.pow(10, COUNT_OF_NUMBERS_AFTER_DECIMAL_POINT);
    private final DotsRepository dotsRepository;
    private final UserService userService;

    public DotsServiceImpl(DotsRepository dotsRepository, UserService userService) {
        this.dotsRepository = dotsRepository;
        this.userService = userService;
    }


    @Override
    public List<DotsResponseDto> getAllUserDots(Authentication authentication) {
        List<DotsEntity> dotsEntityList = dotsRepository.getDotsEntitiesByUserId(userService.findUserByUserName(authentication.getName()).get().getId());
        return dotsEntityList.stream().map(this::mapDotsEntityToResponseDotsDto).toList();
    }

    @Override
    public DotsEntity addDot(DotsRequestDto dotsRequestDto, Authentication authentication) {
        if (validateDots(dotsRequestDto)) {
            long startExec = System.nanoTime();
            DotsEntity dotsEntity = new DotsEntity();
            dotsEntity.setCurRequestTime(dateFormat.format(new Date(System.currentTimeMillis())));
            dotsEntity.setHitType(checkArea(dotsRequestDto));
            dotsEntity.setX(dotsRequestDto.x());
            dotsEntity.setY(dotsRequestDto.y());
            dotsEntity.setR(dotsRequestDto.r());
            dotsEntity.setExecutionTime(System.nanoTime() - startExec);
            dotsEntity.setUser(userService.findUserByUserName(authentication.getName()).get());
            return dotsRepository.save(dotsEntity);
        } else {
            throw new CoordinateValuesException("Coordinates values are out of range");
        }
    }

    @Override
    @Transactional
    public int deleteDotsByUserId(Authentication authentication) {
        int dotsDeletedCount = dotsRepository.deleteAllByUserId(userService.findUserByUserName(authentication.getName()).get().getId());
        if (dotsDeletedCount >= 0) {
            return dotsDeletedCount;
        } else {
            throw new DotsDeleteException("Dot were not deleted");
        }
    }

    private String checkArea(DotsRequestDto dotsRequestDto) {
        if (dotsRequestDto.y() <= dotsRequestDto.r() && (-dotsRequestDto.x() >= 0) && (dotsRequestDto.r() >= -dotsRequestDto.x()) && (dotsRequestDto.y() >= 0))
            return HitType.SECOND_AREA.getHitArea();
        if (dotsRequestDto.y() >= -dotsRequestDto.x() - (dotsRequestDto.r() / 2) && (dotsRequestDto.x() <= 0) && (dotsRequestDto.y() <= 0))
            return HitType.THIRD_AREA.getHitArea();
        if (dotsRequestDto.x() * dotsRequestDto.x() + dotsRequestDto.y() * dotsRequestDto.y() <= dotsRequestDto.r() * dotsRequestDto.r() && dotsRequestDto.x() >= 0 && dotsRequestDto.y() <= 0)
            return HitType.FOURTH_AREA.getHitArea();
        return HitType.MISS.getHitArea();
    }

    private boolean validateDots(DotsRequestDto dotsRequestDto) {
        if (dotsRequestDto.x() < -5 || dotsRequestDto.x() > 5)
            return false;
        if (dotsRequestDto.y() < -5 || dotsRequestDto.y() > 3)
            return false;
        return !(dotsRequestDto.r() < -5) && !(dotsRequestDto.r() > 5);
    }

    public DotsResponseDto mapDotsEntityToResponseDotsDto(DotsEntity dotsEntity) {
        double x = Math.round(dotsEntity.getX() * DIVIDER) / DIVIDER;
        double y = Math.round(dotsEntity.getY() * DIVIDER) / DIVIDER;
        double r = Math.round(dotsEntity.getR() * DIVIDER) / DIVIDER;
        return new DotsResponseDto(x, y, r, dotsEntity.getCurRequestTime(),
                dotsEntity.getExecutionTime(),
                dotsEntity.getHitType()
        );
    }
}
