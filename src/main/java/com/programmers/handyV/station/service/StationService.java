package com.programmers.handyV.station.service;

import org.springframework.stereotype.Service;

import com.programmers.handyV.common.exception.DuplicateException;
import com.programmers.handyV.station.domain.Station;
import com.programmers.handyV.station.dto.request.CreateStationRequest;
import com.programmers.handyV.station.dto.response.CreateStationResponse;
import com.programmers.handyV.station.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public CreateStationResponse create(CreateStationRequest request) {
        if (stationRepository.existsByName(request.name())) {
            throw new DuplicateException("이미 존재하는 이름입니다.");
        }
        Station newStation = request.toStation();
        Station savedStation = stationRepository.save(newStation);
        return CreateStationResponse.from(savedStation);
    }
}
