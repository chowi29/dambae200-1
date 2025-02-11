package com.dambae200.dambae200.domain.sessionInfo.service;

import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfo;
import com.dambae200.dambae200.domain.sessionInfo.domain.SessionInfoRedis;
import com.dambae200.dambae200.domain.sessionInfo.exception.AccessedExpiredSessionTokenException;
import com.dambae200.dambae200.domain.sessionInfo.exception.SessionInfoNotExistsException;
import com.dambae200.dambae200.domain.sessionInfo.repository.SessionInfoRedisRepository;
import com.dambae200.dambae200.domain.sessionInfo.repository.SessionInfoRepository;
import com.dambae200.dambae200.domain.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Primary
public class SessionServiceImplRedisRepository implements SessionService{

    final private SessionInfoRedisRepository redisRepository;
    final private SessionInfoRepository jpaRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean existsByToken(String accessToken){
        try {
            getSessionElseThrow(accessToken);
            return true;
        } catch (SessionInfoNotExistsException e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public SessionInfo getSessionElseThrow(String accessToken){

        SessionInfo sessionInfo;
        boolean cached = true;

        // 캐시 조회
        Optional<SessionInfoRedis> sessionInfoRedisOptional = redisRepository.findById(accessToken);
        if (sessionInfoRedisOptional.isPresent()) {
            sessionInfo = SessionInfoRedis.toSessionInfo(sessionInfoRedisOptional.get());
        } else {
            // 캐시에 없으면 DB 조회. DB에도 없으면 예외 발생
            sessionInfo = jpaRepository.findById(accessToken)
                    .orElseThrow(SessionInfoNotExistsException::new);
            cached = false;
        }

        // 만료되면 삭제 후 예외 발생
        removeIfExpired(sessionInfo);

        // 캐시에 없고 DB에서 가져온 것이라면 캐시에도 저장
        if (!cached)
            redisRepository.save(SessionInfoRedis.from(sessionInfo));

        // 리턴
        return sessionInfo;
    }


    @Transactional
    private void removeIfExpired(SessionInfo sessionInfo){
        try {
            sessionInfo.checkExpiration();
        } catch (AccessedExpiredSessionTokenException e){
            removeSession(sessionInfo.getAccessToken());
            throw new SessionInfoNotExistsException();
        }
    }

    // 세션 키 등록
    @Override
    @Transactional
    public SessionInfo registerSession(UserDto.GetResponse user, String userAgent) {

        // 세션정보 생성
        String accessToken = UUID.randomUUID().toString();
        SessionInfo sessionInfo = new SessionInfo.Builder(accessToken, user.getId())
                .userAgent(userAgent)
                .expirationTime(LocalDateTime.now().plusDays(1))
                .build();

        // 캐시와 DB에 저장
        redisRepository.save(SessionInfoRedis.from(sessionInfo));
        SessionInfo saved = jpaRepository.save(sessionInfo);

        return saved;
    }


    // 세션 키 삭제
    @Override
    @Transactional
    public void removeSession(String accessToken) {
        if (redisRepository.existsById(accessToken))
            redisRepository.deleteById(accessToken);
        if (jpaRepository.existsById(accessToken))
            jpaRepository.deleteById(accessToken);
    }




}
