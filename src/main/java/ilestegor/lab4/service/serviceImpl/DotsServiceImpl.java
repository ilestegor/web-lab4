package ilestegor.lab4.service.serviceImpl;

import ilestegor.lab4.dto.DotsRequestDto;
import ilestegor.lab4.entity.DotsEntity;
import ilestegor.lab4.entity.HitType;
import ilestegor.lab4.repository.DotsRepository;
import ilestegor.lab4.service.DotsService;
import ilestegor.lab4.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DotsServiceImpl implements DotsService {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    private final DotsRepository dotsRepository;
    private final UserService userService;

    public DotsServiceImpl(DotsRepository dotsRepository, UserService userService) {
        this.dotsRepository = dotsRepository;
        this.userService = userService;
    }

    @Override
    public DotsEntity addDot(DotsRequestDto dotsRequestDto, Authentication authentication) {
        //TODO: add dots validation, dont know what to return when dots are not valid
        //TODO: add user validation
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
    }

    private String checkArea(DotsRequestDto dotsRequestDto) {
        if (dotsRequestDto.y() <= dotsRequestDto.r() && (-dotsRequestDto.x()>=0) && (dotsRequestDto.r() >= -dotsRequestDto.x())&& (dotsRequestDto.y()>=0))
            return HitType.SECOND_AREA.getHitArea();
        if (dotsRequestDto.y() >= -dotsRequestDto.x()-(dotsRequestDto.r()/2) && (dotsRequestDto.x() <= 0) && (dotsRequestDto.y() <= 0))
            return HitType.THIRD_AREA.getHitArea();
        if (dotsRequestDto.x() * dotsRequestDto.x() + dotsRequestDto.y() * dotsRequestDto.y() <= dotsRequestDto.r() * dotsRequestDto.r() && dotsRequestDto.x() >= 0 && dotsRequestDto.y() <= 0)
            return HitType.THIRD_AREA.getHitArea();
        return HitType.MISS.getHitArea();
    }
    private boolean validateDots(DotsRequestDto dotsRequestDto) {
        if (dotsRequestDto.x() < -5 || dotsRequestDto.x() > 5)
            return false;
        if (dotsRequestDto.y() < -5 || dotsRequestDto.y() > 3)
            return false;
        return !(dotsRequestDto.r() < -5) && !(dotsRequestDto.r() > 5);
    }
}
