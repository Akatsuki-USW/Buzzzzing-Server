package bokjak.bokjakserver.domain.congestion.service;

import bokjak.bokjakserver.domain.congestion.model.Congestion;
import bokjak.bokjakserver.domain.congestion.repository.CongestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CongestionService {
    CongestionRepository congestionRepository;

    public Optional<Congestion> getCurrentCongestionOfLocation(Long locationId) {
        return congestionRepository.findTopByLocationIdOrderByObservedAtDesc(locationId);
    }
}
