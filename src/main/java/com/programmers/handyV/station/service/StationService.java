package com.programmers.handyV.station.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmers.handyV.common.exception.DuplicateException;
import com.programmers.handyV.station.domain.Station;
import com.programmers.handyV.station.dto.request.CreateStationRequest;
import com.programmers.handyV.station.dto.response.StationResponse;
import com.programmers.handyV.station.repository.StationRepository;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse create(CreateStationRequest request) {
        if (stationRepository.existsByName(request.name())) {
            throw new DuplicateException("이미 존재하는 이름입니다.");
        }
        Station newStation = request.toStation();
        Station savedStation = stationRepository.save(newStation);
        return StationResponse.from(savedStation);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAll(Optional<String> partialName) {
        if (partialName.isPresent()) {
            return findAllContainingPartialName(partialName.get());
        }

        List<Station> stations = stationRepository.findAll();
        return StationResponse.listOf(stations);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllContainingPartialName(String partialName) {
        List<Station> stations = stationRepository.findByPartialName(partialName);
        return StationResponse.listOf(stations);
    }
}
